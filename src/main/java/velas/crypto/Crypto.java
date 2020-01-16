package velas.crypto;

import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;
import net.i2p.crypto.eddsa.spec.*;

import java.security.*;
import java.util.Arrays;


class CryptoKeys {

    public byte[] publicKey;
    public byte[] secretKey;

    public CryptoKeys() {

    }

    public CryptoKeys(byte[] secretKey, byte[] publicKey) {
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    public byte[] seed() {
        return Arrays.copyOfRange(this.secretKey, 0, 32);
    }

}

class Crypto {

    static final EdDSANamedCurveSpec ed25519 = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
    static final int SEED_LEN = 32;

    private static EdDSAPrivateKey secretFromKeys(CryptoKeys keys) {
        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(keys.seed(), ed25519);
        return new EdDSAPrivateKey(privKey);
    }

    public static CryptoKeys generateKeys() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] seed = new byte[SEED_LEN];
        sr.nextBytes(seed);

        return Crypto.generateFromSeed(seed);
    }

    public static CryptoKeys generateFromSeed(byte[] seed) {
        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(seed, ed25519);
        EdDSAPrivateKey sKey = new EdDSAPrivateKey(privKey);
        byte[] publicKey = sKey.getAbyte();
        byte[] secretKey = Helpers.concat(seed, publicKey);

        return new CryptoKeys(secretKey, publicKey);
    }

    public static CryptoKeys generateFromSk(byte[] secret) {
        byte[] publicKey = Arrays.copyOfRange(secret, 32, 64);
        return new CryptoKeys(secret, publicKey);
    }

    public static byte[] Sign(CryptoKeys keys, byte[] msg) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        Signature sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));

        EdDSAPrivateKey sKey = Crypto.secretFromKeys(keys);

        sgr.initSign(sKey);

        sgr.update(msg);
        return sgr.sign();
    }

}
