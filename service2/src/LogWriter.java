import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class LogWriter {

    public static String saveLog() throws IOException {
        String dir = "/vstorage";
        String file = "vstorage";

        Path dirPath = Path.of(dir);
        Path filePath = dirPath.resolve(file);

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        double uptimeSeconds = getUptimeSeconds();
        String uptimeHours = String.format("%.2f", uptimeSeconds / 3600.0);
        long freeMB = getFreeDiskSpaceMB("/");

        String logLine = String.format("Timestamp2: uptime %s hours, free disk in root: %d MBytes%n", uptimeHours, freeMB);

        Files.writeString(filePath, logLine, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      
        return logLine;
    }

    private static double getUptimeSeconds() {
        return (double) java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0;
    }

    private static long getFreeDiskSpaceMB(String path) {
        return new java.io.File(path).getFreeSpace() / (1024 * 1024);
    }
}