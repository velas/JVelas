package velas.crypto;

import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Wallet {

    public final static byte[] VERSION = new byte[]{(byte)15, (byte)244};
    public final static int CHECK_SUM_LEN = 4;

    public String base58Address;
    public byte[] address;

    public Wallet(byte[] pubKey) throws IOException {
        ByteArrayOutputStream versionedPayload = new ByteArrayOutputStream();

        versionedPayload.write(Wallet.VERSION);
        byte[] pkHash = Wallet.hashPublicKey(pubKey);
        versionedPayload.write(pkHash);
        byte[] checksum = Wallet.checksum(versionedPayload.toByteArray());

        versionedPayload.write(checksum);

        this.address = versionedPayload.toByteArray();
        this.base58Address = Base58.encode(this.address);
    }

    public static byte[] hashPublicKey(byte[] pubKey) {
        return Utils.sha256hash160(pubKey);
    }

    public static byte[] checksum(byte[] payload) {
        byte[] data = Helpers.DHASH(payload);

        byte[] slice = new byte[CHECK_SUM_LEN];

        System.arraycopy(data, 0, slice, 0, slice.length);
        return slice;
    }

}
