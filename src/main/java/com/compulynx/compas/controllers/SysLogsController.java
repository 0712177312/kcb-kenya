package com.compulynx.compas.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import com.compulynx.compas.customs.responses.GlobalResponse2;
import com.compulynx.compas.models.Country;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.EncryptionPayloadResp;
import com.google.gson.Gson;
//import org.bouncycastle.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.customs.responses.GlobalResponse;
import com.compulynx.compas.models.SysLogs;
import com.compulynx.compas.models.reports.RptSysLogs;
import com.compulynx.compas.customs.CommonFunctions;
import com.compulynx.compas.services.SysLogService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = Api.REST)
public class SysLogsController {
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private AESsecure aeSsecure;
    @Autowired
    private EncryptionPayloadResp encryptionPayloadResp;

    Gson gson = new Gson();

    @PostMapping(value = "/sysLog")
    public ResponseEntity<?> approveCustomerWaive(@RequestHeader HttpHeaders httpHeaders, @RequestBody String encLog, HttpServletRequest request) {
        try {
            List<String> headerList = httpHeaders.getValuesAsList("Key");
            String key = headerList.get(0);

            String decryptedData = aeSsecure.integratedDataDecryption(key,encLog);
            SysLogs log =  new Gson().fromJson(decryptedData,SysLogs.class);
            String ipAddress = request.getRemoteAddr();
            int logs = sysLogService.log(log.getUserId(), log.getActivity(),log.getLoginType(), ipAddress);
            if (logs > 0) {
                return new ResponseEntity<>(new GlobalResponse(
                        "000", "logged", true, GlobalResponse.APIV), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new GlobalResponse(GlobalResponse.APIV, "201",
                        false, "failed to log"), HttpStatus.OK);
            }
        } catch (Exception e) {
            GlobalResponse resp = new GlobalResponse("404", "error processing logs request", false, GlobalResponse.APIV);
            e.printStackTrace();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
    }

    @GetMapping("/gtLogs")
    public ResponseEntity<?> getSysLogs() throws NoSuchAlgorithmException, IOException {
        try {
            List<RptSysLogs> userGroups = sysLogService.getSysLogs();

            if (userGroups.isEmpty()) {

                GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "404",
                        false, "cannot find usergroups",
                        userGroups);
                String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
                encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
                HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
                return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
            }
            GlobalResponse2 globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "usergroups",
                    userGroups);

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

    @GetMapping("/getSystemActivity")
    public ResponseEntity<?> previewCustomers(@RequestHeader HttpHeaders httpHeaders,
            @RequestParam("FromDt")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    String encFromDate,
            @RequestParam(value = "ToDt")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    String encToDate,
            @RequestParam(value = "userId")
                    String encUserId
    ) throws NoSuchAlgorithmException, IOException {

        List<String> headerList = httpHeaders.getValuesAsList("Key");
        String key = headerList.get(0);

        String decFromDate = aeSsecure.integratedDataDecryption(key,encFromDate);
        String decToDate = aeSsecure.integratedDataDecryption(key,encToDate);
        String decUserId = aeSsecure.integratedDataDecryption(key,encUserId);

        Date fromDate =  new Gson().fromJson(decFromDate,Date.class);
        Date toDate =  new Gson().fromJson(decToDate,Date.class);
        String strUserId =  new Gson().fromJson(decUserId,String.class);

        List<RptSysLogs> logs = null;
        if(strUserId.equalsIgnoreCase("all")){
            logs = sysLogService.gtAllSystemLogs(fromDate, toDate);
        }
        else{
            Long userId = Long.parseLong(strUserId);
            logs = sysLogService.gtSystemLogs(fromDate, toDate, userId);
        }

        GlobalResponse2 globalResponse =null;
        if (logs.size() > 0)
             globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "logs found", logs);
        else
            globalResponse = new GlobalResponse2(GlobalResponse.APIV, "000", true, "logs found", logs);

        String jsonObj = CommonFunctions.convertPojoToJson(globalResponse);
        encryptionPayloadResp = aeSsecure.integratedDataEncryption(jsonObj);
        HttpHeaders headers = CommonFunctions.addEncryptionHeaders(encryptionPayloadResp.getEncryptedKey());
        return ResponseEntity.ok().headers(headers).body(encryptionPayloadResp.getEncryptedPayload());
    }

    @GetMapping("/getSystemActivityForExporting")
    public String previewCustomersForExporting(@RequestHeader HttpHeaders httpHeaders,
            @RequestParam("FromDt")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    String encFromDate,
            @RequestParam(value = "ToDt")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    String encToDate,
            @RequestParam(value = "userId")
                    String encUserId) throws NoSuchAlgorithmException, IOException {

        List<String> headerList = httpHeaders.getValuesAsList("Key");
        String key = headerList.get(0);

        String decFromDate = aeSsecure.integratedDataDecryption(key,encFromDate);
        String decToDate = aeSsecure.integratedDataDecryption(key,encToDate);
        String decUserId = aeSsecure.integratedDataDecryption(key,encUserId);

        Date fromDate =  new Gson().fromJson(decFromDate,Date.class);
        Date toDate =  new Gson().fromJson(decToDate,Date.class);
        String strUserId =  new Gson().fromJson(decUserId,String.class);
        Long userId = Long.parseLong(strUserId);

        List<RptSysLogs> logs = sysLogService.gtSystemLogs(fromDate, toDate, userId);
        String response = "";
        response += "username, ";
        response += "activity, ";
        response += "actTime, ";
        response += "actDate";
        response += "\n";
        for (RptSysLogs log : logs) {
            response += log.getUsername() + ", ";
            response += log.getActivity() + ", ";
            response += log.getCreatedAt() + "";
            response += "\n";
        }
        response += "\n";
        if (logs.size() > 0) {
            return response;
        } else {
            return "logs not found";
        }
    }
}
