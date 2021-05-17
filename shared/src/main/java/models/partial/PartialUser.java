package models.partial;

import models.AccountType;

import java.util.UUID;

/**
 * Class similar to the User class, but contains no methods and all attributes
 * are publicly accessible. This class is used when creating a new User,
 * as only certain attributes need to be sent to the Rest API. For example,
 * the UserId is generated on the Server and therefore should not be sent
 * with a POST request to the /user/ endpoint.
 */
public class PartialUser {
    public final String username;
    public final AccountType accountType;
    public final UUID organisationalUnitId;
    public final String password;

    public PartialUser(String username, AccountType accountType, UUID organisationalUnitId, String password){
        this.username  = username;
        this.accountType = accountType;
        this.organisationalUnitId = organisationalUnitId;
        this.password = password;
    }
}
