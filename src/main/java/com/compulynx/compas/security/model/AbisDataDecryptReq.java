package com.compulynx.compas.security.model;

public class AbisDataDecryptReq {
    private String dataPayload;
    private String keyPayload;

    public AbisDataDecryptReq(){}
    public AbisDataDecryptReq(String dataPayload, String keyPayload) {
        this.dataPayload = dataPayload;
        this.keyPayload = keyPayload;
    }

    public String getDataPayload() {
        return dataPayload;
    }

    public void setDataPayload(String dataPayload) {
        this.dataPayload = dataPayload;
    }

    public String getKeyPayload() {
        return keyPayload;
    }

    public void setKeyPayload(String keyPayload) {
        this.keyPayload = keyPayload;
    }
}
