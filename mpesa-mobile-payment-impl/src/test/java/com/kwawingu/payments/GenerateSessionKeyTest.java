/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateSessionKeyTest {
  private static final Logger LOG = LoggerFactory.getLogger(GenerateSessionKeyTest.class);

  private GenerateSessionKey mpesaGenerateSessionKey;
  private EncryptApiKey encryptApiKey;
  private ApiEndpoint apiEndpoint;

  @BeforeEach
  public void setUp() {
    String publicKey = System.getenv("MPESA_PUBLIC_KEY");
    String apiKey = System.getenv("MPESA_API_KEY");

    if (publicKey == null || apiKey == null) {
      throw new RuntimeException(
          "Missing environment variables: MPESA_PUBLIC_KEY or MPESA_API_KEY");
    }

    mpesaGenerateSessionKey = new GenerateSessionKey();
    encryptApiKey = new EncryptApiKey(publicKey, apiKey);
    apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
  }

  @Test
  public void testClientGetSessionKey() {
    String context = apiEndpoint.getUrl(Service.GET_SESSION);
    LOG.info(context);
    String encryptedApiKey = encryptApiKey.generateAnEncryptApiKey();
    assertNotNull(encryptedApiKey);
    Optional<String> session = mpesaGenerateSessionKey.getSessionKey(encryptedApiKey, context);
    assertTrue(session.isPresent());
  }

  @Test
  public void testGetSessionKey_SuccessfulResponse() {}

  @Test
  public void testGetSessionKey_ErrorResponse() {}

  @Test
  public void testGetSessionKey_Exception() {}
}