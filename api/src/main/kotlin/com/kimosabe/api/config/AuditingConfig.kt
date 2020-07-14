package com.kimosabe.api.config

import com.kimosabe.api.audit.AuditorAwareImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@Configuration
@EnableJpaAuditing
class AuditingConfig {
  @Bean
  fun auditorProvider(): AuditorAware<UUID> {
    return AuditorAwareImpl()
  }
}