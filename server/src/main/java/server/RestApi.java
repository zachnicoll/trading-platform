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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

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

        httpServer = HttpServer.create(new InetSocketAddress(getPortNumber()), 0);

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

    /**
     * Gets port number from server config file
     * @return port number retrieved from config file
     */
    private int getPortNumber() throws IOException {
        File file = new File("../../resources/config.properties");
        int portNumber = 8000;

        try (InputStream serverConfigFile = new FileInputStream(file.getAbsolutePath())) {

            Properties serverConfig = new Properties();

            // load a server config file
            serverConfig.load(serverConfigFile);

            // extract the port and ip values out
            portNumber = Integer.parseInt(serverConfig.getProperty("port"));

        } catch (IOException IOexception) {
            throw IOexception;
        }catch (NumberFormatException formatException){
            throw formatException;
        }finally {
            return portNumber;
        }

    }
}
