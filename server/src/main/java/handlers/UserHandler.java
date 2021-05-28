package handlers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sun.net.httpserver.HttpExchange;
import database.datasources.UserDataSource;

import handlers.AbstractRequestHandler;
import models.AccountType;
import models.AuthenticationToken;
import models.User;
import models.partial.PartialUser;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import database.DBConnection;
import models.Asset;
import models.ResolvedTrade;
import org.xml.sax.ext.DeclHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Route: /user/
 * <p>
 * Supported Methods:
 */
public class UserHandler extends AbstractRequestHandler {

    public UserHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    private final Connection dbConnection = DBConnection.getInstance();

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException, SQLException {
        UserDataSource userDataSource = new UserDataSource();

        if(getUserId(exchange) == null){
            //sends all users to the client
            ArrayList<User> users = userDataSource.getAll();
            writeResponseBody(exchange, users);

        }else{
            String userId = getUserId(exchange);

            writeResponseBody(exchange, userDataSource.getById(UUID.fromString(userId)));
        }

    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {
        //only admin can create new users
        checkIsAdmin(exchange);

        PartialUser partialUser = (PartialUser) readRequestBody(exchange, PartialUser.class);
        User fullUser = new User(
                UUID.randomUUID(),
                partialUser.username,
                partialUser.accountType,
                partialUser.organisationalUnitId
        );

        String hashedPassword = BCrypt.withDefaults().hashToString(12, partialUser.password.toCharArray());

        UserDataSource userDataSource = new UserDataSource();

        userDataSource.createNew(fullUser, hashedPassword);

        writeResponseBody(exchange, fullUser);
    }
}
