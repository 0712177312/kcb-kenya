package com.compulynx.compas.security.web;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.AuthRequest;
import com.compulynx.compas.security.model.BioAuthReq;
import com.compulynx.compas.security.model.BioTokenVerifyReq;
import com.compulynx.compas.security.model.Token;
//import com.compulynx.compas.security.service.BouncyCastleService;
//import com.compulynx.compas.security.service.EncryptionDecryptionService;
import com.compulynx.compas.security.service.LoginService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping(value = Api.REST + "/auth")
public class AuthenticationController {
    @Autowired
    private LoginService authenticationService;
    @Autowired
    private AESsecure aeSsecure;

    @PostMapping("/manual_login")
    public ResponseEntity<?> manualLogin(@RequestHeader HttpHeaders httpHeaders, @RequestBody String encAuthRequest) throws IOException, NoSuchAlgorithmException {
        System.out.println("LOGIN CONTROLLER !!!!!!!!!!!!!!!!!!!!!!!!!!!");

        List<String> headerList = httpHeaders.getValuesAsList("Key");
        String key = headerList.get(0);

        String decryptedData = aeSsecure.integratedDataDecryption(key,encAuthRequest);

        AuthRequest authRequest =  new Gson().fromJson(decryptedData,AuthRequest.class);

        return authenticationService.login(authRequest);
    }
    @PostMapping("/bio_login")//RECEIVES SUCCESSFUL AUTHENTICATION FROM THE ABIS CLIENT
    public String bioLogin(@RequestBody BioAuthReq bioAuthReq){
        return new Gson().toJson(authenticationService.bioLogin(bioAuthReq));
    }
    @PostMapping("/verify_abisclient_token")
    public ResponseEntity<?> verifyTokenFromAbis(@RequestBody BioTokenVerifyReq bioTokenVerify){
        return authenticationService.verifyBioToken(bioTokenVerify);
    }
    @PostMapping("/sys_logout")
    public ResponseEntity<?> logout(@RequestBody Object object){
        return authenticationService.logout();
    }

}