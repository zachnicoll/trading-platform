import com.sun.net.httpserver.HttpServer;
import handlers.assets.AssetsHandler;
import handlers.assettype.AssetTypeHandler;
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
        /**
         * TODO: Import socket address from config/environment file
         */
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Add all the handlers for each route
        server.createContext("/login/", new LoginHandler(false));
        server.createContext("/user/", new UserHandler(true));
        server.createContext("/orgunit/", new OrgUnitHandler(true));
        server.createContext("/resetpassword/", new ResetPasswordHandler(true));
        server.createContext("/refresh/", new RefreshHandler(true));
        server.createContext("/assets/", new AssetsHandler(true));
        server.createContext("/trades/", new TradesHandler(true));
        server.createContext("/assettype/", new AssetTypeHandler(true));

        // Start server with default executor
        server.setExecutor(null);
        server.start();
    }
}
