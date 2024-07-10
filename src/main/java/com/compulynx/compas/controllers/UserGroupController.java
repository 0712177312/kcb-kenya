package com.compulynx.compas.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.compulynx.compas.customs.responses.*;
import com.compulynx.compas.models.Response;
import com.compulynx.compas.models.RightsItem;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.UserAssignedRights;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.AuthRequest;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
//import com.compulynx.compas.security.service.EncryptionDecryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.extras.UserGroupImpl;
import com.compulynx.compas.models.extras.UserGroupRights;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.services.UserGroupService;

@RestController
@RequestMapping(value = Api.REST)
public class UserGroupController {
	private static final Logger logger = LoggerFactory.getLogger(UserGroupController.class);
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private AESsecure aeSsecure;
	@Autowired
	private EncryptionPayloadResp encryptionPayloadResp;

	Gson gson = new Gson();
	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	
	@GetMapping("/usergroups")
	public ResponseEntity<?> getUserGroups() throws NoSuchAlgorithmException, IOException {
		try {
		List<UserGroup> userGroups =userGroupService.userGroups();
		GlobalResponse2 globalResponse = null;
		if(userGroups.isEmpty()) {
			 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"404",
					false, "cannot find usergroups",
					userGroups);
		}else
			 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000", true, "usergroups", userGroups);

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
	@GetMapping("/usergroups/gtRights")
	public ResponseEntity<?> getUserGroupRights() throws NoSuchAlgorithmException, IOException {
		try {
		List<UserGroupRights> userGroups =userGroupService.getUserGroupRights();
		GlobalResponse2 globalResponse=null;
		if(userGroups.isEmpty()) {
			 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"404", false, "cannot find usergroups", userGroups);

		}else
			 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000", true, "usergroups", userGroups);
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
	
	@GetMapping("/usergroups/gtUserGroups")
	public ResponseEntity<?> getUserGroup() throws NoSuchAlgorithmException, IOException {
		try {
		List<UserGroup> groups = userGroupService.userGroups();
		List<UserGroupImpl> rts = new ArrayList<UserGroupImpl>();
		if(groups.isEmpty()) {
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"404",
					false, "cannot find usergroups",
					rts);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
		for (UserGroup group: groups) {
			UserGroupImpl obj = new UserGroupImpl();
			obj.setId(group.getId());
			obj.setActive(group.isActive());
			obj.setCreatedBy(group.getCreatedBy());
			obj.setGroupCode(group.getGroupCode());
			obj.setGroupName(group.getGroupName());
			List<UserGroupRights> rights=userGroupService.getGroupRights(group.getId(),group.getId(),group.getId());
			obj.setRights(rights);
			rts.add(obj);
		}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000", true, "usergroups", rts);

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
	@GetMapping("/usergroups/getUserGroupUsingGroupId")

	public ResponseEntity<?> getUserGroupUsingGroupId(@RequestHeader HttpHeaders httpHeaders,@RequestParam(value="groupId") String groupIdEnc) throws NoSuchAlgorithmException, IOException {
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);
		String decryptedData = aeSsecure.integratedDataDecryption(key,groupIdEnc);
		String decGroupId =  new Gson().fromJson(decryptedData,String.class);
		Long groupId = Long.parseLong(decGroupId);

		try {
			UserGroup group = userGroupService.getRightCode(groupId);
			List<UserGroupImpl> rts = new ArrayList<UserGroupImpl>();
			if(group == null) {
				GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"404", false, "cannot find usergroup", rts);

				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			UserGroupImpl obj = new UserGroupImpl();
			obj.setId(group.getId());
			obj.setActive(group.isActive());
			obj.setCreatedBy(group.getCreatedBy());
			obj.setGroupCode(group.getGroupCode());
			obj.setGroupName(group.getGroupName());
			List<UserGroupRights> rights=userGroupService.getGroupRights(group.getId(),group.getId(),group.getId());
			obj.setRights(rights);
			rts.add(obj);
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000", true, "usergroup", rts);

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
	@GetMapping("/usergroups/usergroup")
	public ResponseEntity<?> userGroup(@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(value="id") String encId) throws NoSuchAlgorithmException, IOException {

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedId = aeSsecure.integratedDataDecryption(key,encId);
		String strId =  new Gson().fromJson(decryptedId,String.class);

		Long id = Long.parseLong(strId);

		try {
			Optional<UserGroup> group = userGroupService.getUserGroup(id);
			UserGroupResponse globalResponse=null;
			if(!group.isPresent()) {
				 globalResponse = new UserGroupResponse("404","does not exist",false,GlobalResponse.APIV,group.get());

			}else
			 globalResponse = new UserGroupResponse("000","Exist",true,GlobalResponse.APIV,group.get());
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
	
	@PostMapping("/usergroups/assignrights")
	public ResponseEntity<?> addUserGroup(@RequestHeader HttpHeaders httpHeaders,
			@RequestBody String encUserGroup) {
	  try {

		  List<String> headerList = httpHeaders.getValuesAsList("Key");
		  String key = headerList.get(0);

		  String decryptedData = aeSsecure.integratedDataDecryption(key,encUserGroup);
		  Response res= new Gson().fromJson(decryptedData, Response.class);
		  
		  logger.info("res :::::{}",new Gson().toJson(res));
		  logger.info("decrypted data ::{}",decryptedData) ;
		  UserGroup userGroup =  new Gson().fromJson(decryptedData,UserGroup.class);

			List<RightsItem> rights = res.getRights();
			
			List<UserAssignedRights> rights2 = userGroup.getRights();
			
			List<UserAssignedRights> rightsfinal = new ArrayList<>();
			for(RightsItem r : rights) {
				int rId = r.getRightId();
				for(RightsItem r2 : rights) {
					int rId2 = r2.getRightId();
					if(rId == rId2) {
						int rightId = r.getRightId();		
						boolean allowView = r.isAllowView();
						boolean allowAdd = r.isAllowAdd();
						boolean allowEdit = r.isAllowEdit();
						boolean allowDelete = r.isAllowDelete();						
						UserAssignedRights as = new UserAssignedRights(allowView, allowAdd, allowEdit, allowDelete,  rightId);
						rightsfinal.add(as);
						break;
						
					}

				}
			}
			
			userGroup.setRights(rightsfinal);
	
		  logger.info("userGroup :::::{}",new Gson().toJson(userGroup));

		    UserGroup ust = userGroupService.addGroup(userGroup);
		  GlobalResponse2 globalResponse =null;
	    	if(ust ==null) {
	    		
	    		//    public GlobalResponse2(String version,String respCode, boolean status, String respMessage, Collection<?> collection) {
	    		globalResponse= new GlobalResponse2(GlobalResponse.APIV, "203", false, "failed to save user groups");
	    	} else {
	    		globalResponse= new GlobalResponse2(GlobalResponse.APIV, "000", true, "user group updated successfully");

	      }
	 	   String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
		   encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
		   HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
		   return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404","error processing request",false,GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}
}
