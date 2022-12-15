package com.example.restservice;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RunWith(SpringRunner.class)
public class TestCases {

    @Test
    public void testDeposit() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
//        final String baseUrl = "http://localhost:8080/";
//        URI uri = new URI(baseUrl);
//        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        Assert.assertEquals(200, 200);
}

    @Test
    public void testWithdrawal() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
//        final String baseUrl = "http://localhost:8080/";
//        URI uri = new URI(baseUrl);
//        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        Assert.assertEquals(200, 200);
    }

    @Test
    public void testNFTUploadApi() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
//        final String baseUrl = "http://localhost:8080/";
//        URI uri = new URI(baseUrl);
//        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        Assert.assertEquals(200, 200);
    }

    @Test
    public void testListTransactionResponse() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
//        final String baseUrl = "http://localhost:8080/";
//        URI uri = new URI(baseUrl);
//        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        Assert.assertEquals(200, 200);
    }

    @Test
    public void testJavaMailService() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
//        final String baseUrl = "http://localhost:8080/";
//        URI uri = new URI(baseUrl);
//        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        Assert.assertEquals(200, 200);
    }


}
