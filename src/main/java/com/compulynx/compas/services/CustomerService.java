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
import java.util.Map;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import com.compulynx.compas.models.extras.CustomersToApproveDetach;

import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.extras.ConvertedCustomerStaff;
import com.compulynx.compas.models.extras.CustomerToDispaly;
import com.compulynx.compas.models.extras.CustomerWaived;
import com.compulynx.compas.models.extras.CustomersToApprove;
import com.compulynx.compas.models.extras.MatchingTeller;
import com.compulynx.compas.models.t24Models.CustomerDetails;
import com.compulynx.compas.models.t24Models.CustomerReqObject;
import com.compulynx.compas.models.t24Models.CustomerStaffUpdateRes;
import com.compulynx.compas.models.t24Models.T24UpdateParams;
import com.compulynx.compas.models.t24Models.T24UpdateReqObject;
import com.compulynx.compas.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.compulynx.compas.customs.CommonFunctions;

@Service
public class CustomerService {
	
	 private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
	
	private static HttpsURLConnection httpsURLConnection;
	private static BufferedReader reader;
	private static String line="";

	@Autowired
	private Environment env;

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	
	
	private AESsecure aeSsecure;
	
	@Autowired
	private Auth2T24TokenService auth2t24TokenService;
	@Autowired
	private EncryptionPayloadResp encryptionPayloadResp;
	Gson gson = new Gson();

	public List<Customer> getCustomers() {
		// TODO Auto-generated method stub
		return customerRepository.findAll();
	}

	public Customer checkCustomer(String gifNumber, String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.findCustomerByCustomerId(gifNumber, customerId);
	}

//		Optional<Customer> byCustomerId = customerRepository.findByCustomerId(customer.getCustomerId());
	//	if(byCustomerId.isPresent()){
		//	log.info("Customer "+customer.getCustomerId() +" already exist!");
		//	log.info("Updating customer details....");
		//	customer.setId(byCustomerId.get().getId());
	//		return customerRepository.save(customer);
	//	}
	//	log.info("Save customer details....");
	//	return customerRepository.save(customer);
	//}

	public Customer upCustomerDetails(Customer customer) {
		Optional<Customer> byCustomerId = customerRepository.findByCustomerId(customer.getCustomerId());
		if(byCustomerId.isPresent()){
			log.info("Customer "+customer.getCustomerId() +" already exist!");
			log.info("Updating customer details....");
			customer.setId(byCustomerId.get().getId());
			return customerRepository.save(customer);
		}
		log.info("Save customer details....");
		
		return customerRepository.save(customer);
	}


	public Customer upCustomerData(Customer customer) {
		Optional<Customer> byCustomerId = customerRepository.findByCustomerId(customer.getCustomerId());
		if (byCustomerId.isPresent()) {
			log.info("Customer " + customer.getCustomerId() + " already exists!");
			log.info("Updating customer details....");
			Customer existingCustomer = byCustomerId.get();
			if (!existingCustomer.getVerified().equals("A")) {
				existingCustomer.setVerified("A");
				// Set verified to "A" if it's not already "A"
				return customerRepository.save(existingCustomer);
			} else {
				return existingCustomer; // No need to update, return existing customer
			}
		} else {
			log.info("Save customer details....");
			customer.setVerified("A"); // Set verified to "A" for new customers
			return customerRepository.save(customer);
		}
	}


	public List<CustomersToApprove> getCustomersToVerify(String branchCode) {
		return customerRepository.getCustomersToApprove(branchCode);
	}

	public List<CustomersToApprove> getCustomersToVerifyAll() {
		return customerRepository.getCustomersToApproveAll();
	}



	public int approveCustomer(int verifiedBy, String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.approveCustomers(verifiedBy, customerId);
	}

	public Customer identifyCustomer(String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.identifyCustomer(customerId);
	}

	public Customer getCustomerToWaive(String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.getCustomerToWaive(customerId);
	}

	public int waiveCustomer(int waivedBy, String customerId,String reason) {
		// TODO Auto-generated method stub
		return customerRepository.waiveCustomer(waivedBy, customerId,reason);
	}

	public List<CustomerWaived> getWaiveCustomers() {
		// TODO Auto-generated method stub
		return customerRepository.getWaiveCustomers();
	}

	public int approveCustomerWaive(String waived, int approvedBy, String reason, String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.approveCustomerWaive(waived, approvedBy,reason, customerId);
	}

	public Customer getMatchedCustomers(String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.getMatchedCustomers(customerId);
	}

	public MatchingTeller getMatchedTellers(String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.getMatchedTellers(customerId);
	}

