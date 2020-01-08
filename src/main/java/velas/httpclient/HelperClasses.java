package velas.httpclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
class TxPublishResponse {
    @JsonProperty("result")
    String result;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class TxRequest {
    @JsonProperty("hashes")
    String[] hashes;
    public TxRequest(String[] hashes) { this.hashes = hashes; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Balance {
    @JsonProperty("amount")
    public BigInteger amount;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ErrorResponse {

    @JsonProperty("status")
    String status;

    @JsonProperty("code")
    int code;

    @JsonProperty("error")
    String error;
}
