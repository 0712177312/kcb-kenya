package com.compulynx.compas.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

import com.compulynx.compas.customs.responses.GlobalResponse2;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.models.MenuHeaderMaster;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.services.MenuService;

@RestController
@RequestMapping(value=Api.REST)
public class MenuController {
	@Autowired
	private MenuService headerService;

	@Autowired
	private AESsecure aeSsecure;
	@Autowired
	private EncryptionPayloadResp encryptionPayloadResp;
	Gson gson = new Gson();
	
	@GetMapping("/menulist")
	public ResponseEntity<?> getHeaderMenus() throws NoSuchAlgorithmException, IOException {
		try {
		Collection<MenuHeaderMaster> menus = headerService.getMenuHeaders();
		
		if(menus.isEmpty()) {

			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"404",
					false, "no menu list  found",
					menus);
			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000",
					true, "menus found",
					menus);
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
	
	@GetMapping("/menulist/group")
	public ResponseEntity<?> getGroupMenus(@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(value="groupId") String groupIdEnc) throws NoSuchAlgorithmException, IOException {

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);
		String decryptedData = aeSsecure.integratedDataDecryption(key,groupIdEnc);
		String decGroupId =  new Gson().fromJson(decryptedData,String.class);
		Long groupId = Long.parseLong(decGroupId);

	 try {
		List<MenuHeaderMaster> menus =headerService.getGroupMenus(groupId);
		
		if(menus.isEmpty()) {
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"404",
					false, "no rights found",
					menus);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		  }
		 GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000",
				 true, "rights found",
				 menus);
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
