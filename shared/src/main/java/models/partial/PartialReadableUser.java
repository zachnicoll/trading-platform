package models.partial;

import models.AccountType;

import java.util.UUID;

/**
 * Class similar to the User class, but all attributes human readable.
 * This class is used when creating a new User from the server to
 * display on the client in a GUI component.
 */
public class PartialReadableUser {
    private String username;
    private AccountType accountType;
    private String organisationalUnitName;

    public PartialReadableUser(String username, AccountType accountType, String organisationalUnitName){
        this.username  = username;
        this.accountType = accountType;
        this.organisationalUnitName = organisationalUnitName;
    }

    public String getOrganisationalUnitName() {
        return organisationalUnitName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getUsername() {
        return username;
    }
}