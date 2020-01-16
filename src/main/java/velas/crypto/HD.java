package velas.crypto;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HD {

    public final static String Ed25519Curve = "Velas seed";
    public final static int HardenedOffset = 0x80000000;
    public final static String DefaultDerivePath = "m/0'";
    private static final String HMAC_SHA512 = "HmacSHA512";

    private CryptoKeys keyPair;

    public HD() throws NoSuchAlgorithmException {
        this.keyPair = Crypto.generateKeys();
    }

    // HD from private key
    public HD(String sk) throws DecoderException {
        byte[] skBuf = Hex.decodeHex(sk.toCharArray());
        this.keyPair = Crypto.generateFromSk(skBuf);
    }

    // HD from seed
    public HD(byte[] seed, String pathArgs) throws Exception {
        String path = pathArgs;
        if (path == null) {
            path = HD.DefaultDerivePath;
        }
        MasterKey mk = HD.derivePath(path, seed);

        System.out.println("=====================");
        System.out.println(Hex.encodeHex(mk.key));
        this.keyPair = Crypto.generateFromSeed(mk.key);
    }

    public byte[] publicKey() {
        return this.keyPair.publicKey;
    }

    public byte[] secretKey() {
        return this.keyPair.secretKey;
    }

    public CryptoKeys cryptoKeys() {
        return this.keyPair;
    }

    public Wallet toWallet() throws IOException {
        return new Wallet(this.keyPair.publicKey);
    }

    public static MasterKey derivePath(String path, byte[] seed) throws Exception {
        if (!isValidPath(path)) {
            throw new Exception("Invalid derivation path: " + path);
        }
        MasterKey mk = HD.getMasterKeyFromSeed(seed);
        byte[] key = mk.key;
        byte[] chainCode = mk.chainCode;

        long[] segments = HD.pathToSegments(path);
        for (long seg: segments) {
            mk = HD.ckdPriv(key, chainCode, seg + HD.HardenedOffset);
            key = mk.key;
            chainCode = mk.chainCode;
        }
        return new MasterKey(key, chainCode);
    }

    public static MasterKey ckdPriv(byte[] parentKey, byte[] parentChainCode, long index) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        buf.write((byte)0);
        buf.write(parentKey);

        byte[] indexBytes = Helpers.intToBigEndian(index);
        buf.write(indexBytes);

        String bufHex = String.valueOf(Hex.encodeHex(buf.toByteArray()));

        Mac hmacSHA512 = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec secretKeySpec = new SecretKeySpec(parentChainCode, HMAC_SHA512);
        hmacSHA512.init(secretKeySpec);
        byte[] digest = hmacSHA512.doFinal(buf.toByteArray());

        return new MasterKey(digest);
    }

    public static MasterKey getMasterKeyFromSeed(byte[] seed) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSHA512 = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec secretKeySpec = new SecretKeySpec(Ed25519Curve.getBytes(), HMAC_SHA512);
        hmacSHA512.init(secretKeySpec);
        byte[] digest = hmacSHA512.doFinal(seed);

        return new MasterKey(digest);
    }

    public static boolean isValidPath(String path) {
        Pattern re = Pattern.compile("^m(/\\d')+$");
        Matcher m = re.matcher(path);
        return m.matches();
    }

    public static long[] pathToSegments(String path) {
        String[] arr = HD.pathToStringArray(path);
        long[] segments = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            long seg = Integer.parseInt(arr[i]);
            segments[i] = seg;
        }
        return segments;
    }

    static String[] pathToStringArray(String path) {
        String[] splitted = path.split("/");
        splitted = Arrays.copyOfRange(splitted, 1, splitted.length);

        for (int i = 0; i < splitted.length; i++) {
            splitted[i] = splitted[i].replace("'", "");
        }
        return splitted;
    }

}
