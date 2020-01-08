package velas.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionOut {

    @JsonProperty("pk_script")
    public String publicKeyScript;

    @JsonProperty("node_id")
    public String nodeId = "0000000000000000000000000000000000000000000000000000000000000000";

    @JsonProperty("payload")
    public Object payload;

    @JsonProperty("value")
    public BigInteger value;

    @JsonProperty("index")
    public int index;

    public TransactionOut() {
        super();
    }

    public TransactionOut(int index, BigInteger value, String publicScript) {
        this.index = index;
        this.publicKeyScript = publicScript;
        this.value = value;
    }

    public byte[] toBytes() throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] index = Helpers.intToLittleEndian(this.index);
        buf.write(index);

        byte[] val = Helpers.biToLittleEndian(this.value);
        buf.write(val);

        byte[] pk = Hex.decodeHex(this.publicKeyScript.toCharArray());
        buf.write(pk);

        if (!this.nodeId.isEmpty() && this.nodeId != "0000000000000000000000000000000000000000000000000000000000000000") {
            byte[] nodeId = Hex.decodeHex(this.publicKeyScript.toCharArray());
            buf.write(nodeId);
        }

        return buf.toByteArray();
    }

}
