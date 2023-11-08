package com.kwawingu.payments.session.keys;

public class MpesaApiKey {
    private final String apiKey;

    public MpesaApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return System.getenv(apiKey);
    }
}