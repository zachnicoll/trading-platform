import com.sun.net.httpserver.HttpServer;
import handlers.assets.AssetsHandler;
import handlers.orgunit.OrgUnitHandler;
import handlers.refresh.RefreshHandler;
import handlers.resetpassword.ResetPasswordHandler;
import handlers.trades.TradesHandler;
import handlers.user.UserHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/user/", new UserHandler());
        server.createContext("/orgunit/", new OrgUnitHandler());
        server.createContext("/resetpassword/", new ResetPasswordHandler());
        server.createContext("/refresh/", new RefreshHandler());
        server.createContext("/assets/", new AssetsHandler());
        server.createContext("/trades/", new TradesHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
