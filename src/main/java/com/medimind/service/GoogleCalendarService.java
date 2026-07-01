package com.medimind.service;

import com.medimind.entity.GoogleToken;
import com.medimind.entity.User;
import com.medimind.repository.GoogleTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService {

    private final GoogleTokenRepository googleTokenRepository;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    public void storeTokens(String code, User user) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri.replace("{baseUrl}", "http://localhost:8080"));
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> body = response.getBody();
            String accessToken = (String) body.get("access_token");
            String refreshToken = (String) body.get("refresh_token");
            Integer expiresIn = (Integer) body.get("expires_in");

            GoogleToken googleToken = googleTokenRepository.findByUser(user)
                    .orElse(GoogleToken.builder().user(user).build());

            googleToken.setAccessToken(accessToken);
            if (refreshToken != null) {
                googleToken.setRefreshToken(refreshToken);
            }
            googleToken.setExpiresAt(LocalDateTime.now().plusSeconds(expiresIn));

            googleTokenRepository.save(googleToken);
        }
    }

    public String getAccessToken(User user) {
        GoogleToken googleToken = googleTokenRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Google Calendar not linked"));

        if (googleToken.getExpiresAt().isBefore(LocalDateTime.now().plusMinutes(5))) {
            refreshAccessToken(googleToken);
        }

        return googleToken.getAccessToken();
    }

    private void refreshAccessToken(GoogleToken googleToken) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("refresh_token", googleToken.getRefreshToken());
        params.add("grant_type", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> body = response.getBody();
            String accessToken = (String) body.get("access_token");
            Integer expiresIn = (Integer) body.get("expires_in");

            googleToken.setAccessToken(accessToken);
            googleToken.setExpiresAt(LocalDateTime.now().plusSeconds(expiresIn));

            googleTokenRepository.save(googleToken);
        }
    }
}
