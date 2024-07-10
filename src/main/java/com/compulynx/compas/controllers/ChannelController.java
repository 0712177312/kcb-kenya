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
import org.springframework.web.bind.annotation.*;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.models.Channel;
import com.compulynx.compas.models.extras.ChannelRep;
import com.compulynx.compas.services.ChannelService;
import com.compulynx.compas.customs.CommonFunctions;

@RestController
@RequestMapping(value = Api.REST)
public class ChannelController {
	@Autowired
	private ChannelService channelService;

	@Autowired
	private AESsecure aeSsecure;
	@Autowired
	private EncryptionPayloadResp encryptionPayloadResp;

	Gson gson = new Gson();
	
	@GetMapping(value = "/gtchannels")
	public ResponseEntity<?> GetChannels() throws NoSuchAlgorithmException, IOException {
		String responsePayload="";
		try {
		List<ChannelRep> channels = channelService.getChannels();
		if(channels.size() <= 0) {
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"201",
					false, "no channels found",
					channels);
			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000",
					true, "channels found",
					channels);

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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
	    }
	}
	@GetMapping(value = "/gtChannelstoWaive")
	public ResponseEntity<?> GetChannelsToWaive() throws NoSuchAlgorithmException, IOException {
		try {
		List<Channel> channels = channelService.getChannelsToWaive();
		if(channels.size() <= 0) {
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"201",
					false, "no channels found",
					channels);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000",
					true, "channels found",
					channels);

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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
	    }
	}
	@PostMapping(value = "/upChannel") 
	public ResponseEntity<?> UpChannels(@RequestHeader HttpHeaders httpHeaders,
			@RequestBody String encChannel){
	 try {
		 List<String> headerList = httpHeaders.getValuesAsList("Key");
		 String key = headerList.get(0);

		 String decryptedData = aeSsecure.integratedDataDecryption(key,encChannel);
		 Channel channel =  new Gson().fromJson(decryptedData,Channel.class);

		 Channel chann = channelService.upChannel(channel);
		
		if(chann == null ) {
			return new ResponseEntity<>(new GlobalResponse("201",
					"channel details were not updated",false,GlobalResponse.APIV),HttpStatus.OK);
		}
	    	return new ResponseEntity<>(new GlobalResponse("000",
				  "channel details updated successfully",true,GlobalResponse.APIV),HttpStatus.OK);
    	} catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	    }
	}
	
	@GetMapping(value = "/gtWaivedchannels")
	public ResponseEntity<?> GetWaivedChannels() throws NoSuchAlgorithmException, IOException {
		try {
		List<Channel> channels = channelService.getWaivedChannles();
		if(channels.size() <= 0) {
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"201",
					false, "no channels found",
					channels);
			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000",
					true, "channels found",
					channels);

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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

	    }
	}
	
	@PostMapping(value = "/waiveChannel") 
	public ResponseEntity<?> waiveChannel(@RequestHeader HttpHeaders httpHeaders,
			@RequestBody String encChannel){
	 try {
		 List<String> headerList = httpHeaders.getValuesAsList("Key");
		 String key = headerList.get(0);

		 String decryptedData = aeSsecure.integratedDataDecryption(key,encChannel);
		 Channel channel =  new Gson().fromJson(decryptedData,Channel.class);
		 int chann = channelService.waiveChannel(channel.getWaivedBy(),channel.getId());
		
		if(chann > 0 ) {
	    	return new ResponseEntity<>(new GlobalResponse("000",
				  "channel details updated successfully",true,GlobalResponse.APIV),HttpStatus.OK);
		}

		return new ResponseEntity<>(new GlobalResponse("201",
				"channel details were not updated",false,GlobalResponse.APIV),HttpStatus.OK);

    	} catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	    }
	}
	
	@PostMapping(value = "/rejectChannel")
	public ResponseEntity<?> rejectChannel(@RequestHeader HttpHeaders httpHeaders,
			@RequestBody String encChannel) {
	 try {
		 List<String> headerList = httpHeaders.getValuesAsList("Key");
		 String key = headerList.get(0);

		 String decryptedData = aeSsecure.integratedDataDecryption(key,encChannel);
		 Channel channel =  new Gson().fromJson(decryptedData,Channel.class);
		 int chann = channelService.recectChannel(channel.getWaivedApprovedBy(),channel.getId());

		if(chann > 0 ) {
	    	return new ResponseEntity<>(new GlobalResponse("000",
				  "channel details updated successfully",true,GlobalResponse.APIV),HttpStatus.OK);
		}

		return new ResponseEntity<>(new GlobalResponse("201",
				"channel details were not updated",false,GlobalResponse.APIV),HttpStatus.OK);

    	} catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	   }
	}
	
	@PostMapping(value = "/approveChannelWaive") 
	public ResponseEntity<?> approveChannelWaive(@RequestHeader HttpHeaders httpHeaders,
			@RequestBody String encChannel){
	 try {
		 List<String> headerList = httpHeaders.getValuesAsList("Key");
		 String key = headerList.get(0);

		 String decryptedData = aeSsecure.integratedDataDecryption(key,encChannel);
		 Channel channel =  new Gson().fromJson(decryptedData,Channel.class);
		 int chann = channelService.approveChannelWaive(channel.getWaivedApprovedBy(),channel.getId());

		 if(chann > 0 ) {
	    	return new ResponseEntity<>(new GlobalResponse("000",
				  "channel details updated successfully",true,GlobalResponse.APIV),HttpStatus.OK);
		}

		return new ResponseEntity<>(new GlobalResponse("201",
				"channel details were not updated",false,GlobalResponse.APIV),HttpStatus.OK);

    	} catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	    }
	}
}
