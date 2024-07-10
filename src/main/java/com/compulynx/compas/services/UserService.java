package com.compulynx.compas.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.customs.responses.UserResponse;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.extras.UserReport;
import com.compulynx.compas.models.extras.UsersToVerify;
import com.compulynx.compas.repositories.UserRepository;
import com.compulynx.compas.security.AES;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepo;
	@Autowired
	private AESsecure aeSsecure;
	@Autowired
	private EncryptionPayloadResp encryptionPayloadResp;
	Gson gson = new Gson();
	
	@Autowired
	public UserService(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	public List<User> getUsers() {
		return userRepo.findAll();
	}

	public List<User> getAllUsersByBranchExcludingCurrentUser(String branchCode, Long userId) {
		return userRepo.getAllUsersByBranchExcludingCurrentUser(branchCode, userId);
	}

	public User findByUsername(String username) {
		// TODO Auto-generated method stub
	try{
		Optional<User> optionalUser =  userRepo.findByUsername(username);
		if(optionalUser.isPresent())
			return optionalUser.get();

	}catch(Exception e){
		e.printStackTrace();
	}
	return null;

	}

	public User addUser(User user) {
		// TODO Auto-generated method stub
		return userRepo.save(user);
	}

	public User authUser(User user) throws Exception {
		//System.out.println(user.getPassword());
		//System.out.println("user password####"+user.getPassword());
		System.out.println("user password####"+user.getUsername());
		return userRepo.findByUsernameAndPassword(user.getUsername(),AES.encrypt(user.getPassword()));
	}

	public List<UsersToVerify> getUsersToVerify() {
		// TODO Auto-generated method stub
		return userRepo.getUsersToVerify();
	}

	public int verifyUsers(Long id,int approvedBy) {
		// TODO Auto-generated method stub
		return userRepo.approveUsers(id,approvedBy);
	}

	public int updateUsers(int group, boolean status, Long userId,int updatedBy) {
		// TODO Auto-generated method stub
		return userRepo.updateUsers(group, status, userId,updatedBy);
	}

	public int updateUsersAndUnlock(int group, boolean status, Long userId,int updatedBy) {
		// TODO Auto-generated method stub
		return userRepo.updateUsersAndUnlock(group, status, userId,updatedBy);
	}

	public int updateStatusToFalse(String username){
		return userRepo.updateStatusToFalse(username);
	}

	public int updateStatusToTrue(String username){
		return userRepo.updateStatusToTrue(username);
	}

	public int updateUsertrials(String username) {
			return userRepo.updateUsertrials(username);
	}
	public int updateUserlocked(String username) {
		return userRepo.updateUserlocked(username);
	}

    public ResponseEntity<?> getLoggedInUser(String username) throws NoSuchAlgorithmException, IOException {
		User user = userRepo.manualAuth(username);
		user.setPassword(null);
		user.setCountry(username);
		UserResponse userResponse = new UserResponse(user, "User details", true, "000", Api.API_VERSION);

		String jsonObj = CommonFunctions.convertPojoToJson(userResponse);
		encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
		HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
		return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());


		//return new ResponseEntity<>(aeSsecure.encrypt(gson.toJson(userResponse)), HttpStatus.OK);
    }

	public List<UserReport> gtEnrolledUsers(Date fromDate, Date toDatePlus1, String enrolledType) {
		System.out.println("from date: " + fromDate);
		System.out.println("to date: " + toDatePlus1);
		System.out.println("enrolled type: " + enrolledType);
		return userRepo.gtEnrolledUsers(fromDate, toDatePlus1, enrolledType);
	}

	public List<UserReport> getEnrolledUsersByBranch(Date fromDate, Date toDatePlus1, String enrolledType,
			String branchCode) {
		// TODO Auto-generated method stub
		return userRepo.getEnrolledUsersByBranch(fromDate, toDatePlus1, enrolledType, branchCode);
	}
}
