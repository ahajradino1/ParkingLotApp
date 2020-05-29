package ba.unsa.etf.http;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static ba.unsa.etf.views.LoginPresenter.TOKEN;

public class HttpUtils {
    private HttpUtils() {}

    public static HttpResponse GET(String path, Boolean authorization) throws IOException {
            URL url = new URL("http://localhost:8080/" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            if (authorization)
                connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

            int statusCode = connection.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));
            String output;
            String response = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                response += output;
            }
            System.out.println(response);
            connection.disconnect();
            JsonReader jsonReader = Json.createReader(new StringReader(response));
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();
            return new HttpResponse(statusCode, jsonObject);
    }

    public static HttpResponse POST(String path, String body, Boolean authorization) throws IOException {
        URL url = new URL("http://localhost:8080/" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        if (authorization)
            connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        StringBuilder response = new StringBuilder();
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        int statusCode = connection.getResponseCode();
        connection.disconnect();
        JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        return new HttpResponse(statusCode, jsonObject);

    }
}
