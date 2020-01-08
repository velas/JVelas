package velas.httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import velas.httpclient.response.Node;

public class VelasClient {

    String baseAddr;
    private ObjectMapper mapper;

    public Wallet wallet;
    public Tx tx;

    public VelasClient(String baseAddr) {
        this.baseAddr = baseAddr;
        this.mapper = new ObjectMapper();

        this.wallet = new Wallet(this.baseAddr, this.mapper);
        this.tx = new Tx(this.baseAddr, this.mapper);
    }

    public Node getNodeInfo() throws JsonProcessingException {
        HttpResponse<String> request = Unirest.get(this.baseAddr + "/api/v1/info").asString();
        System.out.println(request.getBody());
        return this.mapper.readValue(request.getBody(), Node.class);
    }

}
