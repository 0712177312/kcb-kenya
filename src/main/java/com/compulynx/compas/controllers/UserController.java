package com.compulynx.compas.controllers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.Response;
import com.compulynx.compas.models.Teller;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.services.UserGroupService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.customs.SendMail;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.customs.responses.GlobalResponse2;
import com.compulynx.compas.customs.responses.UserResponse;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.extras.CustomerToDispaly;
import com.compulynx.compas.models.extras.UserDto;
import com.compulynx.compas.models.extras.UserReport;
import com.compulynx.compas.models.extras.UsersToVerify;
import com.compulynx.compas.security.AES;
import com.compulynx.compas.services.UserService;

import javax.servlet.http.HttpSession;
@RestController
@RequestMapping(value = Api.REST)
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupService userGroupService;
    @Autowired
     private PasswordEncoder passwordEncoder;
    @Autowired
    private AESsecure aeSsecure;
    @Autowired
    private EncryptionPayloadResp encryptionPayloadResp;

    Gson gson = new Gson();

    @GetMapping("/allUsers")
    public ResponseEntity<?> getUsers() throws Exception {
        try {
            List<User> users = userService.getUsers();

            System.out.println("System users ####");

            UserResponse userResponse = null;
            if (users.isEmpty()) {
                userResponse = new UserResponse(users, "no users found", false, "000", Api.API_VERSION);
            }else
                userResponse = new UserResponse(users, "users found", true, "000", Api.API_VERSION);

            String jsonObj = CommonFunctions.convertPojoToJson(userResponse);
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
    @GetMapping("/getLoggedInUserDetails")
    public ResponseEntity<?> getLoggedInUserDetails() throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication == null){
                GlobalResponse resp = new GlobalResponse("404", "No loggedin users", false, GlobalResponse.APIV);

                String jsonObj = CommonFunctions.convertPojoToJson(resp);
                encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
                HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
                return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
            }
            return userService.getLoggedInUser(authentication.getName());
        } catch (Exception e) {
            GlobalResponse resp = new GlobalResponse("500", "Internal Server request", false, GlobalResponse.APIV);
            e.printStackTrace();
            String jsonObj = CommonFunctions.convertPojoToJson(resp);
            encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
        }
    }

    @GetMapping("/allUsersByBranchExcludingCurrentUser")
    public ResponseEntity<?> getAllUsersByBranchExcludingCurrentUser(@RequestHeader HttpHeaders httpHeaders,
            @RequestParam(value = "branchCode") String ecnBranchCode,
            @RequestParam(value = "groupid") String encGroupId,
            @RequestParam(value = "userId") String encUserId) throws Exception {

        List<String> headerList = httpHeaders.getValuesAsList("Key");
        String key = headerList.get(0);

        String decryptedBranchCode = aeSsecure.integratedDataDecryption(key,ecnBranchCode);
        String decryptedGroupId = aeSsecure.integratedDataDecryption(key,encGroupId);
        String decUserId = aeSsecure.integratedDataDecryption(key,encUserId);

        String strGroupId =  new Gson().fromJson(decryptedGroupId,String.class);
        String strUserId =  new Gson().fromJson(decUserId,String.class);
        String branchCode =  new Gson().fromJson(decryptedBranchCode,String.class);

        Long groupId = Long.parseLong(strGroupId);
        Long userId = Long.parseLong(strUserId);

        try {
            List<User> users;
            UserGroup userGroup = userGroupService.getRightCode(groupId);

            if (userGroup.getGroupCode().equalsIgnoreCase("G001")
                    || userGroup.getGroupCode().equalsIgnoreCase("G002") 
                    || userGroup.getGroupCode().equalsIgnoreCase("G003")
                    || userGroup.getGroupCode().equalsIgnoreCase("G004")) {
                users = userService.getUsers();
            } else {
                users = userService.getAllUsersByBranchExcludingCurrentUser(branchCode, userId);
            }
            UserResponse userResponse =null;
            if (users.isEmpty()) {
                userResponse = new UserResponse(users, "no users found", false, "000", Api.API_VERSION);
            } else {
                userResponse = new UserResponse(users, "users found", true, "000", Api.API_VERSION);
            }
            String jsonObj = CommonFunctions.convertPojoToJson(userResponse);
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

    @PostMapping("/editUserProfile")
    public ResponseEntity<?> editUserProfile(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encUser) throws Exception {
        try {
            //User user =  new Gson().fromJson(aeSsecure.decrypt(encUser),User.class);
     

            List<String> headerList = httpHeaders.getValuesAsList("Key");
            String key = headerList.get(0);

            String decryptedData = aeSsecure.integratedDataDecryption(key,encUser);
           	logger.info(decryptedData);
           	
           	UserDto res= new Gson().fromJson(decryptedData, UserDto.class);
           	logger.info(new Gson().toJson(res));
           	logger.info(" updated by "+res.getUpdatedBy());
            User user =  new Gson().fromJson(decryptedData,User.class);
           // logger.info(new Gson().toJson(user));

            System.out.println(user);
            System.out.println(user.getFullName() + user.getEmail() + user.getPhone() + user.getGroup() +
                    user.getBranch() + user.getId()+ " : "+user.getUpdatedBy());
            int userUpdate = 0;
            // user checked the button to unlock the particular user
            if(user.isLocked()){
                userUpdate = userService.updateUsersAndUnlock(user.getGroup(), user.isStatus(), user.getId(),user.getUpdatedBy());
            }else {
                userUpdate = userService.updateUsers(user.getGroup(), user.isStatus(), user.getId(),user.getUpdatedBy());
            }
            GlobalResponse globalResponse = null;
            if (userUpdate > 0) {
                globalResponse = new GlobalResponse(GlobalResponse.APIV, "000", true, "User updated successfully");
            } else {
                globalResponse = new GlobalResponse(GlobalResponse.APIV, "201",
                        false, "user not updated successfully");
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

    @PostMapping("/upUser")
    public ResponseEntity<?> addUser(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encUser) throws Exception {
        try {
            List<String> headerList = httpHeaders.getValuesAsList("Key");
            String key = headerList.get(0);

            String decryptedData = aeSsecure.integratedDataDecryption(key,encUser);
            User user =  new Gson().fromJson(decryptedData,User.class);

            User username = userService.findByUsername(user.getUsername());

            UserResponse userResponse;
            if (username != null && user.getId() == 0) {
                userResponse = new UserResponse(user, "user with same username already exist", false, "201", Api.API_VERSION);

                String jsonObj = CommonFunctions.convertPojoToJson(userResponse);
                encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
                HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
                return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
            }
            if (user.getPassword() != null) {
               // user.setPassword(AES.encrypt(user.getPassword()));
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword("");
            }
            
            
           // START-ADDED BY KENSON -08/05/2024- SELF AUTORISED
            user.setApproved("V");
           // END
            User usr = userService.addUser(user);

            if (usr == null) {
                userResponse = new UserResponse(user, "failed to add user", false, "201", Api.API_VERSION);
            }else
                userResponse = new UserResponse(user, "user added successfully", true, "200", Api.API_VERSION);
            String jsonObj = CommonFunctions.convertPojoToJson(userResponse);
            encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

           // return new ResponseEntity<>(aeSsecure.encrypt(gson.toJson(new UserResponse(user, "user updated successfully", true, "000", Api.API_VERSION))), HttpStatus.OK);
        } catch (Exception e) {
            GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
            e.printStackTrace();
            String jsonObj = CommonFunctions.convertPojoToJson(resp);
            encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
        }
    }

    @PostMapping(value = "/sysusers/auth")
    public ResponseEntity<?> authUser(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encUser) throws Exception {
        String response = "";
        UserResponse userresponse;
        User userpro=null;
        User usertrials=null;

        System.out.println("Got here");
        try {
            //User user =  new Gson().fromJson(aeSsecure.decrypt(encUser),User.class);
            List<String> headerList = httpHeaders.getValuesAsList("Key");
            String key = headerList.get(0);

            String decryptedData = aeSsecure.integratedDataDecryption(key,encUser);
            User user =  new Gson().fromJson(decryptedData,User.class);

            userpro= userService.authUser(user);
            System.out.println(userpro);
            if (userpro == null) {
                userresponse=new UserResponse("Invalid or Un-known user credentials, kindly verify to continue",
                        false, "201", Api.API_VERSION);
                User currentuser=userService.findByUsername(user.getUsername());
                if(currentuser!=null) {
                    userService.updateUsertrials(user.getUsername());
                    usertrials = userService.findByUsername(user.getUsername());
                    if (usertrials.getTrials() >= 3) {
                        userService.updateUserlocked(user.getUsername());
                        userresponse = new UserResponse("Account has been locked",
                                false, "201", Api.API_VERSION);
                    }
                }
            }
            else if(userpro.isLocked()){
                userresponse=new UserResponse("Account has been locked.",
                        false, "201", Api.API_VERSION);
            }
            else if (userpro != null && userpro.isStatus() != false) {
                System.out.println("email" + user.getEmail());

                userresponse = new UserResponse(userpro,"successfully authenticated!",
                        true, "000", Api.API_VERSION);
            } else {
                userresponse =new UserResponse("successfully authenticated!",
                        false, "201", Api.API_VERSION);
            }
        } catch (Exception e) {
            userresponse =new UserResponse("An error occurred!",
                    false, "404", Api.API_VERSION);
            e.printStackTrace();
        }
        return new ResponseEntity<>(userresponse, HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    @PostMapping(value = "/sysusers/print/auth")
    public ResponseEntity<?> printAuthUser(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encUser) throws Exception {
        String responsePayload ="";
        try {
            List<String> headerList = httpHeaders.getValuesAsList("Key");
            String key = headerList.get(0);

            String decryptedData = aeSsecure.integratedDataDecryption(key,encUser);
            User user =  new Gson().fromJson(decryptedData,User.class);
            User userpro = userService.findByUsername(user.getUsername());
            System.out.println(userpro);
            UserResponse userResponse =  null;
            if (userpro == null) {
               userResponse = new UserResponse("failed to add user", false, "409", Api.API_VERSION);
            } else if (!userpro.getApproved().equalsIgnoreCase("V") || userpro.isStatus() == false) {
               userResponse = new UserResponse("user specified is neither verified or active, kindly ensure you verified and active ", false, "201", Api.API_VERSION);
            } else if (userpro != null) {
                 userResponse = new UserResponse(userpro, "successfully authenticated!", true, "000", Api.API_VERSION);
            } else {
                 userResponse = new UserResponse("invalid user credentials", false, "409", Api.API_VERSION);
            }
            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(userResponse));
            responsePayload = encryptionPayloadResp.getEncryptedPayload();
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(responsePayload);
        } catch (Exception e) {
            GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
            e.printStackTrace();

            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(resp));
            String resPayload = encryptionPayloadResp.getEncryptedPayload();
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(resPayload);
        }
    }

    @GetMapping(value = "/users/toverify")
    public ResponseEntity<?> getUsersToVerify(
//    		@RequestParam("fromDt")
//    		@DateTimeFormat(pattern="yyyy-MM-dd")
//    		Date fromDt,
//    		@RequestParam(value="toDt")
//    		@DateTimeFormat(pattern="yyyy-MM-dd")
//    		Date toDt
    ) throws NoSuchAlgorithmException, IOException {
        try {
            System.out.println("works...");
            List<UsersToVerify> users = userService.getUsersToVerify();
            UserResponse userResponse = null;
            if (users.isEmpty()) {
                 userResponse = new UserResponse("no found users to approve ",
                        false, "201", Api.API_VERSION, users);
               // return new ResponseEntity<>(aeSsecure.encrypt(gson.toJson(userResponse)), HttpStatus.OK);
            }else
             userResponse = new UserResponse("found users to approve ",
                    true, "000", Api.API_VERSION, users);
            //return new ResponseEntity<>(aeSsecure.encrypt(gson.toJson(userResponse)), HttpStatus.OK);

            String jsonObj = CommonFunctions.convertPojoToJson(userResponse);
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
            //return new ResponseEntity<>(aeSsecure.encrypt(gson.toJson(resp)), HttpStatus.OK);
        }
    }
    @PostMapping(value = "/users/verifyusers")
    public ResponseEntity<?> verifyUsers(@RequestHeader HttpHeaders httpHeaders,@RequestBody String encUser) {
        try {
            List<String> headerList = httpHeaders.getValuesAsList("Key");
            String key = headerList.get(0);

            String decryptedData = aeSsecure.integratedDataDecryption(key,encUser);
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users =  new Gson().fromJson(decryptedData,listType);
            if (users.size() > 0) {
                for (User user : users) {
                    System.out.println("user id ##" + user.getId());
                    System.out.println("approved by ##" + user.getApprovedBy());
                    int upuser = userService.verifyUsers(user.getId(),user.getApprovedBy());
                    if (upuser < 0) {
                        return new ResponseEntity<>(new UserResponse("there was problem approving users, kindly retry ",
                                false, "201", Api.API_VERSION), HttpStatus.OK);
                    }
                }
                return new ResponseEntity<>(new UserResponse("user's approved successfully ",
                        true, "000", Api.API_VERSION), HttpStatus.OK);
            }
            return new ResponseEntity<>(new UserResponse("There was problem updating users, kindly retry ",
                    false, "201", Api.API_VERSION), HttpStatus.OK);
        } catch (Exception e) {
            GlobalResponse resp = new GlobalResponse("404", "error processing request", false, GlobalResponse.APIV);
            e.printStackTrace();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
    }

    public String randomPassword() {
        System.out.println("Generating Password");

        String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&?{}*";
        StringBuilder builder = new StringBuilder();

        int count = 8;

        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
    
	@GetMapping("/previewProfiles")
	public ResponseEntity<?> previewUserProfiles(@RequestHeader HttpHeaders httpHeaders,
			@RequestParam("FromDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encFromDate,
			@RequestParam(value = "ToDt") @DateTimeFormat(pattern = "yyyy-MM-dd") String encToDate,
			@RequestParam(value = "branchCode") String encBranchCode,
			@RequestParam(value = "enrolledType") String encEnrolledType,
            @RequestParam(value = "groupid") String encGroupId) throws NoSuchAlgorithmException, IOException {

		List<String> headerList = httpHeaders.getValuesAsList("Key");
		String key = headerList.get(0);
		logger.info("encFromDate{} ",encFromDate);
		logger.info("encToDate{} ",encToDate);

		logger.info("encBranchCode{} ",encBranchCode);

		logger.info("encEnrolledType{} ",encEnrolledType);

		
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
            List<UserReport> users;
            UserGroup userGroup = userGroupService.getRightCode(Long.valueOf(groupId));
            if (userGroup.getGroupCode().equalsIgnoreCase("G001")
                    || userGroup.getGroupCode().equalsIgnoreCase("G002") 
                    || userGroup.getGroupCode().equalsIgnoreCase("G003")
                    || userGroup.getGroupCode().equalsIgnoreCase("G004")) {
            	users = userService.gtEnrolledUsers(fromDate, toDatePlus1, enrolledType);
            }else{
            	users = userService.getEnrolledUsersByBranch(fromDate, toDatePlus1, enrolledType, branchCode);
            }
			GlobalResponse2 globalResponse = null;
			if (users.size() > 0) {
				 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "user profiles found", users);
			}else
			 	 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "user profiles  found", users);

			String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

		} catch (Exception e) {
			GlobalResponse resp = new GlobalResponse("404", "error processing user profiles details request", false,
					GlobalResponse.APIV);
			e.printStackTrace();

			String jsonObj = CommonFunctions.convertPojoToJson(resp);
			encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
			HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
			return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
		}
	}
}
