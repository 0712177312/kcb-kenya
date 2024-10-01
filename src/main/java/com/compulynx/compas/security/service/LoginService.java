package com.compulynx.compas.security.service;

import com.compulynx.compas.models.Teller;
import com.compulynx.compas.repositories.TellerRepository;
import com.compulynx.compas.repositories.UserRepository;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.jwt.JwtTokenUtil;
import com.compulynx.compas.security.model.*;
import com.compulynx.compas.customs.CommonFunctions;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class LoginService {
    //private final UserService userService;
    @Autowired
    private  CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TellerRepository tellerRepository;
    @Autowired
    private AESsecure aeSsecure;
    Gson gson = new Gson();
    public ResponseEntity<?> login(AuthRequest authRequest) throws NoSuchAlgorithmException, IOException {
        EncryptionPayloadResp encryptionPayloadResp =null;
        System.out.println("LOGIN SERVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String username=  authRequest.getUsername();
        String password = authRequest.getPassword();
        System.out.println(username);
        System.out.println(password);


        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            Token token = jwtTokenUtil.generateToken(authentication.getName());

            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(token));
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

        } catch (DisabledException e) {
            ErrorResponse body = new ErrorResponse("Unauthorized", "200", "User is disabled");

            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(body));
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

        } catch (BadCredentialsException e) {
            ErrorResponse body = new ErrorResponse("Unauthorized", "200", "Wrong User Credentials");

            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(body));
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());

        }catch(Exception ex){
            ErrorResponse wrong_user_credentials = new ErrorResponse(ex.getMessage(), "200", "Wrong User Credentials");

            encryptionPayloadResp = aeSsecure.integratedDataEncryption(gson.toJson(wrong_user_credentials));
            HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
            return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
        }

    }

    public ResponseEntity<?> logout() {
        return new ResponseEntity<>("Logout success",HttpStatus.OK);
    }

    public Token bioLogin(BioAuthReq bioAuthReq) {
        try{
            Optional<Teller> optionalTeller = tellerRepository.findByCustomerId(bioAuthReq.getUsername());

            if(!optionalTeller.isPresent())
                System.out.println("Teller with customerId "+bioAuthReq.getUsername() +" does not exist!");

            Teller teller = optionalTeller.get();

            Token token = jwtTokenUtil.generateToken(teller.getTellerSignOnName());

            return token;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ResponseEntity<?> verifyBioToken(BioTokenVerifyReq bioTokenVerify) {
        try{
            String username = jwtTokenUtil.getUsernameFromToken(bioTokenVerify.getToken());

            CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            Boolean aBoolean = jwtTokenUtil.validateToken(bioTokenVerify.getToken(), userDetails);

            ResponseHeader header = new ResponseHeader(1,"SUCCESS");
            return new ResponseEntity(new BioTokenVerifyRes(aBoolean,userDetails,header),HttpStatus.OK);
        }catch (SignatureException signatureException){
            ResponseHeader header = new ResponseHeader(0,"SignatureException");
            return new ResponseEntity(new BioTokenVerifyRes(false,null,header),HttpStatus.OK);
        } catch (ExpiredJwtException e) {
            ResponseHeader header = new ResponseHeader(0,"ExpiredJwtException");
            return new ResponseEntity(new BioTokenVerifyRes(false,null,header),HttpStatus.OK);
        } catch (BadCredentialsException e) {
            ResponseHeader header = new ResponseHeader(0,"BadCredentialsException");
            return new ResponseEntity(new BioTokenVerifyRes(false,null,header),HttpStatus.OK);
        } catch (AuthenticationException e) {
            ResponseHeader header = new ResponseHeader(0,"AuthenticationException");
            return new ResponseEntity(new BioTokenVerifyRes(false,null,header),HttpStatus.OK);
        }

    }
}
