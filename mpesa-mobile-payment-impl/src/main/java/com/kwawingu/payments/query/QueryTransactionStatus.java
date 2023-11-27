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
import java.util.Objects;

public class QueryTransactionStatus {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransaction.class);

    private final MpesaHttpClient mpesaHttpClientClient;
    private final ApiEndpoint apiEndpoint;
    private final MpesaEncryptedSessionKey encryptedSessionKey;
    private final String inputQueryReference;
    private final String inputCountry;
    private final String inputServiceProviderCode;
    private final String inputThirdPartyConversationID;

    private QueryTransactionStatus(Builder builder) {
        this.mpesaHttpClientClient = new MpesaHttpClient();
        this.apiEndpoint = builder.apiEndpoint;
        this.encryptedSessionKey = builder.encryptedSessionKey;
        this.inputQueryReference = builder.inputQueryReference;
        this.inputCountry = builder.inputCountry;
        this.inputServiceProviderCode = builder.inputServiceProviderCode;
        this.inputThirdPartyConversationID = builder.inputThirdPartyConversationID;
    }

    public static class Builder {
        private ApiEndpoint apiEndpoint;
        private MpesaEncryptedSessionKey encryptedSessionKey;
        private String inputQueryReference;
        private String inputCountry;
        private String inputServiceProviderCode;
        private String inputThirdPartyConversationID;

        public Builder setApiEndpoint(ApiEndpoint apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public Builder setEncryptedSessionKey(MpesaEncryptedSessionKey encryptedSessionKey) {
            this.encryptedSessionKey = encryptedSessionKey;
            return this;
        }

        public Builder setInputQueryReference(String inputQueryReference) {
            this.inputQueryReference = inputQueryReference;
            return this;
        }

        public Builder setInputCountry(String inputCountry) {
            this.inputCountry = inputCountry;
            return this;
        }

        public Builder setInputServiceProviderCode(String inputServiceProviderCode) {
            this.inputServiceProviderCode = inputServiceProviderCode;
            return this;
        }

        public Builder setInputThirdPartyConversationID(String inputThirdPartyConversationID) {
            this.inputThirdPartyConversationID = inputThirdPartyConversationID;
            return this;
        }

        public QueryTransactionStatus build() {
            Objects.requireNonNull(apiEndpoint, "API End-Point cannot be null");
            Objects.requireNonNull(encryptedSessionKey, "Encrypted Session Key cannot be null");
            Objects.requireNonNull(inputQueryReference, "Input Query Reference cannot be null");
            Objects.requireNonNull(inputCountry, "Input Country cannot be null");
            Objects.requireNonNull(inputServiceProviderCode, "Input Service Provider Code cannot be null");
            Objects.requireNonNull(inputThirdPartyConversationID, "Input Third Party Conversation ID cannot be null");
            return new QueryTransactionStatus(this);
        }
    }

    private URI params() {
        URI uri = apiEndpoint.getUrl(Service.QUERY_TRANSACTION);
        LOG.debug("Init URL: {}", uri);
        URI urlWithParams = null;
        try {
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

        URI uri = params();

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
