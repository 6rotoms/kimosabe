package com.kimosabe.api.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/secure")
class SecureEndpointController {
  @GetMapping
  @PreAuthorize("hasRole('USER')")
  fun index(request: HttpServletRequest?, session: HttpSession): ResponseEntity<*>? {
    val authentication: Authentication = SecurityContextHolder.getContext().authentication
    return ResponseEntity<Any?>(authentication.principal, HttpStatus.OK)
  }
}