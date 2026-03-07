package be.odeeh.studio.odeehservice.config.security;

import com.google.firebase.auth.FirebaseToken;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class FirebaseAuthentication implements Authentication {

    private final FirebaseToken token;
    private boolean authenticated = true;

    public FirebaseAuthentication(FirebaseToken token) {
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public @Nullable Object getCredentials() {
        return token;
    }

    @Override
    public @Nullable Object getDetails() {
        return token;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return token.getUid();
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return token.getUid();
    }

    public String getUid() {
        return token.getUid();
    }

    public String getEmail() {
        return token.getEmail();
    }
}
