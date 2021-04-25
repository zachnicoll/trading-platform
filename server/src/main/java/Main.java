import com.sun.net.httpserver.HttpServer;
import handlers.assets.AssetsHandler;
import handlers.login.LoginHandler;
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

        server.createContext("/login/", new LoginHandler(false));
        server.createContext("/user/", new UserHandler(true));
        server.createContext("/orgunit/", new OrgUnitHandler(true));
        server.createContext("/resetpassword/", new ResetPasswordHandler(true));
        server.createContext("/refresh/", new RefreshHandler(true));
        server.createContext("/assets/", new AssetsHandler(true));
        server.createContext("/trades/", new TradesHandler(true));

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
