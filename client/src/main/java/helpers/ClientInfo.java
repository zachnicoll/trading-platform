package helpers;

import models.AuthenticationToken;
import models.User;

/**
 * Singleton class for storing and persisting data required between all pages in the client.
 * For example, the current logged-in User and the corresponding AuthenticationToken are displayed
 * and/or used in requests throughout the client.
 */
public class ClientInfo {

    /**
     * The singleton instance of the user information.
     */
    private static ClientInfo single_instance = null;

    private AuthenticationToken authToken;
    private User currentUser;

    /**
     * Constructor saves user data.
     */
    private ClientInfo() {
        authToken = null;
        currentUser = null;
    }

    /**
     * Provides global access to the singleton instance of the user.
     *
     * @return the user's JWT token.
     */
    public static ClientInfo getInstance() {
        if (single_instance == null)
            single_instance = new ClientInfo();

        return single_instance;
    }

    /**
     * Save AuthenticationToken and User to persist them.
     */
    public void saveClientInfo(AuthenticationToken authToken, User currentUser) {
        this.authToken = authToken;
        this.currentUser = currentUser;
    }

    /**
     * Set the stored client information to null.
     */
    public void resetClientInfo() {
        authToken = null;
        currentUser = null;
    }

    /**
     * @return The current stored User
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @return The current stored AuthenticationToken
     */
    public AuthenticationToken getAuthToken() {
        return authToken;
    }
}
