package net.saidora.api.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhook {

    private final String url;

    private String content = " ";
    private String embed_title = " ", embed_description = " ";
    private String embed_color;

    public DiscordWebhook(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmbed_title() {
        return embed_title;
    }

    public void setEmbed_title(String embed_title) {
        this.embed_title = embed_title;
    }

    public String getEmbed_description() {
        return embed_description;
    }

    public void setEmbed_description(String embed_description) {
        this.embed_description = embed_description;
    }

    public String getEmbed_color() {
        return embed_color;
    }

    public void setEmbed_color(String hex) {
        this.embed_color = hex;
    }

    public void send() {

        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            JsonObject jsonObject = new JsonObject();
            if(content != null) jsonObject.addProperty("content", content);
            if(embed_title != null){
                JsonArray jsonElements = new JsonArray();
                JsonObject object = new JsonObject();
                object.addProperty("title", embed_title);
                object.addProperty("description", embed_description);
                object.addProperty("color", Integer.parseInt(embed_color, 16));
                jsonElements.add(object);
                jsonObject.add("embeds", jsonElements);
            }
            osw.write(jsonObject.toString());
            osw.flush();
            osw.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if(conn.getResponseCode() != 204){
                throw new RuntimeException("Something went wrong. We can't send embed into discord channel.\n" + responseCode + " # " + jsonObject.toString());
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

