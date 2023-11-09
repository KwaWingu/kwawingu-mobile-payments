/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kwawingu.payments.c2b.CustomerToBusinessTransaction;
import com.kwawingu.payments.c2b.Payload;
import com.kwawingu.payments.exception.SessionKeyUnavailableException;
import com.kwawingu.payments.session.MpesaKeyProviderFromEnvironment;
import com.kwawingu.payments.session.MpesaSession;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerToBusinessTransactionTest {
  private static final Logger LOG =
      LoggerFactory.getLogger(CustomerToBusinessTransactionTest.class);

  private final MpesaKeyProviderFromEnvironment.Config config =
      new MpesaKeyProviderFromEnvironment.Config.Builder()
          .setApiKeyEnvName("MPESA_API_KEY")
          .setPublicKeyEnvName("MPESA_PUBLIC_KEY")
          .build();

  private final MpesaSession session =
      new MpesaSession(
          new MpesaKeyProviderFromEnvironment(config),
          Environment.SANDBOX,
          Market.VODACOM_TANZANIA);

  private void printSanitizeResponse(String response) {
    for (String s : response.split(",")) {
      if (s.contains("output_ResponseDesc")) {
        LOG.info(s.trim());
      }
    }
    LOG.info(Arrays.stream(response.split(",")).toList().toString());
  }

  @Test
  public void testPayment_wheInitiated_responseSucceed()
      throws IOException,
          InterruptedException,
          SessionKeyUnavailableException,
          NoSuchPaddingException,
          IllegalBlockSizeException,
          NoSuchAlgorithmException,
          InvalidKeySpecException,
          BadPaddingException,
          InvalidKeyException {
    // Set-Up
    Payload payload =
        new Payload.Builder()
            .setAmount("10.00")
            .setCustomerMSISDN("+255762578467")
            .setCountry(Market.VODACOM_TANZANIA.getInputCountryValue())
            .setCurrency(Market.VODACOM_TANZANIA.getInputCurrencyValue())
            .setServiceProviderCode("000000")
            .setTransactionReference("T1234C")
            .setThirdPartyConversationID("asv02e5958774f783d0d689761")
            .setPurchasedItemsDesc("Handbag, Black shoes")
            .build();

    CustomerToBusinessTransaction customerToBusinessTransaction =
        new CustomerToBusinessTransaction.Builder()
            .setApiEndpoint(new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA))
            .setEncryptedSessionKey(session.getEncryptedSessionKey())
            .setPayload(payload)
            .build();

    // Test
    String response = customerToBusinessTransaction.synchronousPayment();
    printSanitizeResponse(response);

    // Assertion
    assertNotNull(response);
    assertFalse(response.isBlank());
  }
}
