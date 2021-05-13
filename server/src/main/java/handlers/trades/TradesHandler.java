package handlers.trades;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.OpenTradeDataSource;
import handlers.AbstractRequestHandler;
import models.OpenTrade;
import models.TradeType;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Route: /trades/
 *
 * Supported Methods:
 *
 */
public class TradesHandler extends AbstractRequestHandler {

    public TradesHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        /*
         * Example of how to block route method if Admin AccountType is required.
         * TODO: remove this when route is implemented correctly.
         */
        checkIsAdmin(exchange);

        OpenTrade trade = new OpenTrade(UUID.randomUUID(), TradeType.BUY, UUID.randomUUID(), UUID.randomUUID(), 100, 1.045f, Timestamp.from(Instant.now()));
        writeResponseBody(exchange, trade);
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {
        // Make new OpenTrade object from json in request body
        OpenTrade newTrade = (OpenTrade) readRequestBody(exchange, OpenTrade.class);

        // Create new trade in DB
        OpenTradeDataSource openTradeDataSource = new OpenTradeDataSource();
        openTradeDataSource.createNew(newTrade);

        // Respond with created Object
        writeResponseBody(exchange, newTrade);
    }
}
