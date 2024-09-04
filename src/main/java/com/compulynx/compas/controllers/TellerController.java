package com.compulynx.compas.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.compulynx.compas.customs.responses.GeneralResponse;
import com.compulynx.compas.customs.responses.GlobalResponse2;
import com.compulynx.compas.mail.EmailSender;
import com.compulynx.compas.models.SysLogs;
import com.compulynx.compas.models.extras.TellersToApproveDetach;
import com.compulynx.compas.models.t24Models.Staff;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.compulynx.compas.services.UserService;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.customs.responses.TellerResponse;
import com.compulynx.compas.models.Teller;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.extras.TellerToApprove;
import com.compulynx.compas.models.extras.TellerToDisplay;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.services.CustomerService;
import com.compulynx.compas.services.TellerService;
import com.compulynx.compas.services.UserGroupService;

@RestController
@RequestMapping(value = Api.REST + "/tellers")
public class TellerController {
	private static final Logger log = LoggerFactory.getLogger(TellerController.class);
	
	private static HttpsURLConnection httpsURLConnection;
	private static BufferedReader reader;
	private static String line="";
	
	@Autowired
	private Environment env;
	@Autowired
	private TellerService tellerService;

	@Autowired
	private CustomerService customerSvc;
	@Autowired
	private UserService userService;
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private EmailSender emailSender;

	@Autowired
	private AESsecure aeSsecure;
	@Autowired
	private EncryptionPayloadResp encryptionPayloadResp;

	Gson gson = new Gson();
	
	@PostMapping("/staff_inquiry")
	public ResponseEntity<?> getStaff(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encStaff) throws NoSuchAlgorithmException, IOException {
		String responsePayload = "";
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encStaff);
		Staff staf =  new Gson().fromJson(decryptedData,Staff.class);
		if(staf.getId() == "" || staf.getId() == null) {
			GlobalResponse resp = new GlobalResponse("404", "error processing request: staffid is missing", false, GlobalResponse.APIV);

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
		return tellerService.t24StaffInquiry(staf);
	}

