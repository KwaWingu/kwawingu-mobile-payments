/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.client.payload;

import com.kwawingu.payments.c2b.CustomerToBusinessTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Payload {
  private static final Logger LOG = LoggerFactory.getLogger(Payload.class);

  private final String amount;
  private final String customerMSISDN;
  private final String country;
  private final String currency;
  private final String serviceProviderCode;
  private final String transactionReference;
  private final String thirdPartyConversationID;
  private final String purchasedItemsDesc;

  private Payload(Builder builder) {
    this.amount = builder.amount;
    this.customerMSISDN = builder.customerMSISDN;
    this.country = builder.country;
    this.currency = builder.currency;
    this.serviceProviderCode = builder.serviceProviderCode;
    this.transactionReference = builder.transactionReference;
    this.thirdPartyConversationID = builder.thirdPartyConversationID;
    this.purchasedItemsDesc = builder.purchasedItemsDesc;
  }

  @SuppressWarnings("initialization.field.uninitialized")
  public static class Builder {
    private String amount;
    private String customerMSISDN;
    private String country;
    private String currency;
    private String serviceProviderCode;
    private String transactionReference;
    private String thirdPartyConversationID;
    private String purchasedItemsDesc;

    public Builder setAmount(String amount) {
      this.amount = amount;
      return this;
    }

    public Builder setCustomerMSISDN(String customerMSISDN) {
      this.customerMSISDN = customerMSISDN;
      return this;
    }

    public Builder setCountry(String country) {
      this.country = country;
      return this;
    }

    public Builder setCurrency(String currency) {
      this.currency = currency;
      return this;
    }

    public Builder setServiceProviderCode(String serviceProviderCode) {
      this.serviceProviderCode = serviceProviderCode;
      return this;
    }

    public Builder setTransactionReference(String transactionReference) {
      this.transactionReference = transactionReference;
      return this;
    }

    public Builder setThirdPartyConversationID(String thirdPartyConversationID) {
      this.thirdPartyConversationID = thirdPartyConversationID;
      return this;
    }

    public Builder setPurchasedItemsDesc(String purchasedItemsDesc) {
      this.purchasedItemsDesc = purchasedItemsDesc;
      return this;
    }

    public Payload build() {
      Objects.requireNonNull(amount, "Transaction amount cannot be null");
      Objects.requireNonNull(customerMSISDN, "Customer MSISDN cannot be null");
      Objects.requireNonNull(country, "country cannot be null");
      Objects.requireNonNull(currency, "currency cannot be null");
      Objects.requireNonNull(serviceProviderCode, "Service Provider Code cannot be null");
      Objects.requireNonNull(transactionReference, "Transaction Reference cannot be null");
      Objects.requireNonNull(
          thirdPartyConversationID, "Third Party Conversation ID cannot be null");
      Objects.requireNonNull(purchasedItemsDesc, "Purchased Item Description cannot be null");
      return new Payload(this);
    }
  }

  // Convert the Payload object to JSON String
  public String toJsonString() {
    return "{"
        + "\"input_Amount\": \""
        + amount
        + "\","
        + "\"input_CustomerMSISDN\": \""
        + customerMSISDN
        + "\","
        + "\"input_Country\": \""
        + country
        + "\","
        + "\"input_Currency\": \""
        + currency
        + "\","
        + "\"input_ServiceProviderCode\": \""
        + serviceProviderCode
        + "\","
        + "\"input_TransactionReference\": \""
        + transactionReference
        + "\","
        + "\"input_ThirdPartyConversationID\": \""
        + thirdPartyConversationID
        + "\","
        + "\"input_PurchasedItemsDesc\": \""
        + purchasedItemsDesc
        + "\""
        + "}";
  }
}
