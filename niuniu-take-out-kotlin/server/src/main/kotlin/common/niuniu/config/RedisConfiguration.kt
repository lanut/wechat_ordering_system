package common.niuniu.config

import common.niuniu.utils.logInfo
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@Slf4j
class RedisConfiguration {
    @Bean
    fun RedisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        logInfo("开始创建redis模板对象...")
        val redisTemplate = RedisTemplate<String, Any>()
        // 设置redis的连接工厂对象
        redisTemplate.connectionFactory = redisConnectionFactory
        // 设置redis key的序列化器
        redisTemplate.keySerializer = StringRedisSerializer()
        return redisTemplate
    }
}