	public int rejectCustomerWaive(int waivedApprovedBy, String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.rejeCustomerWaive(waivedApprovedBy, customerId);
	}

	public List<CustomerToDispaly> gtBioExemption(Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		System.out.println("from date: " + fromDate);
		System.out.println("to date: " + toDate);
		return customerRepository.gtBioExemption(fromDate, toDate);
	}

	public List<CustomerToDispaly> getBioExemptionByBranch(Date fromDate, Date toDate,
													   String branchCode) {
		return customerRepository.getBioExemptionByBranch(fromDate, toDate, branchCode);
	}

	
	public List<CustomerToDispaly> gtEnrolledCustomers(Date fromDate, Date toDate, String enrolledType) {
		// TODO Auto-generated method stub
		System.out.println("from date: " + fromDate);
		System.out.println("to date: " + toDate);
		System.out.println("enrolled type: " + enrolledType);
		return customerRepository.getEnrolledCustomers(fromDate, toDate, enrolledType);
	}

	public List<CustomerToDispaly> getEnrolledCustomersByBranch(Date fromDate, Date toDate, String enrolledType,
													   String branchCode) {
		return customerRepository.getEnrolledCustomersByBranch(fromDate, toDate, enrolledType, branchCode);
	}
//	public Customer getCustomerToVerify(String mnemonic, String mnemonic2) {
//		// TODO Auto-generated method stub
//		return customerRepository.getCustomerToVerify(mnemonic, mnemonic2);
//	}
//
//	public int updateCustomerStatus(String customerId) {
//		// TODO Auto-generated method stub
//		return customerRepository.updateCustomerDetails(customerId);
//	}
//
//	public int removeCustomerPrints(Long profileId) {
//		// TODO Auto-generated method stub
//		return customerRepository.removeCustomerPrints(profileId);
//	}

//	public int removeCustomerProfile(String customerId) {
//		// TODO Auto-generated method stub
//		return customerRepository.removeCustomerDetails(customerId);
//	}

	public int upgradeCustomerProfile(String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.upgradeCustomerDetails(customerId);
	}

	public int rejectCustomerEnrollment(int rejectedBy, String customerId,String reason) {
		return customerRepository.rejectCustomerEnrollment(rejectedBy, customerId,reason);
	}

	public int deleteCustomer(int deletedBy, String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.deleteCustomer(deletedBy, customerId);
	}

//	public List<Customer> gtEnrolledCustomers(Date fromDate, Date toDate) {
//		// TODO Auto-generated method stub
//		return customerRepository.getAll(fromDate,toDate);
//	}

	public List<CustomersToApproveDetach> getCustomersToApproveDetach() {
		return customerRepository.getCustomersToApproveDetach();
	}

	public int approveRemoveCustomer(String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.approveRemoveCustomer(customerId);
	}

	public int rejectRemoveCustomer(String customerId) {
		// TODO Auto-generated method stub
		return customerRepository.rejectRemoveCustomer(customerId);
	}

	public Customer checkCustomerDeleted(String customerId) {
		return customerRepository.checkCustomerDeleted(customerId);
	}

