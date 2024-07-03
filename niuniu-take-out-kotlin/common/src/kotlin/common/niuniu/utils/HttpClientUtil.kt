package common.niuniu.utils

import com.alibaba.fastjson2.JSONObject
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.io.IOException

object HttpClientUtil {
    private const val TIMEOUT_MSEC = 5000

    /**
     * 发送GET方式请求
     *
     * @param url 请求的URL
     * @param paramMap 请求参数
     * @return 响应内容
     */
    fun doGet(url: String, paramMap: Map<String, String>?): String = HttpClients.createDefault().use { httpClient ->
        val uri = URIBuilder(url).apply {
            paramMap?.forEach { (key, value) -> addParameter(key, value) }
        }.build()

        val httpGet = HttpGet(uri)
        httpClient.execute(httpGet).use { response ->
            if (response.statusLine.statusCode == 200) {
                EntityUtils.toString(response.entity, "UTF-8")
            } else {
                throw IOException("Unexpected response status: ${response.statusLine.statusCode}")
            }
        }
    }

    /**
     * 发送POST方式请求
     *
     * @param url 请求的URL
     * @param paramMap 请求参数
     * @return 响应内容
     * @throws IOException
     */
    @Throws(IOException::class)
    fun doPost(url: String, paramMap: Map<String, String>?): String = HttpClients.createDefault().use { httpClient ->
        val httpPost = HttpPost(url).apply {
            paramMap?.let {
                val paramList = it.map { (key, value) -> BasicNameValuePair(key, value) }
                entity = UrlEncodedFormEntity(paramList)
            }
            config = buildRequestConfig()
        }

        httpClient.execute(httpPost).use { response ->
            EntityUtils.toString(response.entity, "UTF-8")
        }
    }

    /**
     * 发送POST方式请求，传递JSON数据
     *
     * @param url 请求的URL
     * @param paramMap 请求参数
     * @return 响应内容
     * @throws IOException
     */
    @Throws(IOException::class)
    fun doPost4Json(url: String, paramMap: Map<String, String>?): String =
        HttpClients.createDefault().use { httpClient ->
            val httpPost = HttpPost(url).apply {
                paramMap?.let {
                    val jsonObject = JSONObject().apply {
                        it.forEach { (key, value) -> this[key] = value }
                    }
                    entity = StringEntity(jsonObject.toString(), "UTF-8").apply {
                        setContentEncoding("UTF-8")
                        setContentType("application/json")
                    }
                }
                config = buildRequestConfig()
            }

            httpClient.execute(httpPost).use { response ->
                EntityUtils.toString(response.entity, "UTF-8")
            }
        }

    private fun buildRequestConfig(): RequestConfig =
        RequestConfig.custom().setConnectTimeout(TIMEOUT_MSEC).setConnectionRequestTimeout(TIMEOUT_MSEC)
            .setSocketTimeout(TIMEOUT_MSEC).build()
}
