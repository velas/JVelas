import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import velas.crypto.*;
import velas.httpclient.VelasClient;

import java.math.BigInteger;
import java.util.Base64;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TransactionTest {

    String pk = "89d5bd2d31889df63cb1c895e4c6f16772e7b06a8c71228bb59d4c9a0c434fc1f6e586d5d051065a580969d15f48f88251ed24b9c77422410bc39a0e7247e53a";
    String testNetMasterNodeUrl = "https://testnet.velas.website";

    @Test
    public void TxTest() throws Exception {
        BigInteger amount = new BigInteger("1000");
        BigInteger commission = new BigInteger("1000000");
        String toAddress = "VLa1hi77ZXD2BSWDD9wQe8vAhejXyS7vBM4";

        VelasClient client = new VelasClient(testNetMasterNodeUrl);
        HD hd = new HD(pk);
        Wallet wallet = hd.toWallet();

        List<TransactionInputOutpoint> unspents = client.wallet.getUnspent(wallet.base58Address);

        Transaction tx = new Transaction(unspents, amount, hd, wallet.base58Address, toAddress, commission);

        boolean res = client.tx.validate(tx);
        System.out.println(res);
    }

    @Test
    public void Uint64ToLittleEndianTest() throws Exception {
        String expected = "45696e5c8f140000";

        BigInteger val = new BigInteger("22605963618629");
        byte[] buf = Helpers.biToLittleEndian(val);

        String msgHex = String.valueOf(Hex.encodeHex(buf));


        assertEquals("45696e5c8f140000 = 22605963618629", expected, msgHex);
    }

    @Test
    public void DHASHTest() throws DecoderException {
        String expected = "70e18036aac802dd18cebc211376f14b43ea5049fa59092e85644f4de7f413ae";
        String hash = "e6bc49dd68e0c89c617609f4f76c40643a7fd24b70a9c51f80b1831b5794f8f2";

        byte[] hashBuf = Hex.decodeHex(hash.toCharArray());

        byte[] res = Helpers.DHASH(hashBuf);
        String msgHex = String.valueOf(Hex.encodeHex(res));

        assertEquals("dhash", expected, msgHex);
    }

    @Test
    public void TransactionIntputOutpointTest() throws Exception {
        String expected = "e6bc49dd68e0c89c617609f4f76c40643a7fd24b70a9c51f80b1831b5794f8f20100000045696e5c8f140000";

        TransactionInputOutpoint prevOut = new TransactionInputOutpoint(1, new BigInteger("22605963618629"), "e6bc49dd68e0c89c617609f4f76c40643a7fd24b70a9c51f80b1831b5794f8f2");

        byte[] msg = prevOut.toBytes();

        String msgHex = String.valueOf(Hex.encodeHex(msg));

        assertEquals("prev out to bytes", expected, msgHex);
    }

    @Test
    public void TransactionOutputTest() throws Exception {
        String expected = "02000000fe2c3101000000000ff47f56798625d92956704ddd3657c7684e037a3d472b5cc992";

        TransactionOut txOut = new TransactionOut(2, new BigInteger("19999998"), "0ff47f56798625d92956704ddd3657c7684e037a3d472b5cc992");

        byte[] msg = txOut.toBytes();

        String msgHex = String.valueOf(Hex.encodeHex(msg));

        // assert statements
        assertEquals("tx out to bytes", expected, msgHex);
    }

    @Test
    public void TransactionInputTest() throws Exception {
        String expected = "e6bc49dd68e0c89c617609f4f76c40643a7fd24b70a9c51f80b1831b5794f8f20100000045696e5c8f14000001000000f485289202e11b67dea464409f5ce14ac0b10fc32b126fdd96454ec19b0b56fbe748c31da2a18fb42c7000173732516e76cb0722f7820c5333477f1fac094840415e4054e34f09683cdda0951a0abbad9012051ee6f756be048c9a28e7018205";

        TransactionInputOutpoint prevOut = new TransactionInputOutpoint(1,
                new BigInteger("22605963618629"),
                "e6bc49dd68e0c89c617609f4f76c40643a7fd24b70a9c51f80b1831b5794f8f2");

        String sigScript = "e748c31da2a18fb42c7000173732516e76cb0722f7820c5333477f1fac094840415e4054e34f09683cdda0951a0abbad9012051ee6f756be048c9a28e7018205";
        byte[] pk = Base64.getDecoder().decode("9IUokgLhG2fepGRAn1zhSsCxD8MrEm/dlkVOwZsLVvs=");

        TransactionInput txIn = new TransactionInput(sigScript, pk, prevOut);

        byte[] msg = txIn.toBytes();

        String msgHex = String.valueOf(Hex.encodeHex(msg));

        // assert statements
        assertEquals("tx in to bytes", expected, msgHex);
    }

}
