import com.sun.net.httpserver.HttpServer;
import handlers.orgunit.OrgUnitHandler;
import handlers.user.UserHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/user/", new UserHandler());
        server.createContext("/orgunit/", new OrgUnitHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
