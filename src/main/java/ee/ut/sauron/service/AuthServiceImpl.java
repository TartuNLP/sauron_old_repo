package ee.ut.sauron.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final List<String> allowedTokens;

    public AuthServiceImpl() {
        String envVar = System.getenv("SAURON_ALLOWED_TOKENS");
        log.info("SAURON_ALLOWED_TOKENS = " + envVar);
        this.allowedTokens = Arrays.asList(envVar.split(";"));
        log.info("Allowed auth tokens: " + allowedTokens.toString());
    }

    @Override
    public boolean isAuthenticated(String authToken) {
        boolean auth = allowedTokens.contains(authToken);
        if (!auth) {
            log.warn(String.format("Access denied for auth token '%s'", authToken));
        }
        return auth;
    }
}
