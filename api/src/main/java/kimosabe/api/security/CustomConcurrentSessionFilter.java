package kimosabe.api.security;

import kimosabe.api.constants.AppConstants;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomConcurrentSessionFilter extends GenericFilterBean {
    private FindByIndexNameSessionRepository sessionRepository;
    public CustomConcurrentSessionFilter(FindByIndexNameSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        doFilterInternal((HttpServletRequest) req, (HttpServletResponse) res, chain);
    }

    private void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        Principal principal = req.getUserPrincipal();
        if (principal != null ) {
            Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName(principal.getName());
            int numExpired = sessions.size() > AppConstants.MAX_NUM_SESSIONS ?
                    sessions.size() - AppConstants.MAX_NUM_SESSIONS : 0;
            List<Map.Entry<String, ? extends Session>> expiredSessions = sessions.entrySet().stream()
                    .sorted(Comparator.comparing(s -> s.getValue().getCreationTime()))
                    .limit(numExpired).collect(Collectors.toList());
            expiredSessions.forEach(e -> sessionRepository.deleteById(e.getKey()));
        }
        chain.doFilter(req, res);
    }
}
