package com.compulynx.compas.security.model;

import org.springframework.stereotype.Component;

@Component
public class EncryptionPayloadResp {
    private String encryptedPayload;
    private String encryptedKey;

    public EncryptionPayloadResp(){}

    public String getEncryptedPayload() {
        return encryptedPayload;
    }

    public void setEncryptedPayload(String encryptedPayload) {
        this.encryptedPayload = encryptedPayload;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }
}
