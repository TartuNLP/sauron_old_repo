package ee.ut.sauron.service;

public interface AuthService {

    boolean isAuthenticated(String authToken);
}
