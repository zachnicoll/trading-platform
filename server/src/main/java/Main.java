import database.datasources.UserDataSource;
import server.RestApi;
import server.TradeResolver;

import java.io.IOException;
import java.util.Timer;
import java.util.UUID;
import database.datasources.ResolvedTradeDataSource;
import database.DBConnection;
import models.ResolvedTrade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import models.User;


public class Main {
    private static final Integer tradeResolvePeriod = 1000 * 30;

    public static void main(String[] args) throws IOException, SQLException {
        // Start RestApi Server
        RestApi restApi = new RestApi();
        restApi.start();

        // Start Trade Resolver Task
        Timer time = new Timer();
        TradeResolver tradeResolver = new TradeResolver();
        time.schedule(tradeResolver, 0, tradeResolvePeriod);

        /*UserDataSource test = new UserDataSource();
        ArrayList<User> temp = test.getAll();
        for (User user : temp) {
            System.out.println(user.getUsername());
        }
        /*ResolvedTrade thisone = test.getById(UUID.fromString("9b406cb0-eddb-4e5a-a6b8-e80fc088284f"), UUID.fromString("467c404d-a5ad-4359-9170-523f862ef028"));

        System.out.println("DONE...");
        System.out.println(thisone.getDateResolved() +"" + thisone.getQuantity());*/
    }
}
