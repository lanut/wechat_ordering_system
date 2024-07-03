package common.niuniu.properties

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "niuniu.wechat")
data class WeChatProperties (
    val appid: String, //小程序的appid
    val secret: String, //小程序的秘钥

    // TODO：后面的不需要设置，记得删除
    val mchid: String = "", //商户号
    val mchSerialNo: String = "", //商户API证书的证书序列号
    val privateKeyFilePath: String = "", //商户私钥文件
    val apiV3Key: String = "", //证书解密的密钥
    val weChatPayCertFilePath: String = "", //平台证书
    val notifyUrl: String = "", //支付成功的回调地址
    val refundNotifyUrl: String = "", //退款成功的回调地址
)
