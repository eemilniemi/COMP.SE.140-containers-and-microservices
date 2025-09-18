import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.File;
import java.lang.management.ManagementFactory;




public class Service2 {
    public static void main (String[] args) throws IOException {
        int port = 8200; //TODO process.env
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);


        server.createContext("/status", exchange -> {
            LogWriter.saveLog();

            String response = "testi";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        server.setExecutor(null);
        System.out.println("Server started at http://localhost:" + port);
        server.start();
    }
}