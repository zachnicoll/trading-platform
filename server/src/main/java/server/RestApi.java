package server;

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

public class RestApi {
    private final HttpServer httpServer;

    public RestApi() throws IOException {
        /**
         * TODO: Import socket address from config/environment file
         */
        httpServer = HttpServer.create(new InetSocketAddress(8000), 0);

        // Add all the handlers for each route
        httpServer.createContext("/login/", new LoginHandler(false));
        httpServer.createContext("/user/", new UserHandler(true));
        httpServer.createContext("/orgunit/", new OrgUnitHandler(true));
        httpServer.createContext("/resetpassword/", new ResetPasswordHandler(true));
        httpServer.createContext("/refresh/", new RefreshHandler(true));
        httpServer.createContext("/assets/", new AssetsHandler(true));
        httpServer.createContext("/trades/", new TradesHandler(true));
        httpServer.createContext("/assettype/", new AssetTypeHandler(true));

        // Set default executor
        httpServer.setExecutor(null);
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}
