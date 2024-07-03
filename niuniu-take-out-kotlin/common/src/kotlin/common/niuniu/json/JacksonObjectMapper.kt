package common.niuniu.json

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class JacksonObjectMapper : ObjectMapper() {
    init {
        // 收到未知属性时不报异常
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        // 反序列化时，属性不存在的兼容处理
        this.deserializationConfig.withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        // 加上三种时间格式对应的序列化器和反序列化器
        val simpleModule = SimpleModule()
            .addDeserializer(
                LocalDateTime::class.java, LocalDateTimeDeserializer(
                    DateTimeFormatter.ofPattern(
                        DEFAULT_DATE_TIME_FORMAT
                    )
                )
            )
            .addDeserializer(
                LocalDate::class.java,
                LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT))
            )
            .addDeserializer(
                LocalTime::class.java,
                LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT))
            )
            .addSerializer(
                LocalDateTime::class.java, LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern(
                        DEFAULT_DATE_TIME_FORMAT
                    )
                )
            )
            .addSerializer(LocalDate::class.java, LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
            .addSerializer(LocalTime::class.java, LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))

        // 注册功能模块 例如，可以添加自定义序列化器和反序列化器
        this.registerModule(simpleModule)
    }

    companion object {
        const val DEFAULT_DATE_FORMAT: String = "yyyy-MM-dd"
        const val DEFAULT_DATE_TIME_FORMAT: String = "yyyy-MM-dd HH:mm:ss"
        const val DEFAULT_TIME_FORMAT: String = "HH:mm:ss"
    }
}
