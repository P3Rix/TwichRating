package com.peri.twitch.rating.ApiUtil;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApiUtils {

    static Logger logger = LoggerFactory.getLogger(ApiUtils.class);

    public static String generateToken(Environment env) {
        String resultToken = "";
        JsonObject json = null;
        try {
            URL url = new URL (ApiConstants.url);

            try {
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                String urlParameters  = "client_id=" + env.getProperty("client.id") +  "&client_secret=" +
                        env.getProperty("client.secret") +
                        "&grant_type=" + ApiConstants.typeCredentials;

                byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
                int    postDataLength = postData.length;
                con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                con.getOutputStream().write(postData);

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.fromJson(response.toString(), JsonElement.class);
                    json = jsonElement.getAsJsonObject();

                    logger.debug("token json: " + json);
                    con.disconnect();

                }
            } catch (IOException e) {
                logger.error("IOException trying to make the request: " + e);
            }
        } catch (MalformedURLException e) {
            logger.error("URL malformed: " + e);
        }
        return StringUtils.capitalize(json.get(ApiConstants.keyTokenType).getAsString()) + " " + json.get(ApiConstants.keyAccessToken).getAsString();
    }

    public static JsonObject getStreamer(String token, String username, Environment env) {
        URL url = null;
        JsonObject json = null;
        try {
            url = new URL(ApiConstants.userUrl + "=" + username);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            logger.debug("Username: " + username);

            con.setRequestProperty("Authorization", token);
            con.setRequestProperty("Client-Id", env.getProperty("client.id"));

            con.setDoOutput(true);
            con.setDoInput(true);

            int status = con.getResponseCode();

            logger.debug("Request status: " + status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(response.toString(), JsonElement.class);
            json = jsonElement.getAsJsonObject();

            logger.debug("Streamer json: " + json);
            in.close();

            con.disconnect();


        } catch (MalformedURLException e) {
            logger.error("URL malformed: " + e);
        } catch (ProtocolException e) {
            logger.error("Procol exception: " + e);
        } catch (IOException e) {
            logger.error("IOException: " + e);
        }

        return json;
    }

    public static JsonObject getStreamerVideo(String token, String id, Environment env) {
        URL url = null;
        JsonObject json = null;
        try {
            url = new URL(ApiConstants.videoUrl + "=" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            logger.debug("Streamer id: " + id);

            con.setRequestProperty("Authorization", token);
            con.setRequestProperty("Client-Id", env.getProperty("client.id"));

            con.setDoOutput(true);
            con.setDoInput(true);

            int status = con.getResponseCode();

            logger.debug("Request status: " + status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(response.toString(), JsonElement.class);
            json = jsonElement.getAsJsonObject();

            logger.info("Video json: " + json);
            in.close();

            con.disconnect();


        } catch (MalformedURLException e) {
            logger.error("URL malformed: " + e);
        } catch (ProtocolException e) {
            logger.error("Procol exception: " + e);
        } catch (IOException e) {
            logger.error("IOException: " + e);
        }

        return json;
    }

    private static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
