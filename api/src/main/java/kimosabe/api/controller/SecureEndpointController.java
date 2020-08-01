package kimosabe.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/secure")
public class SecureEndpointController {
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> index(HttpServletRequest request, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
