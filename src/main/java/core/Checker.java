package core;

import com.github.tomaslanger.Chalk;
import main.Runner;
import me.dilley.MineStat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Checker extends Thread {

    private final String ip;
    private MineStat ms;
    private String output;

    public String getIpAddress() {
        return this.ip;
    }

    public String getServerDetails() {
        return output;
    }

    public static List<MCServer> parseMCServerFile(String csvFilePath) {
        List<MCServer> servers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                MCServer server = parseMCServer(line);
                servers.add(server);
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return servers;
    }

    private static MCServer parseMCServer(String csvLine) {
        String[] serverInfo = csvLine.split(",");

        if (serverInfo.length < 4) {
            throw new IllegalArgumentException("Invalid CSV line: " + csvLine);
        }

        String ipAddress = serverInfo[0];
        String version = serverInfo[1];
        String serverType = serverInfo[2];
        int port = Integer.parseInt(serverInfo[3]);

        return new MCServer(ipAddress, version, serverType, port);
    }

    public Checker(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        this.ms = new MineStat(this.ip);
        if (Runner.settings1) {
            Runner.sa.addToCheckingTab(this.ip);
        }
        Runner.sa.updateCounter();
        output = "";
        if (ms.isServerUp()) {
            output += (this.ip + " is online running version " + ms.getVersion() + " with " + ms.getCurrentPlayers() + " out of " + ms.getMaximumPlayers() + " players." + "\n\n");
            output += ("Message of the day: " + ms.getMotd() + "\n\n");
            output += ("Latency: " + ms.getLatency() + "ms" + "\n\n");
            output += ("Gamemode: " + ms.getGameMode() + "\n\n");
            output += ("Connected using protocol: " + ms.getRequestType() + "\n\n");
            System.out.println(Chalk.on(ip + " is online running " + ms.getVersion() + ".").bgGreen());
            FileHelper.writeToFile(ip + ","
                    + ms.getVersion() + ","
                    + ms.getGameMode() + ","
                    + ms.getPort() + ","
                    + "\"" + ms.getMotd()
                    .replaceAll("\"\\{\"text\":\"", "")
                    .replaceAll(",", "")
                    .replaceAll("\"", "")
                    .replaceAll("}", "")
                    .replaceAll("\\{", "")
                    .replaceAll(",", "") + "\",");
            Runner.sa.addToLiveWithInfoTab(this.ip);
            Runner.map.put(this.ip, output);
        } else {
            output += (ip + " is offline!");
        }

        this.output = output;

        new DiscordWebhook("yourwebhook").sendMessage(output);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void addHistoricalData() {
        String fpath = "src/main/resources/alive.csv";

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(fpath));
            String line = reader.readLine();

            while (line != null) {
                Runner.sa.addToAliveTab(line.split(",")[0]);
                String s = "";
                for (int i = 0; i < line.split(",").length; i++) {
                    s += line.split(",")[i] + "\n";
                }
                Runner.map.put(line.split(",")[0], s);


                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static class MCServer {
        private String ipAddress;
        private String version;
        private String serverType;
        private int port;

        public MCServer(String ipAddress, String version, String serverType, int port) {
            this.ipAddress = ipAddress;
            this.version = version;
            this.serverType = serverType;
            this.port = port;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getVersion() {
            return version;
        }

        public String getServerType() {
            return serverType;
        }

        public int getPort() {
            return port;
        }

        @Override
        public String toString() {
            return "MCServer{" +
                    "ipAddress='" + ipAddress + '\'' +
                    ", version='" + version + '\'' +
                    ", serverType='" + serverType + '\'' +
                    ", port=" + port +
                    '}';
        }
    }

}