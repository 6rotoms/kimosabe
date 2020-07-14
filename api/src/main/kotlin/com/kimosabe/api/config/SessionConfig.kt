package com.kimosabe.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer
import org.springframework.session.web.http.HeaderHttpSessionIdResolver

import org.springframework.session.web.http.HttpSessionIdResolver

@Configuration
@EnableRedisHttpSession
@Order(Ordered.HIGHEST_PRECEDENCE)
class SessionConfig : AbstractHttpSessionApplicationInitializer() {
  @Bean
  fun httpSessionIdResolver(): HttpSessionIdResolver? {
    return HeaderHttpSessionIdResolver.xAuthToken()
  }
}