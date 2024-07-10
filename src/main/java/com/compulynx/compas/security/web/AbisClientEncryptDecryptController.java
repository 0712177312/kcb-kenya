package com.compulynx.compas.security.web;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.AbisDataDecryptReq;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.rabbitmq.tools.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = Api.REST + "/abisClient_encrypt_decrypt")
public class AbisClientEncryptDecryptController {

    @Autowired
    private AESsecure aeSsecure;

    @PostMapping("/encrypt")
    public EncryptionPayloadResp encryptAbisData(@RequestBody String dataToEncrypt) throws NoSuchAlgorithmException, IOException {

        try{
            EncryptionPayloadResp encryptionPayloadResp = aeSsecure.integratedDataEncryption(dataToEncrypt);
            return encryptionPayloadResp;//new ResponseEntity<>(encryptionPayloadResp,HttpStatus.OK);
       }catch(Exception e){
           e.printStackTrace();
           return null;//new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/decrypt")
    public String dencryptAbisData(@RequestBody AbisDataDecryptReq abisDataDecryptReq) throws NoSuchAlgorithmException, IOException {
        try{
            String decryptedData  = aeSsecure.integratedDataDecryption(abisDataDecryptReq.getKeyPayload(), abisDataDecryptReq.getDataPayload());
            return decryptedData;//new ResponseEntity<>(decryptedData, HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return "";//new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
