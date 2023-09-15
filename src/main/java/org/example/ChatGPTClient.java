package org.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPTClient {
    private static final String API_KEY = "sk-API_KEY";
    private static final String URL = "https://api.openai.com/v1/engines/text-davinci-002/completions";

    public static String sendRequest(String inputText) {
        StringBuilder response = new StringBuilder();

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(URL).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + API_KEY);

            String postData = "{\"prompt\":\"" + inputText + "\"}";

            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {

                wr.writeBytes(postData);
                wr.flush();

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while sending request: " + e.getMessage());
            e.printStackTrace();
        }
        return response.toString();
    }
}
