package kimosabe.api.security;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.session.FindByIndexNameSessionRepository;

import java.util.Comparator;
import java.util.List;

public class CustomConcurrentSessionStrategy extends ConcurrentSessionControlAuthenticationStrategy {
    public FindByIndexNameSessionRepository sessionRepository;

    public CustomConcurrentSessionStrategy(
            SessionRegistry sessionRegistry,
            FindByIndexNameSessionRepository sessionRepository
    ) {
        super(sessionRegistry);
        this.sessionRepository = sessionRepository;
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
}
