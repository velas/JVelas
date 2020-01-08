package velas.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.binary.Hex;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionInputOutpoint {

    @JsonProperty("hash")
    public String hash;

    @JsonProperty("index")
    public int index;

    @JsonProperty("value")
    public BigInteger value;

    public TransactionInputOutpoint() {
        super();
    }

    public TransactionInputOutpoint(int index, BigInteger value, String hash) {
        this.index = index;
        this.value = value;
        this.hash = hash;
    }

    public byte[] toBytes() throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] hash = Hex.decodeHex(this.hash.toCharArray());
        buf.write(hash);

        byte[] index = Helpers.intToLittleEndian(this.index);
        buf.write(index);

        byte[] val = Helpers.biToLittleEndian(this.value);
        buf.write(val);

        return buf.toByteArray();
    }

}
