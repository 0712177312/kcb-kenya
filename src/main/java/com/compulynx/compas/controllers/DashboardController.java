package com.compulynx.compas.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.compulynx.compas.customs.responses.GlobalResponse2;
import com.compulynx.compas.security.AESsecure;

import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.compulynx.compas.customs.CommonFunctions;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.models.extras.CardInfo;
import com.compulynx.compas.models.extras.ServerConfig;
import com.compulynx.compas.models.extras.TopFiveBranches;
import com.compulynx.compas.repositories.BranchRepository;
import com.compulynx.compas.repositories.CustomerRepository;

@RestController
@RequestMapping(value = Api.REST+ "/dashboard")
public class DashboardController {
    @Autowired
    private Environment env;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private AESsecure aeSsecure;
    @Autowired
    private EncryptionPayloadResp encryptionPayloadResp;
    Gson gson = new Gson();

    @GetMapping("/cardinfo")
    public ResponseEntity<?> cardInfo() throws NoSuchAlgorithmException, IOException {
        String responsePayload = "";
        try {
            int customers = customerRepository.getCustomerCount();
            int branches = customerRepository.getEnrolledBranchesCount();
            int waivedBranches = branchRepository.getWaivedBranchesCount();

            CardInfo cin = new CardInfo();

            cin.setBraches(branches);
            cin.setCustomers(customers);
            cin.setWaivedBranches(waivedBranches);

            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(cin));
            responsePayload = encryptionPayloadResp.getEncryptedPayload();
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(responsePayload);
        } catch (Exception e) {
            GlobalResponse resp = new GlobalResponse("404", "Server failure authenticating user", false, GlobalResponse.APIV);
            e.printStackTrace();

            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(resp));
            responsePayload = encryptionPayloadResp.getEncryptedPayload();
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(responsePayload);
        }
    }

    @GetMapping(value = "/configs")
    public ResponseEntity<?> getConfigs() throws NoSuchAlgorithmException, IOException {
        String response = "";
        EncryptionPayloadResp encryptionPayloadResp =null;

        System.out.println("env.getProperty(cobankingAuthName)");
        System.out.println(env.getProperty("abis"));
        System.out.println(env.getProperty("outUri"));
        System.out.println(env.getProperty("compas"));
        System.out.println(env.getProperty("greenbit"));
        System.out.println(env.getProperty("sessionTimeout"));
        System.out.println(env.getProperty("sessionIdle"));
        System.out.println(env.getProperty("jwt"));
        System.out.println(env.getProperty("secugen"));
        System.out.println(env.getProperty("customerInqEndpoint"));
        System.out.println(env.getProperty("staffInqEndpoint"));

        try {
            String authName = env.getProperty("cobankingAuthName");
            String authPass = env.getProperty("cobankingAuthPass");
            String t24se = env.getProperty("tserver");
            String cobanking = env.getProperty("cobanking");
            String abis = env.getProperty("abis");
            String greenbit = env.getProperty("greenbit");
            String compasUri = env.getProperty("compasUri");
            String outUri = env.getProperty("outUri");
            String secugen = env.getProperty("secugen");
            String sessionTimeout = env.getProperty("sessionTimeout");
            String sessionIdle = env.getProperty("sessionIdle");
            String jwt = env.getProperty("jwt");//TokenAuthenticationService.getString(username);
            String customerInqEndpoint = env.getProperty("customerInqEndpoint");
            String staffInqEndpoint = env.getProperty("staffInqEndpoint");

            ServerConfig resp = new ServerConfig(t24se, cobanking, abis, greenbit, secugen, authPass, authName, sessionTimeout, sessionIdle, jwt,customerInqEndpoint,staffInqEndpoint,compasUri,outUri);

            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(resp));
            response = encryptionPayloadResp.getEncryptedPayload();
        } catch (Exception e) {
            GlobalResponse resp = new GlobalResponse("404", "Server failure authenticating user", false, GlobalResponse.APIV);
            e.printStackTrace();
            System.out.println("Here is the response");
            System.out.println(resp);
            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(resp));
            response = encryptionPayloadResp.getEncryptedPayload();
        }

        HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
        return ResponseEntity.ok().headers(headers).body(response);
    }

    @GetMapping("/topbranches")
    public ResponseEntity<?> topBranches() throws NoSuchAlgorithmException, IOException {
        String responsePayload = "";
        try {
            List<TopFiveBranches> customers = customerRepository.getTopFiveBranches();
            if (customers.size() > 0) {
                GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000",
                        true, "customers found",
                        customers);
                String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
                encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
                HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
                return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
            }
            GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "201",
                    false, "no customers found",
                    customers);

            String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
            encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
        } catch (Exception e) {
            GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
            e.printStackTrace();

            String jsonObj = CommonFunctions.convertPojoToJson(resp);
            encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
        }
    }
}