package velas.httpclient.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Progress {

    @JsonProperty("starting_block")
    public int startingBlock;

    @JsonProperty("current_block")
    public int currentBlock;

    @JsonProperty("highest_block")
    public int highestBlock;

    @JsonProperty("pulled_states")
    public int pulledStates;

    @JsonProperty("known_states")
    public int knownStates;

}
