package com.compulynx.compas.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.compulynx.compas.customs.responses.GlobalResponse2;
import com.compulynx.compas.mail.EmailSender;
import com.compulynx.compas.models.Channel;
import com.compulynx.compas.models.extras.*;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.compulynx.compas.services.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import com.compulynx.compas.customs.HttpRestProccesor;
import com.compulynx.compas.customs.responses.CustomerResponse;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.Teller;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.services.CustomerService;
import com.compulynx.compas.services.TellerService;
import com.compulynx.compas.services.UserGroupService;

@RestController
@RequestMapping(value = Api.REST)
public class CustomerController {

	private static final Logger log = LoggerFactory.getLogger(TellerController.class);
	
	private static HttpsURLConnection httpsURLConnection;
	private static BufferedReader reader;
	private static String line="";

	@Autowired
	private Environment env;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private TellerService tellerService;
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
	//Gson gson = new Gson();
	String dateFormat = "yyyy-MM-dd HH:mm:ss";
	Gson gson = new GsonBuilder()
			.setDateFormat(dateFormat)
			.create();
	
	@PostMapping("customer_inquiry")
	public ResponseEntity<?> getCustomerFromT24(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {

		//Customer customer =  new Gson().fromJson(aeSsecure.decrypt(encCustomer),Customer.class);

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
		Customer customer =  new Gson().fromJson(decryptedData,Customer.class);

		//String responsePayload="";
		if(customer.getMnemonic() =="" || customer.getMnemonic() == null) {
			GlobalResponse resp = new GlobalResponse("404", "error processing request: staffid is missing", false, GlobalResponse.APIV);
			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
		return customerService.t24CustomerInquiry(customer);
	}
	@GetMapping(value = "/gtCustomers")
	public ResponseEntity<?> getCustomers() throws NoSuchAlgorithmException, IOException {
		try {
			List<Customer> customers = customerService.getCustomers();
			if (customers.size() > 0) {
				GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers found",
						customers);

				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "201", false, "no customers found",
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}

	@PostMapping(value = "/getMatchedCustomers")
	public ResponseEntity<?> identifyCustomers(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomers) throws NoSuchAlgorithmException, IOException {
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomers);
		Type listType = new TypeToken<List<Customer>>() {}.getType();
		List<Customer> customers =  new Gson().fromJson(decryptedData,listType);
		try {
			List<Customer> custs = new ArrayList<>();
			for (Customer cus : customers) {
				Customer cust = customerService.getMatchedCustomers(cus.getCustomerId());
				if (cust != null) {
					custs.add(cust);
				} else {
					MatchingTeller teller = customerService.getMatchedTellers(cus.getCustomerId());
					if (teller != null) {
						Customer tel = new Customer();
						tel.setId(teller.getId());
						tel.setCustomerId(teller.getCustomerId());
						tel.setCustomerIdNumber(teller.getCustomerIdNumber());
						tel.setCustomerName(teller.getCustomerName());
						tel.setGender(teller.getGender());
						tel.setPhoneNumber(teller.getPhoneNumber());
						tel.setCountry(teller.getCountry());
						custs.add(tel);
					}
				}
			}
			GlobalResponse2 globalResponse = null;
			if (customers.size() > 0) {
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers found", custs);
			}else
			 globalResponse =new GlobalResponse2(GlobalResponse.APIV, "201", false, "no customers found", custs);

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

	@PostMapping(value = "/identifyCustomer")
	public ResponseEntity<?> identifyCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
			Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
			Customer cust = customerService.identifyCustomer(customer.getCustomerId());
			if (!(cust.equals(null))) {
				CustomerResponse customerResponse = new CustomerResponse("000", "customer", true, GlobalResponse.APIV, cust);
				String jsonObj = CommonFunctions.convertPojoToJson(customerResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			CustomerResponse customerResponse = new CustomerResponse("201", "customer not found", false, GlobalResponse.APIV, cust);
			String jsonObj = CommonFunctions.convertPojoToJson(customerResponse);
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

	@PostMapping(value = "/checkCustomer")
	public ResponseEntity<?> checkCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
			Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
			Customer cust = customerService.checkCustomer(customer.getMnemonic(), customer.getMnemonic());

			if (cust != null) {
				return new ResponseEntity<>(new GlobalResponse("000", "customer found", true, GlobalResponse.APIV),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV, "201", false, "no customers found"),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/obtainCustomerDetails")
	public ResponseEntity<?> obtainCustomerDetails(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {

		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
			Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
			Customer cust = customerService.checkCustomer(customer.getMnemonic(), customer.getMnemonic());

			if (cust != null) {
				CustomerResponse customerResponse = new CustomerResponse("000", "customer found", true, GlobalResponse.APIV, cust);

				String jsonObj = CommonFunctions.convertPojoToJson(customerResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			} else {
				CustomerResponse customerResponse = new CustomerResponse("201", "customer not found", false, GlobalResponse.APIV, cust);

				String jsonObj = CommonFunctions.convertPojoToJson(customerResponse);
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

	@PostMapping({ "/upCustomerDetails" })
	public ResponseEntity<?> upCustomerDetails(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
			Customer customer =  new Gson().fromJson(decryptedData,Customer.class);

			Customer cust = null;
			int customerUndeleted = 0;
			if (customerService.checkCustomerDeleted(customer.getCustomerId()) != null) {
				customerUndeleted = customerService.customerUnDelete(customer.getCreatedBy(), customer.getCustomerId());
			} else {
				cust = customerService.upCustomerDetails(customer);
			}
			if (cust != null || customerUndeleted > 0) {

				return new ResponseEntity(new GlobalResponse("000", "customer found", true, "1.0.0"), HttpStatus.OK);
			}

			return new ResponseEntity(new GlobalResponse("1.0.0", "201", false, "no customers found"), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error in proccesing ", e);
			GlobalResponse resp = new GlobalResponse("404", "error processing request", false, "1.0.0");

			return new ResponseEntity(resp, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/approveCustomer")
	public ResponseEntity<?> approveCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {

		log.info("/approveCustomer called");
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
		Customer customer =  new Gson().fromJson(decryptedData,Customer.class);

		try {
            String t24Url = env.getProperty("tserver");

            String customerId = customer.getCustomerId();
            log.info("update url for " + t24Url);
           log.info("verified by ::: {} ",customer.getVerifiedBy());
			System.out.println("update url for " + t24Url);
			
			GlobalResponse response = customerService.updateCustomerAndStaff(t24Url, customerId,"TRUE");            

			log.info("T24  response code " + response.getRespCode());
            log.info("T24  response message" + response.getRespMessage());
            
            if(!response.getRespCode().equalsIgnoreCase("200")){
            	log.info("T24 with status 400 " + response.getRespMessage());
				GlobalResponse globalResponse = new GlobalResponse(response.getRespCode(), response.getRespMessage(), false,GlobalResponse.APIV);

				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
            }
        } catch (Exception e) {
            log.error("Error in proccesing ", e);
			GlobalResponse globalResponse = new GlobalResponse("500", "HpptRestProcessor Exception", false, GlobalResponse.APIV);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
        }
        log.info("T24 success response ");

		try {
			int cust = customerService.approveCustomer(customer.getVerifiedBy(), customer.getCustomerId());
			log.info("Compass for customer approval ");

			if (cust > 0) {
				log.info("Compas customer found! ");
				String recipient = customer.getEmail();
				if(recipient == null) {
					log.info("Email for " + customer.getCustomerName() + " is null");
				}else if(!recipient.contains("@")){
					log.info("Email for " + customer.getCustomerName() + " is not available");
				}else {
					String subject = "Biometric Details of Customer Captured";
					String emailContent = "Dear " + customer.getCustomerName() + ", your biometric details have been successfully registered. For any queries please call 0711087000 or 0732187000.";
					emailSender.sendEmail(recipient, subject, emailContent);
					log.info("Email to customer scheduled to be sent to " + customer.getCustomerName());
				}
				String smsUrl = env.getProperty("smsUrl");
                String customerName = customer.getCustomerName();
                String phoneNumber = customer.getPhoneNumber();
                String smsApiUsername = env.getProperty("smsApiUsername");
                String smsApiPassword = env.getProperty("smsApiPassword");
                String getResponse = HttpRestProccesor.sendGetRequest(smsUrl, "sms", customerName, phoneNumber, smsApiUsername, smsApiPassword);
				log.info("SMS Get Request Response is: "+  getResponse);

				GlobalResponse globalResponse = new GlobalResponse("000",
						"customer  " + customer.getCustomerId() + " verified successfully",true,GlobalResponse.APIV);

				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			GlobalResponse globalResponse = new GlobalResponse("404", "no customers found", false,GlobalResponse.APIV);
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}

	@PostMapping(value = "/customersToApprove")
	public ResponseEntity<?> getCustomersToApprove(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
		Customer customer =  new Gson().fromJson(decryptedData,Customer.class);

		try {
			System.out.println("Branch Code: " + customer.getBranchCode());
			System.out.println("Right ID: " + customer.getVerifiedBy());
			UserGroup userGroup = userGroupService.getRightCode(Long.valueOf(customer.getVerifiedBy()));
			List<CustomersToApprove> customers = null;
			if (userGroup.getGroupCode().equalsIgnoreCase("G001")
                    || userGroup.getGroupCode().equalsIgnoreCase("G002") 
                    || userGroup.getGroupCode().equalsIgnoreCase("G003")
                    || userGroup.getGroupCode().equalsIgnoreCase("G004")) {
				customers = customerService.getCustomersToVerifyAll();
			} else {
				customers = customerService.getCustomersToVerify(customer.getBranchCode());
			}

			GlobalResponse2 globalResponse;
			if (customers.size() > 0) {
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", false, "customers to verify found", customers);

			} else
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "201", false, "no customers to verify found", customers);

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
	@GetMapping(value = "/customersToApproveDetach")
	public ResponseEntity<?> getCustomersToApproveDetach() throws NoSuchAlgorithmException, IOException {
		try {
			List<CustomersToApproveDetach> customers = customerService.getCustomersToApproveDetach();
			GlobalResponse2 globalResponse;
			if (customers.size() > 0)
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers to approve detach found", customers);
			 else
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "201", false, "no customers to approve detach found", customers);

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

	@PostMapping(value = "/gtCustomerToWaive")
	public ResponseEntity<?> getCustomerToWaive(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
		Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
		try {
			Customer cust = customerService.checkCustomer(customer.getCustomerId(), customer.getCustomerId());
			System.out.println("customer###" + cust.getVerified());
			if (cust.getWaived().equalsIgnoreCase("N")) {
				CustomerResponse customerResponse = new CustomerResponse("201",
						"customer with specified id is not yet VERIFIED, " + " kindly verify to proceed!", true,
						GlobalResponse.APIV, cust);

				String jsonObj = CommonFunctions.convertPojoToJson(customerResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			if (cust.getWaived().equalsIgnoreCase("W")) {
				CustomerResponse customerResponse = new CustomerResponse("201", "customer with specified id is already waived",
						false, GlobalResponse.APIV, cust);

				String jsonObj = CommonFunctions.convertPojoToJson(customerResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			if (cust.getWaived().equalsIgnoreCase("A")) {
				CustomerResponse customerResponse = new CustomerResponse("201",
						"customer with specified id is already waived and approved", false, GlobalResponse.APIV, cust);

				String jsonObj = CommonFunctions.convertPojoToJson(customerResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			CustomerResponse customerResponse = new CustomerResponse("201", "customer not found", false, GlobalResponse.APIV, cust);
			String jsonObj = CommonFunctions.convertPojoToJson(customerResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "customer with specified id not found", false,
					GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}

	@GetMapping(value = "/gtWaivedCustomers")
	public ResponseEntity<?> getWaivedCustomers() throws NoSuchAlgorithmException, IOException {
		try {
			List<CustomerWaived> customers = customerService.getWaiveCustomers();
			if (customers.size() > 0) {
				GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers found",
						customers);

				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "201", false, "no customers found",
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

	@PostMapping(value = "/waiveCustomer")
	public ResponseEntity<?> waiveCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
			Customer customer =  new Gson().fromJson(decryptedData,Customer.class);

			int cust = customerService.waiveCustomer(customer.getWaivedBy(), customer.getCustomerId(),customer.getReason());
			if (cust > 0) {
				return new ResponseEntity<>(
						new GlobalResponse("000", "customer waived successfully", true, GlobalResponse.APIV),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new GlobalResponse(GlobalResponse.APIV, "201", false, "failed to update customer details"),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing customer details request", false,
					GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/approveCustomerWaive")
	public ResponseEntity<?> approveCustomerWaive(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
		Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
		try{
			log.info("Update T24 BIO Exemption ");
			String t24Url = env.getProperty("tserver");

			log.info("update url for " + t24Url);
			System.out.println("gif" + customer.getCustomerId());
			log.info("Action :::: {}",customer.getAction());
			GlobalResponse response=null;
			
			// Added this block - to check either approve or un-waive
            if(customer.getAction().equalsIgnoreCase("W")) {

			     response = customerService.updateCustomerAndStaff(t24Url, customer.getCustomerId(),"EXEMPTED");
            }else if(customer.getAction().equalsIgnoreCase("N")) {
            	 response = customerService.updateCustomerAndStaff(t24Url, customer.getCustomerId(),"TRUE");
            }
            // End of the block
			log.info("T24 response response " + response.getRespMessage());
			if(response.getRespCode() != "200"){
				log.info("Update T24 BIO Exemption FAILED ");
				log.info("T24 response Code: " + response.getRespCode());
				return new ResponseEntity<>(new GlobalResponse(response.getRespCode(), response.getRespMessage(), false,GlobalResponse.APIV),
						HttpStatus.OK);
			}
		}catch(Exception ex){
			log.error("Update Customer Bio Exemption", ex.getMessage());
			return new ResponseEntity<>(new GlobalResponse("500", "T24: HpptRestProcessor Exception", false, GlobalResponse.APIV),
					HttpStatus.OK);
		}

		try {
			log.info("Update COMPAS BIO Exemption Approval request");
			int cust = customerService.approveCustomerWaive(customer.getWaived(), customer.getWaivedApprovedBy(),customer.getReason(),
					customer.getCustomerId());
			if (cust > 0) {
				log.info("Update COMPAS BIO Exemption Approval successfull");
				return new ResponseEntity<>(
						new GlobalResponse("000", "COMPAS: customer details updated successfully", true, GlobalResponse.APIV),
						HttpStatus.OK);
			} else {
				log.info("Update COMPAS BIO Exemption Approval failed");
				return new ResponseEntity<>(
						new GlobalResponse("201","COMPAS: failed to update customer details", false, GlobalResponse.APIV),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("Update COMPAS BIO Exemption Approval server error!");
			GlobalResponse resp = new GlobalResponse("404", " COMPAS: error processing customer details request", false,
					GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/rejectCustomerWaive")
	public ResponseEntity<?> rejectCustomerWaive(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
			Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
			System.out.println("cif" + customer.getCustomerId());
			int cust = customerService.rejectCustomerWaive(customer.getWaivedApprovedBy(), customer.getCustomerId());
			if (cust > 0) {
				return new ResponseEntity<>(
						new GlobalResponse("000", "customer details updated successfully", true, GlobalResponse.APIV),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new GlobalResponse(GlobalResponse.APIV, "201", false, "failed to reject  customer waive"),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing customer details request", false,
					GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/rejectCustomerEnrollment")
	public ResponseEntity<?> rejectCustomerEnrollment(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
			log.info("decryptedData {}",decryptedData);
			Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
			int updates = customerService.rejectCustomerEnrollment(customer.getRejectedBy(), customer.getCustomerId(),customer.getReason1());
			if (updates > 0) {
				return new ResponseEntity<>(
						new GlobalResponse("000", "Customer rejected successfully", true, GlobalResponse.APIV),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new GlobalResponse(GlobalResponse.APIV, "201", false, "Customer not successfully rejected"),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404",
					"An Exception occurred while attempting to reject the customer", false, GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}
	
	
	
	
	@GetMapping("/previewBioExemption")
	public ResponseEntity<?> previewBioExemption(@RequestHeader HttpHeaders httpHeaders,
			@RequestParam("FromDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encFromDate,
			@RequestParam(value = "ToDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encToDate,
			@RequestParam(value = "branchCode") String encBranchCode,
            @RequestParam(value = "groupid") String encGroupId) throws NoSuchAlgorithmException, IOException {

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decFromDate = aeSsecure.integratedDataDecryption(key,encFromDate);
		String decToDate = aeSsecure.integratedDataDecryption(key,encToDate);
		String decBranchCode = aeSsecure.integratedDataDecryption(key,encBranchCode);
		String decGroupId = aeSsecure.integratedDataDecryption(key,encGroupId);

		Date fromDate =  new Gson().fromJson(decFromDate,Date.class);
		Date toDate =  new Gson().fromJson(decToDate,Date.class);
		String branchCode =  new Gson().fromJson(decBranchCode,String.class);
		String groupId =  new Gson().fromJson(decGroupId,String.class);

		try {
			Date toDatePlus1 = CommonFunctions.getOneDayPlusDate(toDate);
			System.out.println("####"+toDatePlus1);
            List<CustomerToDispaly> customers;
            UserGroup userGroup = userGroupService.getRightCode(Long.valueOf(groupId));
            if (userGroup.getGroupCode().equalsIgnoreCase("G001")
                    || userGroup.getGroupCode().equalsIgnoreCase("G002") 
                    || userGroup.getGroupCode().equalsIgnoreCase("G003")
                    || userGroup.getGroupCode().equalsIgnoreCase("G004")) {
                customers = customerService.gtBioExemption(fromDate, toDatePlus1);
            }else{
                customers = customerService.getBioExemptionByBranch(fromDate, toDatePlus1, branchCode);
            }
			GlobalResponse2 globalResponse = null;
			if (customers.size() > 0) {
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers found", customers);
			}else
			 	 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers found", customers);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing customer details request", false,
					GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}


	@GetMapping("/previewCustomers")
	public ResponseEntity<?> previewCustomers(@RequestHeader HttpHeaders httpHeaders,
			@RequestParam("FromDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encFromDate,
			@RequestParam(value = "ToDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encToDate,
			@RequestParam(value = "enrolledType") String encEnrolledType,
			@RequestParam(value = "branchCode") String encBranchCode,
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
			Date toDatePlus1 = CommonFunctions.getOneDayPlusDate(toDate);
			System.out.println("####"+toDatePlus1);
            List<CustomerToDispaly> customers;
            UserGroup userGroup = userGroupService.getRightCode(Long.valueOf(groupId));
            if (userGroup.getGroupCode().equalsIgnoreCase("G001")
                    || userGroup.getGroupCode().equalsIgnoreCase("G002") 
                    || userGroup.getGroupCode().equalsIgnoreCase("G003")
                    || userGroup.getGroupCode().equalsIgnoreCase("G004")) {
                customers = customerService.gtEnrolledCustomers(fromDate, toDatePlus1, enrolledType);
            }else{
                customers = customerService.getEnrolledCustomersByBranch(fromDate, toDatePlus1, enrolledType, branchCode);
            }
			GlobalResponse2 globalResponse = null;
			if (customers.size() > 0) {
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers found", customers);
			}else
			 	 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers found", customers);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing customer details request", false,
					GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}

	@GetMapping("/previewConvertedCustomersStaff")
	public ResponseEntity<?> previewConvertedCustomersStaff(@RequestHeader HttpHeaders httpHeaders,
			@RequestParam("FromDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encFromDate,
			@RequestParam(value = "ToDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encToDate,
			@RequestParam(value = "enrolledType") String encEnrolledType,
			@RequestParam(value = "branchCode") String encBranchCode,
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
			Date toDatePlus1 = CommonFunctions.getOneDayPlusDate(toDate);
			System.out.println("####"+toDatePlus1);
            List<ConvertedCustomerStaff> customers;
            UserGroup userGroup = userGroupService.getRightCode(Long.valueOf(groupId));
            if (userGroup.getGroupCode().equalsIgnoreCase("G001")
                    || userGroup.getGroupCode().equalsIgnoreCase("G002") 
                    || userGroup.getGroupCode().equalsIgnoreCase("G003")
                    || userGroup.getGroupCode().equalsIgnoreCase("G004")) {
                customers = customerService.GtConvertedCustomersStaff(fromDate, toDatePlus1, enrolledType);
            }else{
                customers = customerService.GtConvertedCustomersStaffByBranch(fromDate, toDatePlus1, enrolledType, branchCode);
            }
			GlobalResponse2 globalResponse = null;
			if (customers.size() > 0) {
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers found", customers);
			}else
			 	 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "customers found", customers);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing customer details request", false,
					GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}
	
	@PostMapping(value = "/deleteCustomer")
	public ResponseEntity<?> deleteCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {
		String responsePayload="";
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
			Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
			int cust = customerService.deleteCustomer(customer.getDeletedBy(), customer.getCustomerId());

			if (cust > 0) {
				GlobalResponse globalResponse = new GlobalResponse(GlobalResponse.APIV, "000", true,
						"customer  " + customer.getCustomerId() + " deleted successfully");
				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

			}
			GlobalResponse globalResponse = new GlobalResponse(GlobalResponse.APIV, "201", false, "no customers found");
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

	@PostMapping(value = "/approveRemoveCustomer")
	public ResponseEntity<?> approveRemoveCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {

		String responsePayload="";
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
		Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
		try {
			String t24Url = env.getProperty("tserver") + customer.getCustomerId() + "/false";
			String customerId = customer.getCustomerId();
            log.info("update url for " + t24Url);

			String response = HttpRestProccesor.postJson(t24Url, customerId);

			log.info("T24 response: " + response);
			if(response.equals("failed")){
				log.error("Accessing the T24 endpoint in approveRemoveCustomer has failed");
			}
		} catch (Exception e) {
			log.error("Exception while accessing the T24 endpoint in approveRemoveCustomer ", e);
		}

		try {
			int cust = customerService.approveRemoveCustomer(customer.getCustomerId());

			if (cust > 0) {
				GlobalResponse globalResponse = new GlobalResponse(GlobalResponse.APIV, "000", true,
						"removal of customer  " + customer.getCustomerId() + " approved successfully");

				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			GlobalResponse globalResponse = new GlobalResponse(GlobalResponse.APIV, "201", false, "no customers found");
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

	@PostMapping(value = "/rejectRemoveCustomer")
	public ResponseEntity<?> rejectRemoveCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) throws NoSuchAlgorithmException, IOException {

		String responsePayload="";

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);

		String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
		Customer customer =  new Gson().fromJson(decryptedData,Customer.class);
		try {
			int cust = customerService.rejectRemoveCustomer(customer.getCustomerId());

			if (cust > 0) {
				GlobalResponse globalResponse = new GlobalResponse(GlobalResponse.APIV, "000", true,
						"removal of customer  " + customer.getCustomerId() + " rejected successfully");

				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			GlobalResponse globalResponse = new GlobalResponse(GlobalResponse.APIV, "201", false, "no customers found");

			responsePayload = aeSsecure.encrypt(gson.toJson(globalResponse).toString());
			return new ResponseEntity<>(responsePayload,
					HttpStatus.OK);
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
			e.printStackTrace();
			responsePayload = aeSsecure.encrypt(gson.toJson(resp).toString());

			return new ResponseEntity<>(responsePayload, HttpStatus.OK);
		}
	}
	@PostMapping(value = "convertStaffToCustomer")
	public ResponseEntity<?> convertStaffToCustomer(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCustomer) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCustomer);
			Customer customerRequestBody = objectMapper.readValue(decryptedData, Customer.class);
			Customer customer = null;
			int customerUndeleted = 0;
			if (customerService.checkCustomerDeleted(customerRequestBody.getCustomerId()) != null) {
				customerUndeleted = customerService.customerUnDelete(customerRequestBody.getCreatedBy(),
						customerRequestBody.getCustomerId());
			} else {
				customer = customerService.upCustomerData(customerRequestBody);
			}
			if (customer != null || customerUndeleted > 0) {
				System.out.println("executing within here");
				log.info("calling convert staff to customer " );
				int conversionUpdateReturnValue = this.tellerService.convertStaffToCustomer(customerRequestBody.getCustomerId());
				System.out.println("i have already executed");
				if (conversionUpdateReturnValue > 0) {
					Teller teller = tellerService.checkStaffDeleted(customerRequestBody.getCustomerIdNumber());
					if (teller != null) {
						int userStatusUpdate = userService.updateStatusToFalse(teller.getTellerSignOnName());
						if (userStatusUpdate > 0) {
							return new ResponseEntity<>(new GlobalResponse("000",
									"Conversion of Staff to Customer done successfully", true, GlobalResponse.APIV),
									HttpStatus.OK);
						} else {
							return new ResponseEntity<>(
									new GlobalResponse("000", "Conversion of Staff to Customer done successfully",
											true, GlobalResponse.APIV),
									HttpStatus.OK);
						}
					} else {
						return new ResponseEntity<>(
								new GlobalResponse("201", "Staff not found", false, GlobalResponse.APIV),
								HttpStatus.OK);
					}

				} else {
					return new ResponseEntity<>(new GlobalResponse("201",
							"Conversion of Staff to Customer not done successfully", false, GlobalResponse.APIV),
							HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV, "201", false,
						"Error occurred while attempting to create the customer when converting staff to customer"),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404",
					"An Exception occurred when attempting to covert staff to customer", false, GlobalResponse.APIV);
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}
	@GetMapping("/getCustomerLogsForExporting")
	public String getCustomerLogsForExporting(
			@RequestParam("FromDt") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "ToDt") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "enrolledType") String enrolledType) {
		try {
			List<CustomerToDispaly> customers = customerService.gtEnrolledCustomers(fromDate, toDate, enrolledType);
			String response = "";
			response += "Full Name, ";
			response += "Gender, ";
			response += "Phone Number, ";
			response += "Id Number";
			response += "\n";
			for (CustomerToDispaly customer : customers) {
				response += customer.getCustomerName() + ", ";
				//response += customer.getGender() + ", ";
				response += customer.getPhoneNumber() + ", ";
				response += customer.getCustomerIdNumber() + "";
				response += "\n";
			}
			response += "\n";
			if (customers.size() > 0) {
				return response;
			} else {
				return "logs not found";
			}
		} catch (Exception e) {
			return "Exception while creating customer logs";
		}
	}
}
