package models;

import models.AuthenticationToken;
import models.User;

public class ClientInfo {

    /**
     * The singleton instance of the user information.
     */
    private static ClientInfo single_instance = null;

    public AuthenticationToken authToken;
    public User currentUser;

    /**
     * Constructor saves user data.
     */
    private ClientInfo()
    {
        authToken = null;
        currentUser = null;

    }

    /**
     * Provides global access to the singleton instance of the user.
     *
     * @return the user's JWT token.
     */
    public static ClientInfo getInstance()
    {
        if (single_instance == null)
            single_instance = new ClientInfo();

        return single_instance;
    }

    public void saveClientInfo(AuthenticationToken authToken, User currentUser)
    {
        this.authToken = authToken;
        this.currentUser = currentUser;
    }


}
