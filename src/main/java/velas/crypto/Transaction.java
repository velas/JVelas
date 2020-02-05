package velas.crypto;

import com.google.bitcoin.core.Base58;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Transaction extends TransactionModel {

    public Transaction() {
        super();
    }

    public Transaction(String hash, List<TransactionInput> txIns, List<TransactionOut> txOuts, String network) {
        super(hash, txIns, txOuts, network);
    }

    public Transaction(List<TransactionInputOutpoint> unspents, BigInteger amount, HD keys, String fromAddress,
                       String toAddress, BigInteger commission) throws Exception {
        super();

        BigInteger zero = new BigInteger("0");
        BigInteger totalin = new BigInteger("0");

        for (TransactionInputOutpoint un: unspents) {
            totalin = totalin.add(un.value);
        }

        this.txIns = new ArrayList<TransactionInput>();

        int index = 0;

        // Commission
        this.txOuts = new ArrayList<TransactionOut>();
        txOuts.add(new TransactionOut(index, commission, ""));
        index += 1;

        // Dest address
        String to = Hex.encodeHexString(Base58.decode(toAddress));
        txOuts.add(new TransactionOut(index, amount, to));
        index += 1;

        BigInteger change = totalin.subtract(amount).subtract(commission);
        if (change.compareTo(zero) > 0) {
            String from = Hex.encodeHexString(Base58.decode(fromAddress));
            txOuts.add(new TransactionOut(index, change, from));
        }

        for (TransactionInputOutpoint prevOut: unspents) {
            byte[] sigMsg = this.msgForSign(prevOut.hash, prevOut.index);
            byte[] sig = Crypto.Sign(keys.cryptoKeys(), sigMsg);
            txIns.add(new TransactionInput(Hex.encodeHexString(sig), keys.publicKey(), prevOut));
        }

        byte[] hash = this.generateHash();
        this.hash = Hex.encodeHexString(hash);
    }

    public Transaction(List<TransactionInputOutpoint> unspents, HD keys, String fromAddress,
                       List<Receiver> receivers, BigInteger commission) throws Exception {
        super();

        BigInteger zero = new BigInteger("0");
        BigInteger totalin = new BigInteger("0");
        BigInteger totalout = new BigInteger("0");

        for (TransactionInputOutpoint un: unspents) {
            totalin = totalin.add(un.value);
        }

        this.txIns = new ArrayList<TransactionInput>();

        int index = 0;

        // Commission
        this.txOuts = new ArrayList<TransactionOut>();
        txOuts.add(new TransactionOut(index, commission, ""));
        index += 1;

        // receivers
        for (Receiver rec: receivers) {
            totalout = totalout.add(rec.amount);
            String to = Hex.encodeHexString(Base58.decode(rec.wallet));
            txOuts.add(new TransactionOut(index, rec.amount, to));
            index +=1;
        }


        BigInteger change = totalin.subtract(totalout).subtract(commission);
        if (change.compareTo(zero) > 0) {
            String from = Hex.encodeHexString(Base58.decode(fromAddress));
            txOuts.add(new TransactionOut(index, change, from));
        }

        for (TransactionInputOutpoint prevOut: unspents) {
            byte[] sigMsg = this.msgForSign(prevOut.hash, prevOut.index);
            byte[] sig = Crypto.Sign(keys.cryptoKeys(), sigMsg);
            txIns.add(new TransactionInput(Hex.encodeHexString(sig), keys.publicKey(), prevOut));
        }

        byte[] hash = this.generateHash();

        this.hash = Hex.encodeHexString(hash);
    }

}
