package velas.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionModel {

    @JsonProperty("hash")
    public String hash;

    @JsonProperty("version")
    public int version = 1;

    @JsonProperty("lock_time")
    public int lockTime = 0;

    @JsonProperty("tx_in")
    public List<TransactionInput> txIns;

    @JsonProperty("tx_out")
    public List<TransactionOut> txOuts;

    @JsonProperty("network_name")
    public String network;

    public TransactionModel() {
        super();
    }

    public TransactionModel(String hash, List<TransactionInput> txIns, List<TransactionOut> txOuts, String network) {
        this.hash = hash;
        this.txIns = txIns;
        this.txOuts = txOuts;
        this.network = network;
    }

    public byte[] msgForSign(String hash, int index) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] hashBuf = Hex.decodeHex(hash.toCharArray());
        buf.write(hashBuf);

        byte[] indexBuf = Helpers.intToLittleEndian(index);
        buf.write(indexBuf);

        byte[] version = Helpers.intToLittleEndian(this.version);
        buf.write(version);

        byte[] locktime = Helpers.intToLittleEndian(this.lockTime);
        buf.write(locktime);

        for(TransactionOut txOut: this.txOuts) {
            buf.write(txOut.toBytes());
        }

        return buf.toByteArray();
    }

    public byte[] generateHash() throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();

        byte[] version = Helpers.intToLittleEndian(this.version);
        buf.write(version);

        byte[] locktime = Helpers.intToLittleEndian(this.lockTime);
        buf.write(locktime);

        for(TransactionInput txIn: this.txIns) {
            buf.write(txIn.toBytes());
        }
        for(TransactionOut txOut: this.txOuts) {
            buf.write(txOut.toBytes());
        }

        return Helpers.DHASH(buf.toByteArray());
    }

}
