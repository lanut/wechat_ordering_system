package common.niuniu.config

import common.niuniu.properties.JwtProperties
import common.niuniu.properties.WeChatProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class JwtPropertiesConfiguration

@Configuration
@EnableConfigurationProperties(WeChatProperties::class)
class WeChatPropertiesConfiguration