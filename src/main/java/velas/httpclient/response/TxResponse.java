package velas.httpclient.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import velas.crypto.Transaction;
import velas.crypto.TransactionInput;
import velas.crypto.TransactionOut;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TxResponse extends Transaction {

    @JsonProperty("size")
    int size;

    @JsonProperty("block")
    String block;

    @JsonProperty("confirmed")
    int confirmed;

    @JsonProperty("confirmed_timestamp")
    int confirmedTimestamp;

    @JsonProperty("total")
    int total;

    public TxResponse() {
        super();
    }

    public TxResponse(String hash, List<TransactionInput> txIns, List<TransactionOut> txOuts, String network) {
        super(hash, txIns, txOuts, network);
    }

}
