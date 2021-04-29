package handlers.trades;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.Trade;
import models.TradeType;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Route: /trades/
 *
 * Supported Methods:
 *
 */
public class TradesHandler extends RequestHandler {

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

        Trade trade = new Trade(UUID.randomUUID(), TradeType.BUY, UUID.randomUUID(), UUID.randomUUID(), 100, 1.045f, new Date());
        writeResponseBody(exchange, trade);
    }
}