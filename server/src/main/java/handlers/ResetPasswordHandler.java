package handlers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sun.net.httpserver.HttpExchange;
import database.datasources.UserDataSource;
import errors.JsonError;
import handlers.AbstractRequestHandler;

import models.NewPassword;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Route: /resetpassword/
 * <p>
 * Supported Methods:
 */
public class ResetPasswordHandler extends AbstractRequestHandler {

    public ResetPasswordHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {

        UserDataSource userDataSource = new UserDataSource();
        UUID userId = UUID.fromString(getUserId(exchange));
        NewPassword capturedPassword = (NewPassword) readRequestBody(exchange, NewPassword.class);
        // If passwords match, change user's password
        if (capturedPassword.checkMatchingPasswords()) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, capturedPassword.confirmPassword.toCharArray());
            userDataSource.changePassword(userId, hashedPassword);
            writeResponseBody(exchange, null, 200);
        }
        else
        {
            writeResponseBody(exchange, new JsonError("Password and ConfirmPassword do not match"), 404);
        } 
    }
}
