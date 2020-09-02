package ba.unsa.etf.http;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static ba.unsa.etf.presenters.LoginPresenter.TOKEN;

public class HttpUtils {
    private HttpUtils() {}

    public static HttpResponse GET(String path, Boolean authorization) throws IOException {
      //  URL url = new URL("http://localhost:8080/" + path);
        URL url = new URL("https://parking-lot-server.herokuapp.com/" + path);
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
        while ((output = br.readLine()) != null) {
            response += output;
        }

        if(response.charAt(0) == '{') {
            response = "[" + response + "]";
        }
        connection.disconnect();
        JsonReader jsonReader = Json.createReader(new StringReader(response));
      //  JsonObject jsonObject = jsonReader.readObject();
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();
        System.out.println(jsonArray);
        return new HttpResponse(statusCode, jsonArray);
    }

    public static HttpResponse POST(String path, String body, Boolean authorization) throws IOException {
      //  URL url = new URL("http://localhost:8080/" + path);
        URL url = new URL("https://parking-lot-server.herokuapp.com/" + path);
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
        int statusCode = connection.getResponseCode();
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(statusCode == 200 || statusCode == 201 ? connection.getInputStream() : connection.getErrorStream(), "utf-8"))) {
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        String res = response.toString();
        if(res.charAt(0) == '{') {
            res = "[" + response.toString() + "]";
        }

        connection.disconnect();
        JsonReader jsonReader = Json.createReader(new StringReader(res));
        JsonArray jsonArray = jsonReader.readArray();
       // JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        return new HttpResponse(statusCode, jsonArray);
    }

    public static HttpResponse DELETE (String path, Boolean authorization) throws IOException {
        //URL url = new URL("http://localhost:8080/" + path);
        URL url = new URL("https://parking-lot-server.herokuapp.com/" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");
        if (authorization)
            connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        int statusCode = connection.getResponseCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (connection.getInputStream())));
        String output;
        String response = "";
        while ((output = br.readLine()) != null) {
            response += output;
        }

        if(response.charAt(0) == '{') {
            response = "[" + response + "]";
        }
        connection.disconnect();
        JsonReader jsonReader = Json.createReader(new StringReader(response));
        //  JsonObject jsonObject = jsonReader.readObject();
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();
        System.out.println(jsonArray);
        return new HttpResponse(statusCode, jsonArray);
    }

    public boolean isArray (String s) {
        return s.charAt(0) == '[';
    }
}
