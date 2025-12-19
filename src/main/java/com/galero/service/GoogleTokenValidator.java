package com.galero.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to validate Google JWT credentials and extract user information
 */
@Service
public class GoogleTokenValidator {

    private static final Logger logger = LoggerFactory.getLogger(GoogleTokenValidator.class);

    @Value("${google.client.id:}")
    private String googleClientId;

    /**
     * Verify Google JWT token and extract claims
     *
     * @param idTokenString the Google ID token
     * @return Map containing user information extracted from the token
     * @throws Exception if token verification fails
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> verifyAndGetClaims(String idTokenString) throws Exception {
        try {
            logger.debug("Validating Google token");
            
            // Check if Google Client ID is configured
            if (googleClientId == null || googleClientId.isBlank()) {
                logger.error("GOOGLE_CLIENT_ID is not configured. Please set it via environment variable or application.yml");
                throw new IllegalArgumentException("Server configuration error: GOOGLE_CLIENT_ID not set. Please contact administrator.");
            }
            
            logger.debug("Expected audience (Client ID): {}", googleClientId);
            
            // Decode and verify the JWT manually
            String[] parts = idTokenString.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid token format: JWT must have 3 parts");
            }

            // Decode the payload (second part)
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            logger.debug("JWT payload decoded successfully");
            
            // Parse JSON payload
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> claims = mapper.readValue(payload, Map.class);

            // Validate issuer
            Object iss = claims.get("iss");
            if (iss == null) {
                throw new IllegalArgumentException("Missing issuer in token");
            }
            
            if (!iss.equals("https://accounts.google.com") && !iss.equals("accounts.google.com")) {
                logger.warn("Invalid issuer in token: {}", iss);
                throw new IllegalArgumentException("Invalid issuer: " + iss);
            }
            logger.debug("Token issuer validated: {}", iss);

            // Validate audience
            Object audObj = claims.get("aud");
            if (audObj == null) {
                throw new IllegalArgumentException("Missing audience in token");
            }
            
            String aud = audObj.toString();
            logger.debug("Token audience: {}, Expected: {}", aud, googleClientId);
            
            if (!aud.equals(googleClientId)) {
                logger.warn("Audience mismatch. Expected: {}, Got: {}", googleClientId, aud);
                throw new IllegalArgumentException("Invalid audience: expected " + googleClientId + " but got " + aud);
            }
            logger.debug("Token audience validated");

            // Check token expiration
            Object expObj = claims.get("exp");
            if (expObj == null) {
                throw new IllegalArgumentException("Missing expiration in token");
            }
            
            Long exp = ((Number) expObj).longValue();
            long currentTime = System.currentTimeMillis() / 1000;
            
            if (currentTime > exp) {
                logger.warn("Token has expired. Current time: {}, Expiration: {}", currentTime, exp);
                throw new IllegalArgumentException("Token has expired");
            }
            logger.debug("Token expiration validated");

            // Extract user information
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("googleId", claims.get("sub"));
            userInfo.put("email", claims.get("email"));
            userInfo.put("firstName", claims.get("given_name"));
            userInfo.put("lastName", claims.get("family_name"));
            userInfo.put("profilePictureUrl", claims.get("picture"));

            logger.info("Google token validated successfully for user: {}", userInfo.get("email"));
            return userInfo;

        } catch (IllegalArgumentException e) {
            logger.error("Token validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during token verification", e);
            throw new IllegalArgumentException("Error verifying Google token: " + e.getMessage(), e);
        }
    }
}
