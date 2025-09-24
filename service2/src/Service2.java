import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.File;
import java.lang.management.ManagementFactory;





public class Service2 {
    public static void main (String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("SERVICE2PORT", "8200"));
        int storagePort = Integer.parseInt(System.getenv().getOrDefault("STORAGEPORT", "8201"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        HttpClient client = HttpClient.newHttpClient();

        server.createContext("/status", exchange -> {
            try {
                String log = LogWriter.saveLog();

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://storage:" + storagePort + "/log"))
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString(log))
                    .build();

                client.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                    .exceptionally(ex -> {
                        System.err.println("Error sending log to storage: " + ex.getMessage());
                        return null;
                    });


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