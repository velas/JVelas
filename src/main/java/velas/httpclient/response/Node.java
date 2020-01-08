package velas.httpclient.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Node {

    @JsonProperty("p2p_info")
    public NodeInfo p2pInfo;

    @JsonProperty("p2p_peers")
    public List<NodeInfo> p2pPeers;

    @JsonProperty("blockchain")
    public Blockchain blockchain;

    @JsonProperty("is_sync")
    public boolean isSync;

    @JsonProperty("progress")
    public Progress progress;

}
