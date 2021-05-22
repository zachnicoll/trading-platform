package server;

import com.sun.net.httpserver.HttpServer;
import handlers.AssetsHandler;
import handlers.AssetTypeHandler;
import handlers.LoginHandler;
import handlers.OrgUnitHandler;
import handlers.RefreshHandler;
import handlers.ResetPasswordHandler;
import handlers.TradesHandler;
import handlers.UserHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Wrapper class for a HttpServer object to ensure that correct routes are constructed
 * every time. Required encapsulation for testing the server as well.
 */
public class RestApi {
    private final HttpServer httpServer;

    /**
     * Create a new HttpServer object and assign internally. The HttpServer's contexts for various
     * routes are created also. Server port configuration is performed here.
     * @throws IOException
     */
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

    /**
     * Start the server.
     */
    public void start() {
        httpServer.start();
    }

    /**
     * Stop the server.
     */
    public void stop() {
        httpServer.stop(0);
    }
}
