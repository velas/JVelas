import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import velas.crypto.HD;
import velas.crypto.MasterKey;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class HDTest {

    @Test
    public void CKDPrivTest() throws DecoderException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        String expectedKey = "9fbab53f6f8c9541aab9cf285f1cd14625d1f39867a98889ea89fa7a04af69c0";
        String expectedChainCode = "9bd5981d21f49e320090a3073c9c5a775804b8f4b9c63810618863c57246cafa";

        String keyHex = "4766a5dc09364e7f840d291a8883a75dfab09a5c2db5332ffbed56a7ffed5c97";
        byte[] key = Hex.decodeHex(keyHex.toCharArray());
        String ccHex = "4a923af8ffeaaf3b244b13599f902397cc815c48608c421557e00dd207f888e8";
        byte[] cc = Hex.decodeHex(ccHex.toCharArray());

        MasterKey mk = HD.ckdPriv(key, cc, HD.HardenedOffset);
        assertEquals("key", expectedKey, String.valueOf(Hex.encodeHex(mk.key)));
        assertEquals("chain code", expectedChainCode, String.valueOf(Hex.encodeHex(mk.chainCode)));
    }

    @Test
    public void MasterKeyFromSeedTest() throws DecoderException, InvalidKeyException, NoSuchAlgorithmException {
        String expectedKey = "a09a8bd704401bf6fb1440266bdf83ccb1e67cf5d3fee89034330b42cd824d83";
        String expectedChainCode = "eb22176f34ac994f5cbb637e73bf3417831cbaaabb7eee870b3215585fdc9e7b";

        String seedHex = "76c3ee8e5b078b89970d01d8818dd898db34af9758ec780e7f4fffb00e09581c06b0dbe51bc0f430df0cf59cb1de60b9ae8223da8278fcbd3dc514142e425993";
        byte[] seed = Hex.decodeHex(seedHex.toCharArray());

        MasterKey mk = HD.getMasterKeyFromSeed(seed);

        String key = String.valueOf(Hex.encodeHex(mk.key));
        String chainCode = String.valueOf(Hex.encodeHex(mk.chainCode));

        assertEquals("key", expectedKey, key);
        assertEquals("chain code", expectedChainCode, chainCode);
    }

    @Test
    public void DerivePathTest() throws Exception {
        String expectedKey = "4766a5dc09364e7f840d291a8883a75dfab09a5c2db5332ffbed56a7ffed5c97";
        String expectedChainCode = "4a923af8ffeaaf3b244b13599f902397cc815c48608c421557e00dd207f888e8";

        String seedHex = "76c3ee8e5b078b89970d01d8818dd898db34af9758ec780e7f4fffb00e09581c06b0dbe51bc0f430df0cf59cb1de60b9ae8223da8278fcbd3dc514142e425993";
        byte[] seed = Hex.decodeHex(seedHex.toCharArray());

        MasterKey mk = HD.derivePath(HD.DefaultDerivePath, seed);

        String key = String.valueOf(Hex.encodeHex(mk.key));
        String chainCode = String.valueOf(Hex.encodeHex(mk.chainCode));

        assertEquals("key", expectedKey, key);
        assertEquals("chain code", expectedChainCode, chainCode);
    }

}
