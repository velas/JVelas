import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import velas.crypto.HD;
import velas.crypto.Wallet;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class WalletTest {

    @Test
    public void WalletFromSeedTest() throws Exception {
        String seed = "76c3ee8e5b078b89970d01d8818dd898db34af9758ec780e7f4fffb00e09581c06b0dbe51bc0f430df0cf59cb1de60b9ae8223da8278fcbd3dc514142e425993";
        String publicKey = "310a5225d0c32fbeace30447e12bf2f3d7c629bf563c7aa037fdd803c05371db";
        String secretKey = "4766a5dc09364e7f840d291a8883a75dfab09a5c2db5332ffbed56a7ffed5c97310a5225d0c32fbeace30447e12bf2f3d7c629bf563c7aa037fdd803c05371db";

        byte[] seedBuf = Hex.decodeHex(seed.toCharArray());

        HD hd = new HD(seedBuf, null);

        byte[] pk = hd.publicKey();
        byte[] sk = hd.secretKey();


        assertEquals("public key", publicKey, String.valueOf(Hex.encodeHex(pk)));
        assertEquals("secret key", secretKey, String.valueOf(Hex.encodeHex(sk)));
    }

    @Test
    public void WalletTest() throws DecoderException, IOException {
        String expected = "VLcN1dBy1VPc9bijr8rzeGbC78MCQ8DjwvS";

        String pk = "d4e3d9ee7c9f57dc35db5dd2360f6ee8b5085a9a14878e234b38f154e18b449bd352b77fd8c136ecb42a0909f9496bbaf3229ba243190488bd1fbf9fa62a7ec5";

        HD hdKeys = new HD(pk);
        Wallet wallet = hdKeys.toWallet();

        assertEquals("wallet hd", expected, wallet.base58Address);
    }

}
