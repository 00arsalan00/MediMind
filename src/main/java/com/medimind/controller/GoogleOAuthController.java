package com.medimind.controller;

import com.medimind.entity.User;
import com.medimind.service.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class GoogleOAuthController {

    private final GoogleCalendarService googleCalendarService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${app.oauth2.authorized-redirect-uri}")
    private String appRedirectUri;

    @GetMapping("/api/oauth/google/link")
    public String linkGoogle() {
        String scope = "https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/calendar.events email profile";
        
        return "redirect:" + UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri.replace("{baseUrl}", "http://localhost:8080"))
                .queryParam("response_type", "code")
                .queryParam("scope", scope)
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build().toUriString();
    }

    @GetMapping("/api/oauth/google/callback")
    public String callback(@RequestParam("code") String code, @AuthenticationPrincipal User user) {
        googleCalendarService.storeTokens(code, user);
        return "redirect:" + appRedirectUri;
    }
}
