import server.RestApi;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        RestApi restApi = new RestApi();
        restApi.start();
    }
}
