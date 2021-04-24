package models;

import exceptions.ApiException;

import javax.naming.AuthenticationException;
import java.util.UUID;

/**
 * Class for storing and manipulating User information locally and on the server.
 * Can be used for simply displaying User information to Admins, or operating as
 * an authenticate User currently logged-in to the client.
 *
 * @see OrganisationalUnit
 * @see AccountType
 */
public class User {
    /**
     * UUID String identifying the User.
     * Unique.
     */
    private final UUID userId;

    /**
     * Username displayed publicly to identify User.
     * Unique.
     */
    private final String username;

    /**
     * Defines the set of privileges associated with the User.
     */
    private AccountType accountType;

    /**
     * Organisational Unit the User belongs to.
     */
    private UUID organisationalUnitId;

    /**
     * JWT token supplied by the server upon logging-in.
     * May need to be refreshed if it expires.
     */
    private String authenticationToken;

    /**
     * Construct a new User with given information, most likely provided by the API.
     * @param userId Unique identifier for User
     * @param username Displayed name of User
     * @param accountType Representation of privileges given to User
     * @param organisationalUnitId The ID of the Organisational Unit the User belongs to
     */
    public User(UUID userId, String username, AccountType accountType, UUID organisationalUnitId) {
        this.userId = userId;
        this.username = username;
        this.accountType = accountType;
        this.organisationalUnitId = organisationalUnitId;
    }

    /**
     * Hash the given password and make a request to the /login API endpoint - if
     * successful, the JWT token provided by the server will be returned.
     * @param username Username of the User to be logged-in
     * @param password String will be hashed before being sent to server
     * @return authentication token as a string
     * @throws AuthenticationException if login is unsuccessful
     */
    public static String Login(String username, String password) throws AuthenticationException {
        /**
         * Hash password before sending to API
         */

        /**
         * Make API request with username and password
         */

        /**
         * Set authenticationToken as token supplied by server
         */

        return "";
    }

    /**
     * Remove the User's authentication token, rendering them 'unauthenticated'.
     */
    public void Logout() {
        authenticationToken = null;
        return;
    }

    /**
     * Determine whether or not a User has an authentication token that has not expired.
     * @return true if authentication token is present and has not expired, false otherwise
     */
    public Boolean IsAuthenticated() {
        if (authenticationToken != null) {
            /**
             * Check if authentication token has not expired
             */

            return true;
        }
        return false;
    }

    /**
     * Update a User's attribute via the /user endpoint. If successful, the User object
     * will also be updated.
     * @param newAccountType updated AccountType
     * @param newOrganisationalUnitId updated OrganisationalUnit
     * @throws ApiException if an error occurs while making the API request
     */
    public void updateUser(AccountType newAccountType, UUID newOrganisationalUnitId) throws ApiException {
        /**
         * Make PUT API request here to update User, this API request throws ApiException
         */

        accountType = newAccountType;
        organisationalUnitId = newOrganisationalUnitId;
    }

    /**
     * Change a User's password via the /user/[userId]/change-password endpoint
     * @throws ApiException if an error occurs while making the API request
     */
    public void changePassword(String password, String confirmPassword) throws ApiException, AuthenticationException {
        if (password.compareTo(confirmPassword) != 0) {
            throw new AuthenticationException();
        }

        /**
         * Hash the password
         */

        /**
         * Make POST request to the API to update password
         */
        return;
    }

    /**
     * Set a User's authentication token after calling the static User.Login() method.
     * @param authenticationToken JWT token as a String
     */
    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    /**
     * Get the AccountType of the User for displaying.
     * @return the User's AccountType
     */
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     * Get the OrganisationalUnit of the User for displaying.
     * @return the User's OrganisationalUnit
     */
    public UUID getOrganisationalUnitId() {
        return organisationalUnitId;
    }

    /**
     * Get the username of the User for displaying.
     * @return the User's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the User ID of the User for displaying
     * @return the User's ID
     */
    public UUID getUserId() {
        return userId;
    }
}
