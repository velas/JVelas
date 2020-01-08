import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import velas.crypto.HD;
import velas.crypto.TransactionInputOutpoint;
import velas.crypto.Wallet;
import velas.httpclient.VelasClient;
import velas.httpclient.response.Node;
import velas.httpclient.response.TxResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClientTest {

    String pk = "89d5bd2d31889df63cb1c895e4c6f16772e7b06a8c71228bb59d4c9a0c434fc1f6e586d5d051065a580969d15f48f88251ed24b9c77422410bc39a0e7247e53a";
    String pk2 = "caa5802c315c994651e757ab5ae2de1f087ba4588e30cffb3fe7ac022ba4ecc6e6bb0082a92e91f92a5480a1f5d4df435f6752b4b31b3d06c11d126a98bfd978";
    String testNetMasterNodeUrl = "https://testnet.velas.website";

    @Test
    public void NodeInfoTest() throws IOException, InterruptedException {
        VelasClient client = new VelasClient(testNetMasterNodeUrl);

        Node info = client.getNodeInfo();

        assertEquals("p2p_info id", "ccf62994d19a2a08fc8952ad90c2f20830bfad5ba2779bc03c65aa51a7c4c1fc", info.p2pInfo.id);
    }

    @Test
    public void GetWalletTest() throws DecoderException, IOException {
        VelasClient client = new VelasClient(testNetMasterNodeUrl);
        HD hd = new HD(pk2);
        Wallet wallet = hd.toWallet();

        System.out.println(wallet.base58Address);

        BigInteger balance = client.wallet.getBalance(wallet.base58Address);

        System.out.println(balance);
    }

    @Test
    public void GetUnspentTest() throws DecoderException, IOException {
        VelasClient client = new VelasClient(testNetMasterNodeUrl);
        HD hd = new HD(pk2);
        Wallet wallet = hd.toWallet();

        System.out.println(wallet.base58Address);

        List<TransactionInputOutpoint> unspent = client.wallet.getUnspent(wallet.base58Address);

        for (TransactionInputOutpoint un: unspent) {
            System.out.println("=============");
            System.out.println(un.index);
            System.out.println(un.hash);
            System.out.println(un.value);
        }
    }

    @Test
    public void GetHashListByHeightTest() throws JsonProcessingException {
        VelasClient client = new VelasClient(testNetMasterNodeUrl);
        String[] hashes = client.tx.getHashListByAddress("VLa1hi77ZXD2BSWDD9wQe8vAhejXyS7vBM4");

//        var client = new Client(Url);
//        var height = 1500;
//        var hashes = await client.Tx.GetHashListByHeight(height);

        List<TxResponse> resp = client.tx.getByHashList(hashes);

        System.out.println(resp);
    }

}
