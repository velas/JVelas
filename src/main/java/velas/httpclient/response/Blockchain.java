package velas.httpclient.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Blockchain {

    @JsonProperty("height")
    public int height;

    @JsonProperty("current_hash")
    public String currentHash;

    @JsonProperty("current_epoch")
    public String currentEpoch;

}
