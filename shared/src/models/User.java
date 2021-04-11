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
     * Organisation Unit the User belongs to.
     */
    private OrganisationalUnit organisationalUnit;

    /**
     * JWT token supplied by the server upon logging-in.
     * May need to be refreshed if it expires.
     */
    private String authenticationToken;

    /**
     *
     * @param username
     * @param accountType
     * @param organisationalUnit
     */
    User (String userId, String username, AccountType accountType, OrganisationalUnit organisationalUnit) {
        this.userId = userId;
        this.username = username;
        this.accountType = accountType;
        this.organisationalUnit = organisationalUnit;
    }

    /**
     *
     * @param password
     * @throws AuthenticationException
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
     *
     */
    public void Logout() {
        return;
    }

    /**
     *
     * @return
     */
    public Boolean IsAuthenticated() {
        return true;
    }

    /**
     *
     * @param newAccountType
     * @param newOrganisationalUnit
     * @throws ApiException
     */
    public void updateUser(AccountType newAccountType, OrganisationalUnit newOrganisationalUnit) throws ApiException {
        /**
         * Make API request here to update User, this API request throws ApiException
         */

        accountType = newAccountType;
        organisationalUnit = newOrganisationalUnit;
    }

    /**
     *
     * @throws ApiException
     */
    public void deleteUser() throws ApiException {
        return;
    }

    /**
     *
     * @return
     */
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     *
     * @return
     */
    public OrganisationalUnit getOrganisationalUnit() {
        return organisationalUnit;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    public String getUserId() {
        return userId;
    }
}
