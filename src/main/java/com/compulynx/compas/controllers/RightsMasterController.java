package com.compulynx.compas.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.compulynx.compas.customs.responses.GlobalResponse2;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.models.RightMaster;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.services.RightsMasterService;

@RestController
@RequestMapping(value = Api.REST)
public class RightsMasterController {
	@Autowired
	private RightsMasterService masterService;

	@Autowired
	private AESsecure aeSsecure;
	@Autowired
	private EncryptionPayloadResp encryptionPayloadResp;

	Gson gson = new Gson();
	
	@GetMapping(value="/rightsmenulist")
	public ResponseEntity<?> getRights() throws NoSuchAlgorithmException, IOException {
		try {
		List<RightMaster> rights = masterService.getRights();
		
		if(rights.isEmpty()) {

			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"404",
					false, "no rights found",
					rights);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000",
					false, "no rights found",
					rights);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404","error processing request",false,GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}
}