	@GetMapping(value = "/gtTellers")
	public ResponseEntity<?> getTellers() throws NoSuchAlgorithmException, IOException {
		try {
			List<Teller> tellers = tellerService.getTellers();
			GlobalResponse2 globalResponse = null;
			if (tellers.size() > 0)
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "tellers found", tellers);
			else
			 	globalResponse = new GlobalResponse2(GlobalResponse.APIV, "201", false, "no tellers found", tellers);

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

	@GetMapping(value = "/gtBranchTellers")
	public ResponseEntity<?> getBranchTellers(@RequestHeader HttpHeaders httpHeaders, @RequestParam(value = "branch") String encBranch) throws NoSuchAlgorithmException, IOException {

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);
		String decryptedData = aeSsecure.integratedDataDecryption(key,encBranch);
		String branch =  new Gson().fromJson(decryptedData,String.class);
		try {
			List<Teller> tellers = tellerService.getBranchTellers(branch);
			GlobalResponse2 globalResponse = null;
			if (tellers.size() > 0)
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "tellers found", tellers);
			else
			 	 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "201", false, "no tellers found", tellers);

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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}

	@GetMapping(value = "/gtTeller")
	public ResponseEntity<?> getTellerDetails(@RequestHeader HttpHeaders httpHeaders,@RequestParam(value = "tellr") String encTellr) throws NoSuchAlgorithmException, IOException {
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);
		String decryptedData = aeSsecure.integratedDataDecryption(key,encTellr);
		String tellr =  new Gson().fromJson(decryptedData,String.class);
		try {
			Teller teller = tellerService.getTeller(tellr);
			TellerResponse tellerResponse = null;
			if (teller != null)
				 tellerResponse = new TellerResponse("000", "teller found", true, GlobalResponse.APIV, teller);
			 else
				 tellerResponse = new TellerResponse("201", "teller not found", false, GlobalResponse.APIV, teller);

				String jsonObj = CommonFunctions.convertPojoToJson(tellerResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}

	@PostMapping({ "/upTellerDetails" })
	public ResponseEntity<?> upCustomerDetails(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
			Teller teller =  new Gson().fromJson(decryptedData,Teller.class);
			System.out.println("####" + teller.getTellerId());
			Teller cust = null;
			int tellerUndeleted = 0;
			GeneralResponse generalResponse=null;
			if (tellerService.checkStaffDeleted(teller.getTellerId()) != null) {
				tellerUndeleted = tellerService.staffUnDelete(teller.getCreatedBy(), teller.getTellerId());
			} else {
				generalResponse = tellerService.upTellerDetails(teller);
			}
			log.info("teller details updated succesfull for " + teller.getCustomerId());
			if (generalResponse.getStatusCode().equalsIgnoreCase("200") || tellerUndeleted > 0) {
				return new ResponseEntity(new GlobalResponse("000", "staff found", true, "1.0.0"), HttpStatus.OK);
			}
			return new ResponseEntity(new GlobalResponse("1.0.0", "201", false, "no staff found"), HttpStatus.OK);
		} catch (Exception e) {
			log.error("upTellerDetails", e);
			GlobalResponse resp = new GlobalResponse("404", "error processing request", false, "1.0.0");
			e.printStackTrace();
			return new ResponseEntity(resp, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/checkTeller")
	public ResponseEntity<?> checkCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
			Teller teller =  new Gson().fromJson(decryptedData,Teller.class);

			Teller cust = tellerService.checkTeller(teller.getTellerId());
			if (cust != null) {
				return new ResponseEntity<>(
						new GlobalResponse("000", "teller already enrolled", true, GlobalResponse.APIV), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new GlobalResponse(GlobalResponse.APIV, "201", false, "teller not enrolled"), HttpStatus.OK);
			}
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/checkStaffApproved")
	public ResponseEntity<?> checkStaffApproved(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) throws NoSuchAlgorithmException, IOException {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
			Teller teller =  new Gson().fromJson(decryptedData,Teller.class);
			Teller cust = tellerService.checkStaffApproved(teller.getTellerId());
			TellerResponse tellerResponse =  null;
			if (cust != null)
				 tellerResponse = new TellerResponse("000", "teller found", true, GlobalResponse.APIV, cust);
			 else
				 tellerResponse = new TellerResponse("201", "teller not found", false, GlobalResponse.APIV, teller);

			String jsonObj = CommonFunctions.convertPojoToJson(tellerResponse);
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

	@GetMapping(value = "/tellersToApprove")
	public ResponseEntity<?> getCustomersToApprove(@RequestHeader HttpHeaders httpHeaders, @RequestParam(value = "branchCode") String encBranchCode,
			@RequestParam(value = "groupid") String encGroupid) throws NoSuchAlgorithmException, IOException {
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);
		String decryptedBranchCode = aeSsecure.integratedDataDecryption(key,encBranchCode);
		String decryptedGroupId = aeSsecure.integratedDataDecryption(key,encGroupid);
		String groupid =  new Gson().fromJson(decryptedGroupId,String.class);
		String branchCode =  new Gson().fromJson(decryptedBranchCode,String.class);
		try {
			System.out.println("Branch Code: " + branchCode);
			System.out.println("Right ID: " + groupid);

			UserGroup userGroup = userGroupService.getRightCode(Long.valueOf(groupid));
			List<TellerToApprove> tellers;
			if (userGroup.getGroupCode().equalsIgnoreCase("G001")
                    || userGroup.getGroupCode().equalsIgnoreCase("G002") 
                    || userGroup.getGroupCode().equalsIgnoreCase("G003")
                    || userGroup.getGroupCode().equalsIgnoreCase("G004")) {
				tellers = tellerService.getTellersToVerifyAll();
			} else {
				tellers = tellerService.getTellersToVerify(branchCode);
			}

			GlobalResponse2 globalResponse = null;
			if (tellers.size() > 0)
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", false, "tellers to verify found", tellers);
				else
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "201", false, "no tellers to verify found", tellers);

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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			//return new ResponseEntity<>(aeSsecure.encrypt(gson.toJson(resp)), HttpStatus.OK);
		}
	}

	@GetMapping(value = "/tellersToApproveDetach")
	public ResponseEntity<?> getTellersToApproveDetach() throws NoSuchAlgorithmException, IOException {

		try {
			List<TellersToApproveDetach> tellers = tellerService.getTellersToApproveDetach();
			GlobalResponse2 globalResponse = null;
			if (tellers.size() > 0)
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "tellers to approve detach found", tellers);
			 else
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "201", false,
						"no tellers to approve detach found", tellers);

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

	@PostMapping(value = "/approveTeller")
	public ResponseEntity<?> approveCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) throws NoSuchAlgorithmException, IOException {

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
		Teller teller =  new Gson().fromJson(decryptedData,Teller.class);
		 log.info("Teller information :: {}",new Gson().toJson(teller));
		//added if statement to omit t24 request on an already enrolled staff -existing in customer table
	//	if(!teller.getEnroll().equalsIgnoreCase("U") && teller.getEnroll() == null ) {
        try {
            String t24Url = env.getProperty("tserver");
            String customerId = teller.getCustomerId();
            String enrollStatus=teller.getEnroll();
            log.info(new Gson().toJson(teller));
            log.info("update url for " + t24Url);
            log.info("enroll status to use {}",enrollStatus);
            log.info("Verified by id :: {} ",teller.getVerifiedBy());

            GlobalResponse response = customerSvc.updateCustomerAndStaff(t24Url, customerId,"TRUE");            

            log.info("T24 response response " + response.getRespMessage());
            if(response.getRespCode() != "200"){

				GlobalResponse  globalResponse = new GlobalResponse(response.getRespCode(), response.getRespMessage(), false,GlobalResponse.APIV);
				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
            }

        } catch (Exception e) {
            log.error("upTellerDetails", e);
			GlobalResponse globalResponse = new GlobalResponse("500", "HpptRestProcessor Exception", false, GlobalResponse.APIV);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
        }
//	}
		try {
			int cust = tellerService.approveTeller(teller.getVerifiedBy(), teller.getCustomerId());

			GlobalResponse globalResponse = null;
			if (cust > 0) {
				String recipient = teller.getTellerEmail();
				if(recipient == null) {
					log.info("Email for " + teller.getTellerName() + " is null");
				}else if(!recipient.contains("@")){
					log.info("Email for " + teller.getTellerName() + " is not available");
				}else {
					String subject = "Biometric Details of Staff Captured";
					String emailContent = "Dear " + teller.getTellerName() + ", your biometric details have been successfully registered. For any queries please call 0711087000 or 0732187000.";
					emailSender.sendEmail(recipient, subject, emailContent);
					log.info("Email to staff scheduled to be sent to " + teller.getTellerName());
				}
				 globalResponse = new GlobalResponse( "000",
						"teller  " + teller.getCustomerId() + " verified successfully",true,GlobalResponse.APIV);
			} else
				 globalResponse = new GlobalResponse("404", "no teller found", false,GlobalResponse.APIV);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("500", "error processing request", false, GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}
	@PostMapping(value = "/upgradeCustomerProfile")
	public ResponseEntity<?> upgradeCustomerProfile(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
			Teller teller =  new Gson().fromJson(decryptedData,Teller.class);
			Teller cust = null;
			GeneralResponse generalResponse=null;
			int tellerUndeleted = 0;
			
			//START-ADDED BY KENSON -08/05/2024- SELF AUTHORISED
			 teller.setVerified("A");
			//END 
			if (tellerService.checkStaffDeleted(teller.getTellerId()) != null) {
				tellerUndeleted = tellerService.staffUnDelete(teller.getCreatedBy(), teller.getTellerId());
				generalResponse = tellerService.upTellerDetails(teller);
				
			} else {
				generalResponse = tellerService.upTellerDetails(teller);
			}
			if (generalResponse.getStatusCode().equalsIgnoreCase("200") || tellerUndeleted > 0) {
				int prof = this.customerSvc.upgradeCustomerProfile(teller.getCustomerId());
				if (prof > 0) {
					//commented by KENSON
					int userStatusUpdate = userService.updateStatusToTrue(teller.getTellerSignOnName());
				if (userStatusUpdate > 0) {
						return new ResponseEntity<>(
								new GlobalResponse("000", "customer upgraded successfully", true, GlobalResponse.APIV),
								HttpStatus.OK);				
						}
				else {
					return new ResponseEntity<>(
							new GlobalResponse("201", "failed to update user status", false, GlobalResponse.APIV),
							HttpStatus.OK);
					}
				} else {
					return new ResponseEntity<>(
							new GlobalResponse("201", "failed to upgrade customer details", false, GlobalResponse.APIV),
							HttpStatus.OK);
				}
			} else if (generalResponse.getStatusCode().equalsIgnoreCase("201")) {
				return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV, "201", false, "Customers already exist"),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV, "201", false, "no customers found"),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println("################################## SEVERE");
			System.out.println(e.getMessage());
			GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/rejectTellerApproval")
	public ResponseEntity<?> rejectTellerApproval(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
			Teller teller =  new Gson().fromJson(decryptedData,Teller.class);

			int updates = tellerService.rejectTellerApproval(teller.getRejectedBy(), teller.getCustomerId(),teller.getReason());
			if (updates > 0) {
				return new ResponseEntity<>(
						new GlobalResponse("000", "Teller rejected successfully", true, GlobalResponse.APIV),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new GlobalResponse(GlobalResponse.APIV, "201", false, "Teller not successfully rejected"),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404",
					"An Exception occurred while attempting to reject the teller", false, GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/removeTeller")
	public ResponseEntity<?> removeTeller(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
			Teller teller =  new Gson().fromJson(decryptedData,Teller.class);

			int updates = tellerService.removeTeller(teller.getDeletedBy(), teller.getCustomerId());
			if (updates > 0) {
				return new ResponseEntity<>(
						new GlobalResponse("000", "Teller removed successfully", true, GlobalResponse.APIV),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new GlobalResponse(GlobalResponse.APIV, "201", false, "Teller not removed successfully"),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404",
					"An Exception occurred while attempting to remove the teller", false, GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/obtainTellerDetails")
	public ResponseEntity<?> obtainTellerDetails(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) throws NoSuchAlgorithmException, IOException {
		String responsePayload="";
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
			Teller teller =  new Gson().fromJson(decryptedData,Teller.class);
			System.out.println("teller id" + teller.getTellerId());
			Teller cust = tellerService.checkTeller(teller.getTellerId());
			if (cust != null) {
				TellerResponse tellerResponse = new TellerResponse("000", "teller found", true, GlobalResponse.APIV, cust);

				String jsonObj = CommonFunctions.convertPojoToJson(tellerResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			} else {
				GlobalResponse globalResponse = new GlobalResponse(GlobalResponse.APIV, "201", false, "teller not enrolled");
				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}

	@PostMapping(value = "/approveRemoveTeller")
	public ResponseEntity<?> approveRemoveTeller(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) throws NoSuchAlgorithmException, IOException {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
			Teller teller =  new Gson().fromJson(decryptedData,Teller.class);
			int updates = tellerService.approveRemoveTeller(teller.getCustomerId());

			GlobalResponse globalResponse=null;
			if (updates > 0) {
				 globalResponse = new GlobalResponse("000", "removal of teller " + teller.getCustomerId() + " approved successfully", true, GlobalResponse.APIV);
			} else {
				 globalResponse = new GlobalResponse(GlobalResponse.APIV, "201", false, "removal of teller " + teller.getCustomerId() + " not approved successfully");
			}
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

	@PostMapping(value = "/rejectRemoveTeller")
	public ResponseEntity<?> rejectRemoveTeller(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encTeller) throws NoSuchAlgorithmException, IOException {
		String responsePayload="";
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encTeller);
			Teller teller =  new Gson().fromJson(decryptedData,Teller.class);

			int updates = tellerService.rejectRemoveTeller(teller.getCustomerId());
			GlobalResponse globalResponse =null;
			if (updates > 0) {
				 globalResponse = new GlobalResponse("000", "removal of teller " + teller.getCustomerId() + " rejected successfully", true, GlobalResponse.APIV);
			} else {
				 globalResponse = new GlobalResponse(GlobalResponse.APIV, "201", false, "removal of teller " + teller.getCustomerId() + " not rejected successfully");
			}
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

	@GetMapping("/previewStaff")
	public ResponseEntity<?> previewStaff(@RequestHeader HttpHeaders httpHeaders,
										  @RequestParam("FromDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encFromDate,
			@RequestParam(value = "ToDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encToDate,
			@RequestParam(value = "enrolledType") String encEnrolledType, @RequestParam(value = "branchCode") String encBranchCode,
                                          @RequestParam(value = "groupid") String encGroupId) throws NoSuchAlgorithmException, IOException {

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decFromDate = aeSsecure.integratedDataDecryption(key,encFromDate);
		String decToDate = aeSsecure.integratedDataDecryption(key,encToDate);
		String decEnrolledType = aeSsecure.integratedDataDecryption(key,encEnrolledType);
		String decBranchCode = aeSsecure.integratedDataDecryption(key,encBranchCode);
		String decGroupId = aeSsecure.integratedDataDecryption(key,encGroupId);

		Date fromDate =  new Gson().fromJson(decFromDate,Date.class);
		Date toDate =  new Gson().fromJson(decToDate,Date.class);
		String enrolledType =  new Gson().fromJson(decEnrolledType,String.class);
		String branchCode =  new Gson().fromJson(decBranchCode,String.class);
		String groupId =  new Gson().fromJson(decGroupId,String.class);
		try {
            List<TellerToDisplay> staff;
            UserGroup userGroup = userGroupService.getRightCode(Long.valueOf(groupId));
            // the system administrators to be able to view all the reports
            if (userGroup.getGroupCode().equalsIgnoreCase("G001")
                    || userGroup.getGroupCode().equalsIgnoreCase("G002")
                    || userGroup.getGroupCode().equalsIgnoreCase("G003")
                    || userGroup.getGroupCode().equalsIgnoreCase("G004")) {
                staff = tellerService.getEnrolledStaff(fromDate, toDate, enrolledType);
                log.info("",fromDate);
                log.info("",toDate);
                log.info("",enrolledType);
                log.info("",branchCode);
            }else{
                // other system users to view reports based on their branches
            log.info("fromDate",fromDate);
            log.info("toDate",toDate);
            log.info("enrolledType",enrolledType);
            log.info("branchCode",branchCode);
                staff = tellerService.getEnrolledStaffByBranch(fromDate, toDate, enrolledType, branchCode);
            }
			GlobalResponse globalResponse =null;
			if (staff.size() > 0) {
				 globalResponse = new GlobalResponse(GlobalResponse.APIV, "000", true, "staff found", new HashSet<>(staff));
			}else
			 globalResponse = new GlobalResponse(GlobalResponse.APIV, "000", false, "staff not found", new HashSet<>(staff));

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing staff details request", false,
					GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}
}
