package com.kwawingu.payments.query;


import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.CustomerToBusinessTransactionTest;
import com.kwawingu.payments.Environment;
import com.kwawingu.payments.Market;
import com.kwawingu.payments.exception.SessionKeyUnavailableException;
import com.kwawingu.payments.session.MpesaSession;
import com.kwawingu.payments.session.keys.MpesaEncryptedSessionKey;
import com.kwawingu.payments.session.keys.MpesaSessionKey;
import com.kwawingu.payments.session.provider.MpesaKeyProviderFromEnvironment;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QueryTransactionStatusTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransactionTest.class);

    @Test
    public void queryTransactionStatus() throws SessionKeyUnavailableException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        // Set-Up
        final MpesaKeyProviderFromEnvironment.Config config =
                new MpesaKeyProviderFromEnvironment.Config.Builder()
                        .setApiKeyEnvName("MPESA_API_KEY")
                        .setPublicKeyEnvName("MPESA_PUBLIC_KEY")
                        .build();
        MpesaSession mpesaSession = new MpesaSession(new MpesaKeyProviderFromEnvironment(config), Environment.SANDBOX, Market.VODACOM_TANZANIA);
        ApiEndpoint endpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
        QueryTransactionStatus query = new QueryTransactionStatus(endpoint, mpesaSession.getEncryptedSessionKey());

        String response = query.queryTransaction();
        LOG.info(response);

        assertNotNull(response);
    }
}
