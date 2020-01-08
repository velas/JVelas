package velas.httpclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import velas.crypto.Transaction;
import velas.httpclient.response.TxResponse;

import java.util.List;

public class Tx {

    String baseAddr;
    private ObjectMapper mapper;

    public Tx(String addr, ObjectMapper mapper) {
        this.baseAddr = addr;
        this.mapper = mapper;
    }

    public String[] getHashListByAddress(String address) throws JsonProcessingException {
        HttpResponse<String> request = Unirest.get(this.baseAddr + "/api/v1/wallet/txs/" + address).asString();
        return this.mapper.readValue(request.getBody(), String[].class);
    }

    public String[] getHashListByHeight(int height) throws JsonProcessingException {
        HttpResponse<String> request = Unirest.get(this.baseAddr + "/api/v1/txs/height/" + height).asString();
        return this.mapper.readValue(request.getBody(), String[].class);
    }

    public boolean validate(Transaction tx) throws Exception {
        HttpResponse<String> request = Unirest.post(this.baseAddr + "/api/v1/txs/validate")
                .body(this.mapper.writeValueAsString(tx))
                .asString();

        if (request.getStatus() == 400) {
            ErrorResponse res = this.mapper.readValue(request.getBody(), ErrorResponse.class);
            throw new Exception(res.error);
        }

        TxPublishResponse res = this.mapper.readValue(request.getBody(), TxPublishResponse.class);
        return res.result.equals("ok");
    }

    public boolean publish(Transaction tx) throws Exception {
        HttpResponse<String> request = Unirest.post(this.baseAddr + "/api/v1/txs/publish")
                .body(this.mapper.writeValueAsString(tx))
                .asString();

        if (request.getStatus() == 400) {
            ErrorResponse res = this.mapper.readValue(request.getBody(), ErrorResponse.class);
            throw new Exception(res.error);
        }

        TxPublishResponse res = this.mapper.readValue(request.getBody(), TxPublishResponse.class);
        return res.result.equals("ok");
    }

    public List<TxResponse> getByHashList(String[] hashes) throws JsonProcessingException {
        TxRequest req = new TxRequest(hashes);

        HttpResponse<String> request = Unirest.post(this.baseAddr + "/api/v1/txs")
                .body(this.mapper.writeValueAsString(req))
                .asString();

        return this.mapper.readValue(request.getBody(),  mapper.getTypeFactory().constructCollectionType(List.class, TxResponse.class));
    }

}