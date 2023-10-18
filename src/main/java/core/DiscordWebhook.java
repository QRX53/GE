package core;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordWebhook {

    private final String webhookUrl;

    public DiscordWebhook(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void sendMessage(String message) {

        if (message.contains("offline")) {
            return;
        }

        String json = String.format("{\"embeds\":[{\"description\":\"%s\",\"color\":65280}]}", message);
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
            conn.getResponseCode();
        } catch (Exception e) {
            if (e instanceof SocketException) {
                return;
            } else {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }
}