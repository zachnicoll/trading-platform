package models;

import exceptions.ApiException;

import javax.naming.AuthenticationException;

public class User {
    /**
     * UUID String identifying the User.
     * Unique.
     */
    private final String userId;

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
    private OrganisationalUnit organisationalUnit;

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
     * @param organisationalUnit Organisational Unit the User belongs to
     */
    User (String userId, String username, AccountType accountType, OrganisationalUnit organisationalUnit) {
        this.userId = userId;
        this.username = username;
        this.accountType = accountType;
        this.organisationalUnit = organisationalUnit;
    }

    /**
     * Hash the given password and make a request to the /login API endpoint - if
     * successful, the JWT token provided by the server will be stored in the User.
     * @param password String will be hashed before being sent to server
     * @throws AuthenticationException if login is unsuccessful
     */
    public void Login(String password) throws AuthenticationException {
        /**
         * Hash password before sending to API
         */

        /**
         * Make API request with username and password
         */

        /**
         * Set authenticationToken as token supplied by server
         */

        return;
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
     * @param newOrganisationalUnit updated OrganisationalUnit
     * @throws ApiException if an error occurs while making the API request
     */
    public void updateUser(AccountType newAccountType, OrganisationalUnit newOrganisationalUnit) throws ApiException {
        /**
         * Make PUT API request here to update User, this API request throws ApiException
         */

        accountType = newAccountType;
        organisationalUnit = newOrganisationalUnit;
    }

    /**
     * Change a User's password via the /user/[userId]/change-password endpoint
     * @throws ApiException if an error occurs while making the API request
     */
    public void changePassword(String password) throws ApiException {
        /**
         * Hash the password
         */

        /**
         * Make POST request to the API to update password
         */
        return;
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
    public OrganisationalUnit getOrganisationalUnit() {
        return organisationalUnit;
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
    public String getUserId() {
        return userId;
    }
}
