package handlers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sun.net.httpserver.HttpExchange;
import database.datasources.AssetTypeDataSource;
import database.datasources.UserDataSource;

import errors.JsonError;
import handlers.AbstractRequestHandler;
import models.AccountType;
import models.AuthenticationToken;
import models.User;
import models.partial.PartialReadableUser;
import models.partial.PartialUser;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
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
        String[] params = exchange.getRequestURI().getRawPath().split("/");

        if (params.length == 3 && params[2].contains("all")) {

            //sends all users to the client in readable form
            ArrayList<PartialReadableUser> users = userDataSource.getAllReadable();
            writeResponseBody(exchange, users);

        } else {

            String userId = getUserId(exchange);
            writeResponseBody(exchange, userDataSource.getById(UUID.fromString(userId)));
        }

    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {
        //only admin can create new users
        checkIsAdmin(exchange);
        PartialUser partialUser = (PartialUser) readRequestBody(exchange, PartialUser.class);

        if (Objects.nonNull(partialUser)) {

            if (partialUser.password.isBlank()) {

                JsonError jsonError = new JsonError("User does not contain a password");
                writeResponseBody(exchange, jsonError, 400);

            } else if (partialUser.username.isBlank()) {

                JsonError jsonError = new JsonError("User does not contain a username");
                writeResponseBody(exchange, jsonError, 400);

            } else if (partialUser.organisationalUnitId.toString().isBlank()) {

                JsonError jsonError = new JsonError("User does not contain an organisational unit Id");
                writeResponseBody(exchange, jsonError, 400);

            } else {
                User fullUser = new User(
                        UUID.randomUUID(),
                        partialUser.username,
                        partialUser.accountType,
                        partialUser.organisationalUnitId
                );
                UserDataSource userDataSource = new UserDataSource();

                String hashedPassword = BCrypt.withDefaults().hashToString(12, partialUser.password.toCharArray());
                userDataSource.createNew(fullUser, hashedPassword);
                writeResponseBody(exchange, fullUser);

            }
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws SQLException, IOException {
        checkIsAdmin(exchange);
        String[] params = exchange.getRequestURI().getRawPath().split("/");

        if (params.length == 3) {
            // User ID is present in the URL, use it to delete user
            UserDataSource userDataSource = new UserDataSource();
            UUID userId = UUID.fromString(params[2]);
            userDataSource.deleteById(userId);
            writeResponseBody(exchange, null);
        } else {
            JsonError jsonError = new JsonError("User not found");
            writeResponseBody(exchange, jsonError, 404);
        }
    }
}
