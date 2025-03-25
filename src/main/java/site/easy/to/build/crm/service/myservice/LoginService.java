package site.easy.to.build.crm.service.myservice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public boolean hasRoleManager(HttpSession session) {

        if (session == null) {
            return false; // Session non trouvée
        }

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");

        if (securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if (authentication != null) {
                return authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));
            }
        }
        return false;
    }
    private HttpSession getSessionById(String sessionId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getId().equals(sessionId)) {
            return session;
        }
        return null;
}


}

