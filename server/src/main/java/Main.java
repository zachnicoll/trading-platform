import server.RestApi;
import server.TradeResolver;

import java.io.IOException;
import java.util.Timer;
import java.sql.SQLException;


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
    }
}
