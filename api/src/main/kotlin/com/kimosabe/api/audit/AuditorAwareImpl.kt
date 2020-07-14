package com.kimosabe.api.audit

import com.kimosabe.api.model.User
import org.springframework.data.domain.AuditorAware
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class AuditorAwareImpl : AuditorAware<UUID> {
  override fun getCurrentAuditor(): Optional<UUID> {
    val auth = SecurityContextHolder.getContext().authentication
    return if (!auth.isAuthenticated || auth is AnonymousAuthenticationToken) {
      Optional.empty()
    } else Optional.of((auth.principal as User).userId)
  }
}
