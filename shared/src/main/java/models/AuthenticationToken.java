package models;

/**
 * Wrapper class for returning a JSON object with the form:
 * {
 *     authenticationToken: "token"
 * }
 */
public class AuthenticationToken {
    private final String authenticationToken;

    public AuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    @Override
    public String toString() {
        return authenticationToken;
    }
}
