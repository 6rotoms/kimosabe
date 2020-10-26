package kimosabe.api.security;

import kimosabe.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;

@Component
public class CustomConcurrentSessionStrategy extends ConcurrentSessionControlAuthenticationStrategy {
    private final FindByIndexNameSessionRepository sessionRepository;
    private final UserService userService;

    @Autowired
    public CustomConcurrentSessionStrategy(
            SessionRegistry sessionRegistry,
            FindByIndexNameSessionRepository sessionRepository,
            UserService userService
    ) {
        super(sessionRegistry);
        this.sessionRepository = sessionRepository;
        this.userService = userService;
    }

    @Override
    protected void allowableSessionsExceeded(List<SessionInformation> sessions, int allowableSessions, SessionRegistry registry) throws SessionAuthenticationException {
        // Determine least recently used sessions, and mark them for invalidation
        sessions.sort(Comparator.comparing(SessionInformation::getLastRequest));
        int maximumSessionsExceededBy = sessions.size() - allowableSessions + 1;
        List<SessionInformation> sessionsToBeExpired = sessions.subList(0, maximumSessionsExceededBy);
        for (SessionInformation session : sessionsToBeExpired) {
            session.expireNow();
            sessionRepository.deleteById(session.getSessionId());
        }
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        super.onAuthentication(authentication, request, response);
        userService.updateLoginTime(authentication.getName());
    }
}
