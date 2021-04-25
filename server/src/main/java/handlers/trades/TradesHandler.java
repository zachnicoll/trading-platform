package handlers.trades;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.AccountType;
import models.Trade;
import models.TradeType;
import models.User;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class TradesHandler extends RequestHandler {

    public TradesHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange t) throws IOException {
        Trade trade = new Trade(TradeType.BUY, UUID.randomUUID(), UUID.randomUUID(), 100, 1.045f, new Date());
        writeResponseBody(t, trade);
    }

    @Override
    protected void handlePost(HttpExchange t) throws IOException {
        respondNotImplemented(t);
    }

    @Override
    protected void handlePut(HttpExchange t) throws IOException {
        respondNotImplemented(t);
    }

    @Override
    protected void handleDelete(HttpExchange t) throws IOException {
        respondNotImplemented(t);
    }
}
