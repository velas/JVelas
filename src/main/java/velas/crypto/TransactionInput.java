package velas.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionInput {

    @JsonProperty("signature_script")
    public String sigScript;

    @JsonProperty("public_key")
    public byte[] publicKey;

    @JsonProperty("previous_output")
    public TransactionInputOutpoint previousOutput;

    @JsonProperty("sequence")
    public int sequence = 1;

    public TransactionInput(String sigScript, byte[] publicKey, TransactionInputOutpoint prevOut) {
        this.sigScript = sigScript;
        this.publicKey = publicKey;
        this.previousOutput = prevOut;
    }

    public TransactionInput() {
        super();
    }

    public byte[] toBytes() throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();

        byte[] po = previousOutput.toBytes();
        buf.write(po);

        byte[] seq = Helpers.intToLittleEndian(sequence);
        buf.write(seq);

        buf.write(publicKey);

        byte[] ss = Hex.decodeHex(sigScript.toCharArray());
        buf.write(ss);

        return buf.toByteArray();
    }

}
