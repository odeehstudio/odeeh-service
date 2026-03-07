package be.odeeh.studio.odeehservice.config.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class FirebaseTokenFilter extends OncePerRequestFilter {

    private final FirebaseAuth firebaseAuth;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = extractToken(request);

            if (token != null) {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
                Authentication authentication = new FirebaseAuthentication(decodedToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (FirebaseAuthException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        } catch (BadCredentialsException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }
}
