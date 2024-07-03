package common.niuniu.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil
import common.niuniu.properties.WeChatProperties
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpHeaders
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.math.BigDecimal
import java.security.PrivateKey
import java.security.Signature
import java.security.cert.X509Certificate
import java.util.*

/**
 * 微信支付工具类
 */
@Component
class WeChatPayUtil {

    companion object {
        //微信支付下单接口地址
        const val JSAPI = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi"

        //申请退款接口地址
        const val REFUNDS = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds"
    }

    @Autowired
    private lateinit var weChatProperties: WeChatProperties

    /**
     * 获取调用微信接口的客户端工具对象
     *
     * @return
     */
    private fun getClient(): CloseableHttpClient? {
        return try {
            val merchantPrivateKey: PrivateKey = PemUtil.loadPrivateKey(FileInputStream(File(weChatProperties.privateKeyFilePath)))
            val x509Certificate: X509Certificate = PemUtil.loadCertificate(FileInputStream(File(weChatProperties.weChatPayCertFilePath)))
            val wechatPayCertificates: List<X509Certificate> = listOf(x509Certificate)

            val builder = WechatPayHttpClientBuilder.create()
                .withMerchant(weChatProperties.mchid, weChatProperties.mchSerialNo, merchantPrivateKey)
                .withWechatPay(wechatPayCertificates)

            // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
            builder.build()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 发送post方式请求
     *
     * @param url
     * @param body
     * @return
     */
    @Throws(Exception::class)
    private fun post(url: String, body: String): String {
        val httpClient = getClient() ?: throw Exception("HttpClient is null")

        val httpPost = HttpPost(url).apply {
            addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString())
            addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
            addHeader("Wechatpay-Serial", weChatProperties.mchSerialNo)
            entity = StringEntity(body, "UTF-8")
        }

        val response: CloseableHttpResponse = httpClient.execute(httpPost)
        return try {
            EntityUtils.toString(response.entity)
        } finally {
            httpClient.close()
            response.close()
        }
    }

    /**
     * 发送get方式请求
     *
     * @param url
     * @return
     */
    @Throws(Exception::class)
    private fun get(url: String): String {
        val httpClient = getClient() ?: throw Exception("HttpClient is null")

        val httpGet = HttpGet(url).apply {
            addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString())
            addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
            addHeader("Wechatpay-Serial", weChatProperties.mchSerialNo)
        }

        val response: CloseableHttpResponse = httpClient.execute(httpGet)
        return try {
            EntityUtils.toString(response.entity)
        } finally {
            httpClient.close()
            response.close()
        }
    }

    /**
     * jsapi下单
     *
     * @param orderNum    商户订单号
     * @param total       总金额
     * @param description 商品描述
     * @param openid      微信用户的openid
     * @return
     */
    @Throws(Exception::class)
    private fun jsapi(orderNum: String, total: BigDecimal, description: String, openid: String): String {
        val jsonObject = JSONObject().apply {
            put("appid", weChatProperties.appid)
            put("mchid", weChatProperties.mchid)
            put("description", description)
            put("out_trade_no", orderNum)
            put("notify_url", weChatProperties.notifyUrl)
            put("amount", JSONObject().apply {
                put("total", total.multiply(BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toInt())
                put("currency", "CNY")
            })
            put("payer", JSONObject().apply {
                put("openid", openid)
            })
        }
        val body = jsonObject.toJSONString()
        return post(JSAPI, body)
    }

    /**
     * 小程序支付
     *
     * @param orderNum    商户订单号
     * @param total       金额，单位 元
     * @param description 商品描述
     * @param openid      微信用户的openid
     * @return
     */
    @Throws(Exception::class)
    fun pay(orderNum: String, total: BigDecimal, description: String, openid: String): JSONObject {
        //统一下单，生成预支付交易单
        val bodyAsString = jsapi(orderNum, total, description, openid)
        //解析返回结果
        val jsonObject = JSON.parseObject(bodyAsString)
        println(jsonObject)

        val prepayId = jsonObject.getString("prepay_id")
        return if (prepayId != null) {
            val timeStamp = (System.currentTimeMillis() / 1000).toString()
            val nonceStr = RandomStringUtils.randomNumeric(32)
            val list = arrayListOf<Any>(
                weChatProperties.appid,
                timeStamp,
                nonceStr,
                "prepay_id=$prepayId"
            )
            //二次签名，调起支付需要重新签名
            val signMessage = list.joinToString("\n")
            val message = signMessage.toByteArray()

            val signature = Signature.getInstance("SHA256withRSA").apply {
                initSign(PemUtil.loadPrivateKey(FileInputStream(File(weChatProperties.privateKeyFilePath))))
                update(message)
            }
            val packageSign = Base64.getEncoder().encodeToString(signature.sign())

            //构造数据给微信小程序，用于调起微信支付
            JSONObject().apply {
                put("timeStamp", timeStamp)
                put("nonceStr", nonceStr)
                put("package", "prepay_id=$prepayId")
                put("signType", "RSA")
                put("paySign", packageSign)
            }
        } else {
            jsonObject
        }
    }

    /**
     * 申请退款
     *
     * @param outTradeNo    商户订单号
     * @param outRefundNo   商户退款单号
     * @param refund        退款金额
     * @param total         原订单金额
     * @return
     */
    @Throws(Exception::class)
    fun refund(outTradeNo: String, outRefundNo: String, refund: BigDecimal, total: BigDecimal): String {
        val jsonObject = JSONObject().apply {
            put("out_trade_no", outTradeNo)
            put("out_refund_no", outRefundNo)
            put("amount", JSONObject().apply {
                put("refund", refund.multiply(BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toInt())
                put("total", total.multiply(BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toInt())
                put("currency", "CNY")
            })
            put("notify_url", weChatProperties.refundNotifyUrl)
        }
        val body = jsonObject.toJSONString()

        //调用申请退款接口
        return post(REFUNDS, body)
    }
}
