package com.compulynx.compas.customs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.compulynx.compas.security.AESsecure;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;


public class CommonFunctions {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonFunctions.class);
    @Autowired
    private static AESsecure aeSsecure;
	
	public static void disableSslVerification()
            throws KeyManagementException {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

        
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
					// TODO Auto-generated method stub
					
				}
            }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception ex) {
           // log.error("Error in posting " + ex);
        }
    }
 
 public static void configHttpsUrlConnection(HttpsURLConnection httpsURLConnection) {
		try {
			httpsURLConnection.setDoInput(true);
			httpsURLConnection.setDoOutput(true);
			httpsURLConnection.setRequestMethod("POST");
			httpsURLConnection.setConnectTimeout(10000);
			httpsURLConnection.setReadTimeout(10000);
		}catch(Exception ex) {
			ex.printStackTrace();
			//logger.error("Error in configuring HttpsUrlConnection() : "+ex.getMessage());
		}
		
	}
 public static void configHttpUrlConnection(HttpURLConnection httpURLConnection) {
		try {
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(10000);
			httpURLConnection.setReadTimeout(10000);
		}catch(Exception ex) {
			ex.printStackTrace();
			//logger.error("Error in configuring HttpsUrlConnection() : "+ex.getMessage());
		}
		
	}
	public static String convertPojoToJson(Object obj) {
		String getAuthJsonStr ="";
		ObjectMapper mapper = new ObjectMapper();  
        try {  
           
        	getAuthJsonStr = mapper.writeValueAsString(obj);  
        }  
        catch (IOException e) {  
            e.printStackTrace();  
            //logger.error("Error : "+e.getMessage());
        } 
        return getAuthJsonStr;
	}

    public static Date getOneDayPlusDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, +1);
        return calendar.getTime();
    }

    public static String encryptResPayload(Object obj){
        try{
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //System.out.println("SOUT:::"+ow.writeValueAsString(obj));;
            String encrypt = aeSsecure.encrypt(ow.writeValueAsString(obj));

            //System.out.println("ENCRYPTED:::"+encrypt);
            return encrypt;

        }catch (Exception e){
            System.out.println("encryptResPayload Exception: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static HttpHeaders addEncryptionHeaders(String headerValue){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("encKey",headerValue);
        return responseHeaders;
    }
    
}
