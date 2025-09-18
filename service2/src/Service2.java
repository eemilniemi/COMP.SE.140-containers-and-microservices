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
            try {
                String log = LogWriter.saveLog();

                byte[] responseBytes = log.getBytes();

                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            } catch (IOException e) {
                exchange.sendResponseHeaders(500, 0);
                exchange.close();
                System.err.println("Error handling /status request: " + e.getMessage());
            }
        });

        server.setExecutor(null);
        System.out.println("Server started at http://localhost:" + port);
        server.start();
    }
}