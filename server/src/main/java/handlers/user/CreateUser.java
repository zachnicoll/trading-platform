package handlers.user;

import models.AccountType;

import java.util.UUID;

public class CreateUser {
    public final String username;
    public final AccountType accountType;
    public final UUID organisationalUnitId;
    public final String password;

    public CreateUser(String username, AccountType accountType, UUID organisationalUnitId, String password){
        this.username  = username;
        this.accountType = accountType;
        this.organisationalUnitId = organisationalUnitId;
        this.password = password;
    }
}
