package com.compulynx.compas.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import com.compulynx.compas.models.Token;
import com.compulynx.compas.models.TokenResponse;
import com.compulynx.compas.repositories.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Service
public class Auth2T24TokenService {
	
	private static final Logger logger = LoggerFactory.getLogger(Auth2T24TokenService.class);
	
	
	 private  TokenRepository tokenRepository;
	
	
	   @Autowired
	    public Auth2T24TokenService(TokenRepository tokenRepository) {
	        this.tokenRepository = tokenRepository;
	        logger.debug("TokenRepository injected: {}", tokenRepository != null);//if not null return true else false
	    }
	
    
    public String getAccessToken(Environment env) throws IOException {
    	
    	logger.debug("Starting getAccessToken");
        if (tokenRepository == null) {
            logger.error("TokenRepository is null");
           throw new IllegalStateException("TokenRepository is not initialized");
        }
        int responseCode = 0;
        StringBuilder response = new StringBuilder();
        TokenResponse tokenResponse = null;

        URL url = new URL(env.getProperty("tokenEndpoint"));
        URLConnection connection = url.openConnection();

        try {
            if (connection instanceof HttpURLConnection || connection instanceof HttpsURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String basicAuth = "Basic " + Base64.getEncoder().encodeToString(
                        (env.getProperty("clientId") + ":" + env.getProperty("clientSecret")).getBytes());
                httpURLConnection.setRequestProperty("Authorization", basicAuth);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setConnectTimeout(20000);

                try (OutputStream os = httpURLConnection.getOutputStream()) {
                    os.write("grant_type=client_credentials".getBytes());
                    os.flush();
                }

                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 200) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    tokenResponse = mapper.readValue(response.toString(), TokenResponse.class);
                    logger.info("Expires in {}", LocalDateTime.now().plusSeconds(tokenResponse.getExpires_in()));

                    if (tokenResponse.getAccess_token() == null || tokenResponse.getExpires_in() == 0) {
                        throw new IOException("Invalid token response: missing access token or expiration time");
                    }

                    Token token = new Token();
                    token.setAccessToken(tokenResponse.getAccess_token());
                    token.setCreatedAt(LocalDateTime.now());
                    token.setExpiresIn(tokenResponse.getExpires_in());

                    // Save the token to the database
                    try {
                        logger.info("Saving token: {}", new Gson().toJson(token));
                        tokenRepository.save(token);
                        logger.info("Token saved successfully");
                    } catch (Exception e) {
                        logger.error("An exception occurred when saving the token", e);
                        e.printStackTrace();
                    }
                    logger.info(tokenResponse.getAccess_token());
                    return tokenResponse.getAccess_token();
                } else {
                    throw new IOException("Failed to retrieve access token. HTTP response code: " + responseCode);
                }
            } else {
                throw new IOException("Unsupported connection type: " + connection.getClass().getName());
            }
        } finally {
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).disconnect();
            } else if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).disconnect();
            }
        }
    }
    
    private Long generateTokenId() {
        return System.currentTimeMillis();
    }

}