	public int customerUnDelete(int createdBy, String customerId) {
		return customerRepository.customerUnDelete(createdBy, customerId);
	}
	
	
	public GlobalResponse updateCustomerAndStaff(String url, String customerID,String updateStatus) {
    	log.info("updateCustomerAndStaff called!");
    
    	try {	
    		
    		URL custAndStaffUrl = new URL(url);	
    		URLConnection urlConnection = custAndStaffUrl.openConnection();
        	HttpURLConnection connection = null;
        	StringBuffer customerAndStaffBuffer = new StringBuffer();
			if(url =="" || customerID == null) {
				GlobalResponse resp = new GlobalResponse("404", "error processing request: customerid is missing", false, GlobalResponse.APIV);
				return resp;
			}		   
				//get access  token 
				String accessToken = null;
				try {
					accessToken = auth2t24TokenService.getAccessToken(env);
					log.info("access -- token :: {}",accessToken);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				log.info(accessToken);
				log.info("Exited the get access token");
		       if (urlConnection instanceof HttpsURLConnection) {
		    	   connection = (HttpsURLConnection) urlConnection;
	            } else if(urlConnection instanceof HttpURLConnection) {
	             connection = (HttpURLConnection)urlConnection;
	            }
	            else {
	            	  log.error("urlConnection is not instance of either HttpsURLConnection or HttpURLConnection");
	            	  GlobalResponse resp = new GlobalResponse("404", "urlConnection is not instance of either HttpsURLConnection or HttpURLConnection", false, GlobalResponse.APIV);
	                  return resp;
	            }
			CommonFunctions.disableSslVerification();
			log.info("updateCustomerAndStaff  disabled SSL!");
			
						
			// httpURLConnection = (HttpURLConnection)custAndStaffUrl.openConnection();
			log.info("updateCustomerAndStaff: CONN OPENNED !");
			
			//CommonFunctions.configHttpUrlConnection(httpURLConnection);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json");          
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);
			connection.setDoOutput(true);
			connection.setConnectTimeout(20000);	
	        OutputStream getCustAndStaffOs = connection.getOutputStream();
	        log.info("updateCustomerAndStaff OUTPUT STRING RETURNED ");
	        
	        OutputStreamWriter getCustAndStaffOsw = new OutputStreamWriter(getCustAndStaffOs, "UTF-8");  
	        
	        //T24UpdateReqObject object = new T24UpdateReqObject(env.getProperty("cobankingAuthName"),env.getProperty("cobankingAuthPass"),new T24UpdateParams(customerID,updateStatus));
	        T24UpdateReqObject object = new T24UpdateReqObject("string","string",
	        		       new T24UpdateParams(customerID,updateStatus));
  
	        String json = new Gson().toJson(object);
	        
	        if(json != null) {
	        	getCustAndStaffOsw.write(json);						
	        	getCustAndStaffOsw.flush();
	        	getCustAndStaffOsw.close();  
	        } 
	        			       
			int status = connection.getResponseCode();
			
			log.info("UpdateCustomerAndStaff!!!!!!!!!!!!!!!!!!!!!!!!! "+status);
			
			if(status == 200) {
				
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));						
				while((line = reader.readLine()) != null) {
					customerAndStaffBuffer.append(line);
				}
				reader.close();
				connection.disconnect();	
				ObjectMapper mapper = new ObjectMapper();
				CustomerStaffUpdateRes resBody = mapper.readValue(customerAndStaffBuffer.toString(), CustomerStaffUpdateRes.class);	
				System.out.println("customerAndStaffBuffer :::::::::::::::::::::::"+resBody.toString());
				if(resBody.getErrorCode() != null && resBody.getErrorCode() != "") {
					System.out.println("IF THERE'S AN ERROR :::::::::::::::");
					GlobalResponse resp = new GlobalResponse(resBody.getErrorCode(), resBody.getErrorMessage(), false, GlobalResponse.APIV);
					 return resp; 
				}
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+resBody.getPayload().toString());
				//(String respCode, String respMessage, boolean status, String version)
				GlobalResponse resp = new GlobalResponse("200",resBody.getPayload().getStatus(), false, GlobalResponse.APIV);
				System.out.println("SUCCESS:::::::::::::::::::::::"+resp.getRespCode() +" RESP MESSAGE:::::"+resp.getRespMessage());
				 return resp;
					
			}else {
				System.out.println("DID NOT RETURN 200::::::::::::::::::::::::::::::::::::");
				GlobalResponse resp = new GlobalResponse("500", "T24 did not return a valid response!", false, GlobalResponse.APIV);
				return resp;
			}	
	
		}catch(MalformedURLException e) {
			e.printStackTrace();
			GlobalResponse resp = new GlobalResponse("500", "T24 endpoints not reachable", false, GlobalResponse.APIV);
			return resp;
			
		}catch(IOException ev) {
			ev.printStackTrace();
			GlobalResponse resp = new GlobalResponse("500", "T24 endpoints not reachable", false, GlobalResponse.APIV);
			return resp;
		}catch (Exception exception) {
			exception.printStackTrace();
			GlobalResponse resp = new GlobalResponse("500", "T24 endpoints not reachable", false, GlobalResponse.APIV);
			return resp;
		} 	
    }

	public ResponseEntity<?> t24CustomerInquiry(Customer customer) throws NoSuchAlgorithmException, IOException {
		log.error("t24CustomerInquiry!");
		
		String customerInqEndpoint = env.getProperty("customerInqEndpoint");
		URL customerDetailsUrl = new URL(customerInqEndpoint);
		HttpURLConnection connection = null;
		URLConnection urlConnection = customerDetailsUrl.openConnection();
		StringBuffer getCustomerDetailsBuffer = new StringBuffer();
		
		//get access  token

		try {
			
			log.info("Inside customer inquiry :: "+customerInqEndpoint);
			//get access  token 
			String accessToken = null;
			try {
				accessToken = auth2t24TokenService.getAccessToken(env);
				log.info("access -- token :: {}",accessToken);
			} catch (Exception e) {
				  log.error("IOException inside customerservice => getaccesstoken: ", e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			log.info(accessToken);
			log.info("Exited the get access token");
		     if (urlConnection instanceof HttpsURLConnection) {
		    	   connection = (HttpsURLConnection) urlConnection;
	            } else if(urlConnection instanceof HttpURLConnection) {
	             connection = (HttpURLConnection)urlConnection;
	            }
	            else {
	            	  log.error("urlConnection is not instance of either HttpsURLConnection or HttpURLConnection");
	              GlobalResponse resp = new GlobalResponse("404", "urlConnection is not instance of either HttpsURLConnection or HttpURLConnection", false, GlobalResponse.APIV);
	              String jsonObj = CommonFunctions.convertPojoToJson(resp);
	    		  encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
	    		  HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
	    	      return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
	            }
			CommonFunctions.disableSslVerification();	
			connection.setRequestMethod("POST");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json");          
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);  // Add the access token here
			connection.setDoOutput(true);
			connection.setConnectTimeout(20000);

	        OutputStream getCustDetsOs = connection.getOutputStream();
	        
	      
	        OutputStreamWriter getCustDetsOsw = new OutputStreamWriter(getCustDetsOs, "UTF-8");  
	        
	        com.compulynx.compas.models.t24Models.Customer cust = new com.compulynx.compas.models.t24Models.Customer();
	        cust.setMnemonic(customer.getMnemonic());
	      //  CustomerReqObject custReqObj = new CustomerReqObject(env.getProperty("cobankingAuthName"),env.getProperty("cobankingAuthPass"),cust);
	        CustomerReqObject custReqObj = new CustomerReqObject("string","string",cust);

	        String getCustDetReqString = CommonFunctions.convertPojoToJson(custReqObj);
	        if(getCustDetReqString != null) {
	        	getCustDetsOsw.write(getCustDetReqString);						
	        	getCustDetsOsw.flush();
	        	getCustDetsOsw.close();  
	        } 
	        			       
			int status = connection.getResponseCode();
			System.out.println("STATUS FROM T24 =>> t24CustomerInquiry !!!!!!!!!!!!!! "+status);
			
			if(status == 200) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));						
				while((line = reader.readLine()) != null) {
					getCustomerDetailsBuffer.append(line);
				}
				reader.close();
				connection.disconnect();
				
				ObjectMapper mapper = new ObjectMapper();
				CustomerDetails custDetails = mapper.readValue(getCustomerDetailsBuffer.toString(), CustomerDetails.class);	
				log.error("Customer inquiry successfull!");
				//responsePayload = aeSsecure.encrypt(gson.toJson(custDetails).toString());
				String jsonObj = CommonFunctions.convertPojoToJson(custDetails);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
				//return new ResponseEntity<>(responsePayload, HttpStatus.OK);
			}else if(String.valueOf(status).startsWith("5")){
				GlobalResponse resp = new GlobalResponse("500", "T24 endpoint is unreachable", false, GlobalResponse.APIV);

				//responsePayload = aeSsecure.encrypt(gson.toJson(resp).toString());
				String jsonObj = CommonFunctions.convertPojoToJson(resp);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

			}
			else {
				log.error("Customer not found!");
				GlobalResponse resp = new GlobalResponse("404", "Customer not found!", false, GlobalResponse.APIV);
				//responsePayload = aeSsecure.encrypt(gson.toJson(resp).toString());

				String jsonObj = CommonFunctions.convertPojoToJson(resp);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

				//return new ResponseEntity<>(responsePayload, HttpStatus.OK);
			}
	}catch(Exception e) {
		       log.error("IOException: ", e.getMessage());
		    GlobalResponse resp = new GlobalResponse("500", "T24 endpoint is unreachable", false, GlobalResponse.APIV);

			//responsePayload = aeSsecure.encrypt(gson.toJson(resp).toString());
			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			//return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		//return new ResponseEntity<>(responsePayload, HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
	}

	public List<ConvertedCustomerStaff> GtConvertedCustomersStaff(Date fromDate, Date toDate, String enrolledType) {
		// TODO Auto-generated method stub
		return customerRepository.GtConvertedCustomersStaff(fromDate, toDate, enrolledType);
	}

	public List<ConvertedCustomerStaff> GtConvertedCustomersStaffByBranch(Date fromDate, Date toDate,
			String enrolledType, String branchCode) {
		// TODO Auto-generated method stub
		return customerRepository.GtConvertedCustomersStaffByBranch(fromDate, toDate, enrolledType, branchCode);
	}

}

