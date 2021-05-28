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
     * Update a User's attributes.
     * @param newAccountType updated AccountType
     * @param newOrganisationalUnitId updated OrganisationalUnit
     */
    public void updateUser(AccountType newAccountType, UUID newOrganisationalUnitId) {
        accountType = newAccountType;
        organisationalUnitId = newOrganisationalUnitId;
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
