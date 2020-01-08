package velas.httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import velas.crypto.TransactionInputOutpoint;

import java.math.BigInteger;
import java.util.List;

public class Wallet {

    String baseAddr;
    private ObjectMapper mapper;

    public Wallet(String addr, ObjectMapper mapper) {
        this.baseAddr = addr;
        this.mapper = mapper;
    }

    public BigInteger getBalance(String address) throws JsonProcessingException {
        HttpResponse<String> request = Unirest.get(this.baseAddr + "/api/v1/wallet/balance/" + address).asString();
        Balance balance = this.mapper.readValue(request.getBody(), Balance.class);
        return balance.amount;
    }

    public List<TransactionInputOutpoint> getUnspent(String address) throws JsonProcessingException {
        HttpResponse<String> request = Unirest.get(this.baseAddr + "/api/v1/wallet/unspent/" + address).asString();
        return this.mapper.readValue(request.getBody(),  mapper.getTypeFactory().constructCollectionType(List.class, TransactionInputOutpoint.class));
    }

    public List<TransactionInputOutpoint> getUnspentForStaking(String address) throws JsonProcessingException {
        HttpResponse<String> request = Unirest.get(this.baseAddr + "/api/v1/wallet/unspent_for_staking/" + address).asString();
        return this.mapper.readValue(request.getBody(),  mapper.getTypeFactory().constructCollectionType(List.class, TransactionInputOutpoint.class));
    }

}
