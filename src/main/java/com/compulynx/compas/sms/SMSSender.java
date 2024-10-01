package com.compulynx.compas.sms;


import java.io.IOException;
import java.util.Date;

import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class SMSSender {

    @Autowired
    private Environment env;

    private TimeFormatter tF = new AbsoluteTimeFormatter();

    /*
     * This method is used to send SMS to for the given MSISDN
     */
    public void sendTextMessage(String MSISDN, String customerName)
    {

        System.out.println("smsUsername: " + env.getProperty("smsUsername"));
        System.out.println("smsPassword: " + env.getProperty("smsPassword"));
        System.out.println("smsHostName: " + env.getProperty("smsHostName"));
        System.out.println("smsPort: " + env.getProperty("smsPort"));


        // bind param instance is created with parameters for binding with SMSC
        BindParameter bP = new BindParameter(
                BindType.BIND_TX,
                env.getProperty("smsUsername"),
                env.getProperty("smsPassword"),
                "cp",
                TypeOfNumber.UNKNOWN,
                NumberingPlanIndicator.UNKNOWN,
                null);

        SMPPSession smppSession = null;

        try
        {
            // smpp session is created using the bindparam and the smsc ip address/port
            smppSession = new SMPPSession(env.getProperty("smsHostName"), Integer.parseInt(env.getProperty("smsPort")), bP);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }

        // Sample TextMessage
        String message = "Test message to: " + customerName;

//        GeneralDataCoding dataCoding = new GeneralDataCoding(false, true,
//                MessageClass.CLASS1, Alphabet.ALPHA_DEFAULT);
        GeneralDataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false);

        ESMClass esmClass = new ESMClass();

        try
        {
            // submitShortMessage(..) method is parametrized with necessary
            // elements of SMPP submit_sm PDU to send a short message
            // the message length for short message is 140
            smppSession.submitShortMessage(
                    "CMT",
                    TypeOfNumber.NATIONAL,
                    NumberingPlanIndicator.ISDN,
                    MSISDN,
                    TypeOfNumber.NATIONAL,
                    NumberingPlanIndicator.ISDN,
                    MSISDN,
                    esmClass,
                    (byte) 0,
                    (byte) 0,
                    tF.format(new Date()),
                    null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT),
                    (byte) 0,
                    dataCoding,
                    (byte) 0,
                    message.getBytes());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
     * The SMPP system recongnizes phones of the form 245XXXXXXXXX
     */
    public static String convertPhoneToFormatThatSmsGatewayRecognizes(String phoneNumber) {
        // if number begins with +, then remove the +
        if (phoneNumber.startsWith("+")) {
            return phoneNumber.substring(1);
        } else if (phoneNumber.startsWith("0")) {
            // if number begins with 0, then replace the 0 with 254
            phoneNumber = phoneNumber.substring(1);
            return "254" + phoneNumber;
        }else{
            return phoneNumber;
        }
    }

}
