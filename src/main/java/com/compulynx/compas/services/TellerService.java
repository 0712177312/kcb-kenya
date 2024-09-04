package com.compulynx.compas.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import com.compulynx.compas.customs.responses.GeneralResponse;
import com.compulynx.compas.models.extras.TellersToApproveDetach;
import com.compulynx.compas.models.t24Models.Staff;
import com.compulynx.compas.models.t24Models.StaffDetails;
import com.compulynx.compas.models.t24Models.StaffReqObject;

import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.google.gson.Gson;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.models.Teller;
import com.compulynx.compas.models.extras.TellerToApprove;
import com.compulynx.compas.models.extras.TellerToDisplay;
import com.compulynx.compas.repositories.TellerRepository;
import com.compulynx.compas.repositories.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TellerService {
	private static final Logger logger = LoggerFactory.getLogger(CommonFunctions.class);
	private static HttpsURLConnection httpsURLConnection;
	private static BufferedReader reader;
	private static String line="";

	@Autowired
	private Environment env;
	@Autowired
	private TellerRepository tellerRepository;
	@Autowired
	private AESsecure aeSsecure;
	@Autowired
	private Auth2T24TokenService auth2t24TokenService;
	@Autowired
	private EncryptionPayloadResp encryptionPayloadResp;
	Gson gson = new Gson();

	public Teller checkTeller(String tellerId) {
		// TODO Auto-generated method stub
		return tellerRepository.checkTeller(tellerId);
	}

	public GeneralResponse upTellerDetails(Teller teller) {
		Optional<Teller> optionalTeller = tellerRepository.findByCustomerId(teller.getCustomerId());
		if(optionalTeller.isPresent())
			return new GeneralResponse("201","Record already exist!");
		tellerRepository.save(teller);
		return new GeneralResponse("200","Record updated successfully!");
	}

	public List<Teller> getTellers() {
		// TODO Auto-generated method stub
		return tellerRepository.findAll();
	}

	public Teller getTeller(String tellr) {
		// TODO Auto-generated method stub
		return tellerRepository.getTellerDetails(tellr);
	}

	public List<Teller> getBranchTellers(String branch) {
		// TODO Auto-generated method stub
		return tellerRepository.getBranchTellers(branch);
	}

	public int approveTeller(int verifiedBy, String customerId) {
		// TODO Auto-generated method stub
		return tellerRepository.approveTellers(verifiedBy, customerId);
	}

	// Filtered by branch
	public List<TellerToApprove> getTellersToVerify(String branchCode) {
		// TODO Auto-generated method stub
		return tellerRepository.getTellersToApprove(branchCode);
	}

	// All without filters
	public List<TellerToApprove> getTellersToVerifyAll() {
		// TODO Auto-generated method stub
		return tellerRepository.getTellersToApproveAll();
	}

//	public Teller getTellerToVerify(String customerId, String mnemonic) {
//		// TODO Auto-generated method stub
//		return tellerRepository.getTellerToVerify(customerId, mnemonic);
//	}
//
//	public int updateTellerStatus(String customerId) {
//		// TODO Auto-generated method stub
//		return tellerRepository.updateTellerDetails(customerId);
//	}
//
//	public int removeTellerPrints(Long profileId) {
//		// TODO Auto-generated method stub
//		return this.tellerRepository.removeTellerPrints(profileId);
//	}
//
//	public int removeTellerDetails(String customerId) {
//		// TODO Auto-generated method stub
//		return this.tellerRepository.removeTellerDetails(customerId);
//	}

	public int rejectTellerApproval(int rejectedBy, String customerId,String reason ) {
		return tellerRepository.rejectTellerApproval(rejectedBy, customerId,reason);
	}

	public int removeTeller(int deletedBy, String customerId) {
		return tellerRepository.removeTeller(deletedBy, customerId);
	}

	public int convertStaffToCustomer(String customerId) {
		return tellerRepository.convertStaffToCustomer(customerId);
	}

	public Teller checkStaffApproved(String tellerId) {
		return tellerRepository.checkStaffApproved(tellerId);
	}

	public Teller checkStaffDeleted(String tellerId) {
		return tellerRepository.checkStaffDeleted(tellerId);
	}

	public int staffUnDelete(int createdBy, String tellerId) {
		return tellerRepository.staffUnDelete(createdBy, tellerId);
	}

	public List<TellersToApproveDetach> getTellersToApproveDetach() {
		return tellerRepository.getTellersToApproveDetach();
	}

	public int approveRemoveTeller(String customerId) {
		return tellerRepository.approveRemoveTeller(customerId);
	}

	public int rejectRemoveTeller(String customerId) {
		return tellerRepository.rejectRemoveTeller(customerId);
	}

	public List<TellerToDisplay> getEnrolledStaff(Date fromDate, Date toDate, String enrolledType) {
		logger.info("",fromDate);
	     logger.info("",toDate);
	     logger.info("",enrolledType);
		return tellerRepository.getEnrolledStaff(fromDate, toDate, enrolledType);
	}

    public List<TellerToDisplay> getEnrolledStaffByBranch(Date fromDate, Date toDate, String enrolledType, String branchCode) {
        return tellerRepository.getEnrolledStaffByBranch(fromDate, toDate, enrolledType, branchCode);
    }

	public ResponseEntity<?> t24StaffInquiry(Staff staf) throws NoSuchAlgorithmException, IOException {
		String responsePayload ="";
		String staffInqEndpoint = env.getProperty("staffInqEndpoint");
		URL staffDetailsUrl = new URL(staffInqEndpoint);
		URLConnection urlConnection =staffDetailsUrl.openConnection();
		HttpURLConnection connection = null;
		try {
		
			String accessToken = null;
			try {
				accessToken = auth2t24TokenService.getAccessToken(env);
				logger.info("access -- token :: {}",accessToken);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			logger.info(accessToken);
			logger.info("Exited the get access token");
			 if (urlConnection instanceof HttpsURLConnection) {
		    	   connection = (HttpsURLConnection) urlConnection;
	            } else if(urlConnection instanceof HttpURLConnection) {
	             connection = (HttpURLConnection)urlConnection;
	            }
	            else {
	            	  logger.error("urlConnection is not instance of either HttpsURLConnection or HttpURLConnection");
	            	    GlobalResponse resp = new GlobalResponse("404", "urlConnection is not instance of either HttpsURLConnection or HttpURLConnection", false, GlobalResponse.APIV);
	            		String jsonObj = CommonFunctions.convertPojoToJson(resp);
	    				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
	    				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
	    				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
	            }
			CommonFunctions.disableSslVerification();
			StringBuffer getStaffDetailsBuffer = new StringBuffer();	
			connection.setRequestMethod("POST");	
			connection.setRequestProperty("accept", "*/*");
			connection.addRequestProperty("Content-Type", "application/json");          
			connection.addRequestProperty("Authorization", "Bearer " + accessToken);
			connection.setDoOutput(true);
			connection.setConnectTimeout(20000);
	        OutputStream getCustDetsOs = connection.getOutputStream();
	        OutputStreamWriter getCustDetsOsw = new OutputStreamWriter(getCustDetsOs, "UTF-8");  
	        
	        Staff staff = new Staff();
	        staff.setId(staf.getId());
	        //StaffReqObject staffReqObj = new StaffReqObject(env.getProperty("cobankingAuthName"),env.getProperty("cobankingAuthPass"),staff);
	        StaffReqObject staffReqObj = new StaffReqObject("string","string",staff);

	        String getStaffDetReqString = CommonFunctions.convertPojoToJson(staffReqObj);
	        if(getStaffDetReqString != null) {
	        	getCustDetsOsw.write(getStaffDetReqString);						
	        	getCustDetsOsw.flush();
	        	getCustDetsOsw.close();  
	        } 
	        			       
			int status = connection.getResponseCode();
			System.out.println("STATUS FROM T24 =>> t24StaffInquiry !!!!!!!!!!!!!! "+status);
			
			if(status == 200) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));						
				while((line = reader.readLine()) != null) {
					getStaffDetailsBuffer.append(line);
				}
				reader.close();
				connection.disconnect();
				
				ObjectMapper mapper = new ObjectMapper();
				StaffDetails staffDetails = mapper.readValue(getStaffDetailsBuffer.toString(), StaffDetails.class);
				
				//responsePayload = aeSsecure.encrypt(gson.toJson(staffDetails).toString());
				String jsonObj = CommonFunctions.convertPojoToJson(staffDetails);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

				//return new ResponseEntity<>(responsePayload, HttpStatus.OK);

			}else {
				GlobalResponse resp = new GlobalResponse("404", "Staff not found!", false, GlobalResponse.APIV);
				//responsePayload = aeSsecure.encrypt(gson.toJson(resp).toString());
				String jsonObj = CommonFunctions.convertPojoToJson(resp);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

				//return new ResponseEntity<>(responsePayload, HttpStatus.NOT_FOUND);
			}	

	}catch (Exception exception) {
		System.out.println("Exception "+exception.getMessage());	
		Log.error(exception.getMessage());
		GlobalResponse resp = new GlobalResponse("404", "T24 endpoint is unreachable", false, GlobalResponse.APIV);
			//responsePayload = aeSsecure.encrypt(gson.toJson(resp).toString());

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

			//return new ResponseEntity<>(responsePayload, HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
	}

}
