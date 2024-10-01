package com.compulynx.compas.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;

import com.compulynx.compas.customs.responses.GlobalResponse2;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.google.gson.Gson;
//import org.bouncycastle.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.Country;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.services.RegionService;

@RestController
@RequestMapping(value = Api.REST)
public class RegionController {
	@Autowired
	private RegionService regionService;

	@Autowired
	private AESsecure aeSsecure;
	@Autowired
	private EncryptionPayloadResp encryptionPayloadResp;

	Gson gson = new Gson();
	
	@GetMapping("/gtCountries")
	public ResponseEntity<?> getCountries () throws NoSuchAlgorithmException, IOException {

		String responsePayload="";

		try {
		List<Country> countries = regionService.getCountries();
		
		if(countries.size() < 0) {
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"201",
					false, "countries found",
					countries);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000",
					true, "countries found",
					countries);

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
	@GetMapping("/gtActiveCountries")
	public ResponseEntity<?> getActiveCountries () throws NoSuchAlgorithmException, IOException {
		try {
		List<Country> countries = regionService.getActiveCountries();
		
		if(countries.size() < 0) {
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"201",
					false, "countries found",
					countries);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000",
					true, "countries found",
					countries);

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
	@GetMapping("/gtActiveCountryBranches")
	public ResponseEntity<?> getActiveCountryBranches (@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(value="ctry") String encCountry) throws NoSuchAlgorithmException, IOException {
		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);
		String decryptedData = aeSsecure.integratedDataDecryption(key,encCountry);
		String decGroupId =  new Gson().fromJson(decryptedData,String.class);
		String country = decGroupId;

		try {
		List<Branch> branches = regionService.getActiveCountryBranches(country);
		   if(branches.size() < 0) {
			   GlobalResponse2 globalResponse = new GlobalResponse2("201",
					   "no branches found",false,GlobalResponse.APIV,branches);

			  // responsePayload = aeSsecure.encrypt(gson.toJson(globalResponse).toString());
			   String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			   encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			   HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			   return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			   //return new ResponseEntity<>(CommonFunctions.encryptResPayload(globalResponse),HttpStatus.OK);
		}
			GlobalResponse2 globalResponse = new GlobalResponse2("000",
					"branches found",true,GlobalResponse.APIV,branches);

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
	@PostMapping("/upCountry")
	public ResponseEntity<?> upCountry(@RequestHeader HttpHeaders httpHeaders,
			@RequestBody String encCountry) {
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCountry);
			Country country =  new Gson().fromJson(decryptedData,Country.class);

			Country cou = regionService.checkIfExists(country.getCountryCode(), country.getName());
			if(cou != null && country.getId() == 0) {
				 return new ResponseEntity<>(new GlobalResponse("201",
			 			"Specified country name or code already exist",false,GlobalResponse.APIV),HttpStatus.OK);
			} else {
			   Country count = regionService.upCountry(country);
			   if(count == null) {
				   return new ResponseEntity<>(new GlobalResponse("201",
			 			 "failed to update country details",false,GlobalResponse.APIV),HttpStatus.OK);
		    	}
			    return new ResponseEntity<>(new GlobalResponse("000",
					"Country details successfully updated",true,GlobalResponse.APIV),HttpStatus.OK);
			  }
	    	} catch (Exception e) {
			    GlobalResponse resp = new GlobalResponse("404","error processing request",false,GlobalResponse.APIV);
		     	e.printStackTrace();
		    	return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	@GetMapping("/gtBranches")
	public ResponseEntity<?> getBranches() throws NoSuchAlgorithmException, IOException {
		try {
		 List<Branch> branches = regionService.getBranches();
		   if(branches.size() < 0) {

			   GlobalResponse2 globalResponse = new GlobalResponse2("201",
					   "no branches found",false,GlobalResponse.APIV,branches);
			   String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			   encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			   HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			   return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
			GlobalResponse2 globalResponse = new GlobalResponse2("000",
					"branches found",true,GlobalResponse.APIV,branches);

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
	
	@GetMapping("/gtActiveBranches")
	public ResponseEntity<?> getActiveBranches() throws NoSuchAlgorithmException, IOException {
		try {
		 List<Branch> branches = regionService.getActiveBranches();
		   if(branches.size() < 0) {
			   GlobalResponse2 globalResponse = new GlobalResponse2("201",
					   "no branches found",false,GlobalResponse.APIV,branches);

			   String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			   encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			   HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			   return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
			GlobalResponse2 globalResponse = new GlobalResponse2("000",
					"branches found",true,GlobalResponse.APIV,branches);
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
	
	@PostMapping("/upBranch")
	public ResponseEntity<?> upBranch(@RequestHeader HttpHeaders httpHeaders, @RequestBody String encBranch)
	{
		try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encBranch);
			Branch branch =  new Gson().fromJson(decryptedData,Branch.class);

			Branch bran = regionService.checkIfBranchExist(branch.getBranchCode(),branch.getBranchName());
		 if( bran !=null && branch.getId() == 0) {
		       return new ResponseEntity<>(new GlobalResponse("201",
				    "branch with specified details already exists",false,GlobalResponse.APIV),HttpStatus.OK);
		  } else {
			Branch br = regionService.upBranch(branch);
			if(br !=null) {
			  return new ResponseEntity<>(new GlobalResponse("000",
					"branch details successfully updated",true,GlobalResponse.APIV),HttpStatus.OK);
	    	}
		       return new ResponseEntity<>(new GlobalResponse("201",
				    "failed to updated branch details",false,GlobalResponse.APIV),HttpStatus.OK);
			}
	     } catch (Exception e) {
		    	 GlobalResponse resp = new GlobalResponse("404","error processing request",false,GlobalResponse.APIV);
		    	   e.printStackTrace();
			       return new ResponseEntity<>(resp, HttpStatus.OK);
	  }
	}


    @GetMapping(value="/getBranchesToWaive")
    public ResponseEntity<?> getBranchToWaive( ) throws NoSuchAlgorithmException, IOException {
		try {
			 List<Branch> branches = regionService.getBranchesToWaive();
			   if(branches.size() < 0) {
				   GlobalResponse2 globalResponse = new GlobalResponse2("201",
						   "no branches found",false,GlobalResponse.APIV,branches);
				   String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				   encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				   HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				   return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
			}
			GlobalResponse2 globalResponse = new GlobalResponse2("000",
					"branches found",true,GlobalResponse.APIV,branches);

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
    
    @PostMapping(value="/approveBranchWaive")
    public ResponseEntity<?> approveWaivedBranch(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encBranch) {
    	try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encBranch);
			Branch branch =  new Gson().fromJson(decryptedData,Branch.class);

			System.out.println("country code" + branch.getCountryCode());
    	int cust = regionService.approveBranchWaive(branch.getWaivedApprovedBy(), branch.getId());
    	if(cust > 0) {
    		return new ResponseEntity<>(new GlobalResponse(
    				"000","branch waive approved successfully",true,GlobalResponse.APIV),HttpStatus.OK);
    	} else {
		return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV,"201",
				false, "failed to update branch details"),HttpStatus.OK);
    	  }
        } catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing country details request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	  }
    }
    @PostMapping(value="/rejectBranchWaive")
    public ResponseEntity<?> rejectWaivedBranch(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encBranch) {
    	try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encBranch);
			Branch branch =  new Gson().fromJson(decryptedData,Branch.class);

			System.out.println("country code" + branch.getCountryCode());
    	int cust = regionService.rejectBranchWaive(branch.getWaivedApprovedBy(), branch.getId());
    	if(cust > 0) {
    		return new ResponseEntity<>(new GlobalResponse(
    				"000","branch waive rejected successfully",true,GlobalResponse.APIV),HttpStatus.OK);
    	} else {
		return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV,"201",
				false, "failed to update branch waive"),HttpStatus.OK);
    	  }
        } catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing country details request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	  }
    }
    
    @GetMapping(value ="/gtWaivedBranches")
    public ResponseEntity<?> getWaivedBranches() throws NoSuchAlgorithmException, IOException {
      try {
    	List<Branch> brans = regionService.getWaivedBranches();
    	if(brans.size() > 0) {
			GlobalResponse2 globalResponse =new GlobalResponse2(GlobalResponse.APIV,"000",
					true, "waived branches found",
					brans);
			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
    	}
		  GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"201",
				  false, "no waived branches found",
				  brans);

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
    
    @PostMapping(value="/waiveBranch")
    public ResponseEntity<?> waiveBranch(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encBranch) {
    	try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encBranch);
			Branch branch =  new Gson().fromJson(decryptedData,Branch.class);

			int cust = regionService.waiveBranch(branch.getWaivedBy(), branch.getId());
    	if(cust > 0) {
    		return new ResponseEntity<>(new GlobalResponse("000","branch details updated successfully ",
    				true,GlobalResponse.APIV),HttpStatus.OK);
    	} else {
		return new ResponseEntity<>(new GlobalResponse("201","failed to update branch details",
				false,GlobalResponse.APIV),HttpStatus.OK);
    	  }
        } catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing country details request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	  }
    }    
    

    @GetMapping(value="/gtCountriesToWaive")
	public ResponseEntity<?> getCountriesToWaive () throws NoSuchAlgorithmException, IOException {
		try {
		List<Country> countries = regionService.getCountriesToWaive();

			GlobalResponse2 globalResponse = null;
		if(countries.size() < 0)
			 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"201", false, "countries found", countries);
		else
			 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000", true, "countries found", countries);

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
    
    @PostMapping(value="/approveCountryWaive")
    public ResponseEntity<?> approveWaivedCountry(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCountry) {
    	try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCountry);
			Country country =  new Gson().fromJson(decryptedData,Country.class);
			System.out.println("country code" + country.getCountryCode());
    	int cust = regionService.approveCountryWaive(country.getWaivedApprovedBy(), country.getId());
    	if(cust > 0) {
    		return new ResponseEntity<>(new GlobalResponse(
    				"000","country waived successfully",true,GlobalResponse.APIV),HttpStatus.OK);
    	} else {
		return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV,"201",
				false, "failed to update country details"),HttpStatus.OK);
    	  }
        } catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing country details request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	  }
    }
    @PostMapping(value="/rejectCountryWaive")
    public ResponseEntity<?> rejectWaivedCountry(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encCountry) {
    	try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCountry);
			Country country =  new Gson().fromJson(decryptedData,Country.class);

			System.out.println("country code" + country.getCountryCode());
    	int cust = regionService.rejectCountryWaive(country.getWaivedApprovedBy(), country.getId());
    	if(cust > 0) {
    		return new ResponseEntity<>(new GlobalResponse(
    				"000","country details updated successfully",true,GlobalResponse.APIV),HttpStatus.OK);
    	} else {
		return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV,"201",
				false, "failed to update country details"),HttpStatus.OK);
    	  }
        } catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing country details request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	  }
    }
    
    @PostMapping(value="/waiveCountry")
    public ResponseEntity<?> waiveCountry(@RequestHeader HttpHeaders httpHeaders, @RequestBody String encCountry) {
    	try {
			List<String> headerList = httpHeaders.getValuesAsList("Key");
			String key = headerList.get(0);

			String decryptedData = aeSsecure.integratedDataDecryption(key,encCountry);
			Country country =  new Gson().fromJson(decryptedData,Country.class);

			System.out.println("country code" + country.getCountryCode());
    	int cust = regionService.waiveCountry(country.getWaivedBy(), country.getId());
    	if(cust > 0) {
    		return new ResponseEntity<>(new GlobalResponse(
    				"000","Country waived successfully",true,GlobalResponse.APIV),HttpStatus.OK);
    	} else {
		return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV,"201",
				false, "failed to update country details"),HttpStatus.OK);
    	  }
        } catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing country details request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
	    	return new ResponseEntity<>(resp, HttpStatus.OK);
	  }
    }
    
    @GetMapping(value ="/gtWaivedCountries")
    public ResponseEntity<?> getWaivedCountries() throws NoSuchAlgorithmException, IOException {
      try {
    	List<Country> brans = regionService.getWaivedCountries();
    	if(brans.size() > 0) {
			GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"000",
					true, "waived branches found",
					brans);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
    	}
		  GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV,"201",
				  false, "no waived branches found",
				  brans);
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
    
    @GetMapping(value ="/gtBranchesPrev")
    public ResponseEntity<?> getBranchesPrev(@RequestHeader HttpHeaders httpHeaders,
    		@RequestParam("status")
    		String encStatus) throws NoSuchAlgorithmException, IOException {

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);
		String decryptedData = aeSsecure.integratedDataDecryption(key,encStatus);
		String status =  new Gson().fromJson(decryptedData,String.class);

      try {
    	if(status.equalsIgnoreCase("A")) {
    		List<Branch> brans = regionService.getBranches();
    		if ( brans.size() > 0) {
				GlobalResponse globalResponse = new GlobalResponse(GlobalResponse.APIV,"000",
						true, "waived branches found",
						new HashSet<>(brans));

				String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
				encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
				HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
				return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
        	}
			GlobalResponse globalResponse = new GlobalResponse(GlobalResponse.APIV,"201",
					false, "no branches found",
					new HashSet<>(brans));

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
    	}else {
    		List<Branch> brans = regionService.getBranchesPrev(status);
			GlobalResponse globalResponse = null;
    		if ( brans.size() > 0) {

				 globalResponse = new GlobalResponse(GlobalResponse.APIV,"000",
						true, "waived branches found", new HashSet<>(brans));
        	}else
			 	globalResponse = new GlobalResponse(GlobalResponse.APIV,"201", false, "no waived branches found", new HashSet<>(brans));

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
    	}
	  	} catch (Exception e) {
		    GlobalResponse resp = new GlobalResponse("404","error processing request",false,GlobalResponse.APIV);
	     	e.printStackTrace();
		  String jsonObj = CommonFunctions.convertPojoToJson(resp);
		  encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
		  HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
		  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
	    }
    }
}