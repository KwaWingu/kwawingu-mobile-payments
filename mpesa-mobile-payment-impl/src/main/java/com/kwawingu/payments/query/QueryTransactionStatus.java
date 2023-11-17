package com.kwawingu.payments.query;

import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.Service;
import com.kwawingu.payments.c2b.CustomerToBusinessTransaction;
import com.kwawingu.payments.client.MpesaHttpClient;
import com.kwawingu.payments.session.keys.MpesaEncryptedSessionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class QueryTransactionStatus {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransaction.class);

    private final MpesaHttpClient mpesaHttpClientClient;
    private final ApiEndpoint apiEndpoint;
    private final MpesaEncryptedSessionKey encryptedSessionKey;

    public QueryTransactionStatus(ApiEndpoint apiEndpoint, MpesaEncryptedSessionKey encryptedSessionKey) {
        this.apiEndpoint = apiEndpoint;
        this.encryptedSessionKey = encryptedSessionKey;
        this.mpesaHttpClientClient = new MpesaHttpClient();
    }

    public String queryTransaction() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        encryptedSessionKey.insertAuthorizationHeader(headers);
        headers.put("Origin", "*");

        URI uri = apiEndpoint.getUrl(Service.QUERY_TRANSACTION);
        HttpResponse<String> response = null;
        LOG.debug(String.valueOf(uri));

        try {
            response = mpesaHttpClientClient.queryTransactionStatusRequest(headers, uri);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body();
    }
}
