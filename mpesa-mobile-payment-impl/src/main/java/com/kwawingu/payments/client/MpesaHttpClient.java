/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kwawingu.payments.client.response.CustomerToBusinessTransactionResponse;
import com.kwawingu.payments.client.response.GetSessionResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MpesaHttpClient {
  @SuppressWarnings("UnusedVariable")
  private static final Logger LOG = LoggerFactory.getLogger(MpesaHttpClient.class);

  private final HttpClient httpClient;

  public MpesaHttpClient() {
    this.httpClient = HttpClient.newHttpClient();
  }

  public JsonObject stringToJson(String response) {
    try {
      JsonElement jsonElement = JsonParser.parseString(response);
      if (jsonElement.isJsonObject()) return jsonElement.getAsJsonObject();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
    return new JsonObject();
  }

  public GetSessionResponse getSessionRequest(Map<String, String> headers, URI uri)
      throws IOException, InterruptedException {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri).GET();
    headers.forEach(requestBuilder::headers);
    HttpRequest request = requestBuilder.build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return new GetSessionResponse(response.statusCode(), stringToJson(response.body()));
  }

  public CustomerToBusinessTransactionResponse.SynchronousResponses
      customerToBusinessTransactionRequest(
          Map<String, String> headers, HttpRequest.BodyPublisher httpBody, URI uri)
          throws IOException, InterruptedException {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri).POST(httpBody);
    headers.forEach(requestBuilder::headers);
    HttpRequest request = requestBuilder.build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return new CustomerToBusinessTransactionResponse.SynchronousResponses(
        response.statusCode(), stringToJson(response.body()));
  }

  public HttpResponse<String> QueryTransactionStatus(Map<String, String> headers, HttpRequest.BodyPublisher httpBody, URI uri) throws IOException, InterruptedException {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri).GET();
    headers.forEach(requestBuilder::headers);
    HttpRequest request = requestBuilder.build();
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }

}
