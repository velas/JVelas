package velas.crypto;

import java.util.Arrays;

public class MasterKey {

    public byte[] key;
    public byte[] chainCode;

    public MasterKey(byte[] data) {
        this.key = Arrays.copyOfRange(data, 0, 32);
        this.chainCode = Arrays.copyOfRange(data, 32, 64);
    }

    public MasterKey(byte[] key, byte[] chainCode) {
        this.key = key;
        this.chainCode = chainCode;
    }

}
