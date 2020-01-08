package velas.httpclient.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeInfo {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String Name;

    @JsonProperty("addr")
    public String Addr;

}
