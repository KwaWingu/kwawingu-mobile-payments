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
import java.net.URISyntaxException;
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

    private URI params(String inputQueryReference, String inputCountry, String inputServiceProviderCode, String inputThirdPartyConversationID) {
        URI uri = apiEndpoint.getUrl(Service.QUERY_TRANSACTION);
        LOG.debug("Init URL: {}", String.valueOf(uri));
        URI urlWithParams = null;
        try {
            urlWithParams = new URI(uri.toString());
            urlWithParams = new URI(
                    uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    "queryReference=" + inputQueryReference +
                            "&country=" + inputCountry +
                            "&serviceProviderCode=" + inputServiceProviderCode +
                            "&thirdPartyConversationID=" + inputThirdPartyConversationID,
                    uri.getFragment()
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        LOG.debug("New URI with param: {}", urlWithParams);
        return urlWithParams;
    }

    public String queryTransaction() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        encryptedSessionKey.insertAuthorizationHeader(headers);
        headers.put("Origin", "*");

        URI uri = params("5C1400CVRO", "GHA", "ORG001", "1e9b774d1da34af78412a498cbc28f5e");

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
