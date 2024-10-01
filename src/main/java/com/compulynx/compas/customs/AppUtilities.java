package com.compulynx.compas.customs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 
 * @author Ceasar
 *
 */
public class AppUtilities {

	public static final String ERROR = " ERROR: ";
	public static final String STACKTRACE = " STACKTRACE: ";

	
	public static String generateField37() {
		return "DE37" + UUID.randomUUID().toString();
	}

	/**
	 * Log pre string.
	 *
	 * @return the string
	 */
	public static String logPreString() {
		return " iPrint Solution | " + Thread.currentThread().getStackTrace()[2].getClassName() + " | "
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + " | "
				+ Thread.currentThread().getStackTrace()[2].getMethodName() + "() | ";
	}

	public static String generateUUID() {
		String randomUUIDString = "";
		try {
			MessageDigest salt = MessageDigest.getInstance("SHA-256");
			salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
			randomUUIDString = Bytes2HexString(salt.digest());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());

			randomUUIDString = UUID.randomUUID().toString().replace("-", "");
		}
		return randomUUIDString;
	}

	/**
	 * Get exception string stack trace
	 *
	 * @param ex
	 * @return
	 */
	public static String getExceptionStacktrace(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}

	public String anyDate(String format) {
		try {
			if ("".equals(format)) {
				format = "yyyy-MM-dd HH:mm:ss"; // default
			}
			java.util.Date today;
			SimpleDateFormat formatter;

			formatter = new SimpleDateFormat(format);
			today = new java.util.Date();
			return (formatter.format(today));
		} catch (Exception ex) {
			System.out.println(" \n**** anyDate ****\n" + ex.getMessage());
		}
		return "";
	}

	public AppUtilities() {

	}

	public static void printHexString(String hint, byte[] b) {
		System.out.print(hint);
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase() + " ");
		}
		System.out.println("");
	}

	public static String Bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[8];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < 8; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	public static BigDecimal round(BigDecimal d, int scale, boolean roundUp) {
		int mode = (roundUp) ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN;
		return d.setScale(scale, mode);
	}

	public String FormatAmountFromSwitch(String amount, int rootdivide) {
		if (amount.contains("C000")) {
			amount = amount.substring(4, amount.length());
		} else {
			amount = "-" + amount.substring(4, amount.length());
		}
		BigDecimal root = new BigDecimal(rootdivide);
		BigDecimal decimalamount = new BigDecimal(amount);
		decimalamount = decimalamount.divide(root);
		decimalamount = round(decimalamount, 2, true);
		return String.valueOf(decimalamount);
	}

	public String FormatAmountToSwitch(String amount, int rootmultiply) {
		BigDecimal root = new BigDecimal(rootmultiply);
		BigDecimal decimalamount = new BigDecimal(amount);
		decimalamount = round(decimalamount, 2, true);
		decimalamount = decimalamount.multiply(root);
		decimalamount = round(decimalamount, 0, true);
		return String.valueOf(decimalamount);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS yyyy-MM-dd HH:mm:ss
	 * 
	 * yyyy-MM-dd HH:mm:ss YYMMdd yyyy-MM-dd HH:mm:ss md yyyy-MM-dd hh:mm:ss a
	 * 
	 * DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	 * Timestamp date = new Timestamp(dateFormat.parse("04/04/2014 21:07:13.897"
	 * ).getTime());
	 * 
	 * // format variables into 0-filled strings String formattedDate =
	 * String.format("%011d", date.getTime() / 1000);
	 *
	 * "HH:mm:ss.SSSZ" "EEE, d MMM yyyy HH:mm:ss Z" "EEE, d MMM yyyy,HH:mm:ss"
	 *
	 * NB: CC is nothing but the first two digits of the year which mean the century
	 *
	 * @param format
	 * @return
	 */
	public static String genericDatetime(String format) {

		String date = "";
		try {
			ZonedDateTime zonedDateTime = ZonedDateTime.now();

			if (!format.trim().isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				date = formatter.format(zonedDateTime);
			} else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				date = formatter.format(zonedDateTime);
			}
		} catch (Exception ex) {
			System.out.println(getExceptionStacktrace(ex));
		}
		return date;
	}

	public static String formatAmountAddCommaSeparator(String amountToFormat) {
		DecimalFormat formatter = new DecimalFormat("#,###.##");
		return formatter.format(Double.parseDouble(amountToFormat));
	}

	public static java.sql.Date convertStringToJavaSqlDate(String dateToParse) {
		try {
			return (java.sql.Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(dateToParse);
		} catch (Exception ex) {
			System.out.println(" \n**** anyDate ****\n" + ex.getMessage());
		}
		return new java.sql.Date(Calendar.getInstance().getTime().getTime());

	}




	public static String subtractDaysFromCurrentDateReturnDate(int daysToSubtract) {
		String theDate = "";
		try {

			LocalDateTime currentDate = LocalDateTime.now().minusDays(daysToSubtract);
			theDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		} catch (Exception ex) {
			System.out.println(" \n**** Exception ****\n" + ex.getMessage());
		}
		return theDate;

	}

	public static JsonObject convertJsonStringToJsonObject(String jsonString){
		JsonObject obj = new JsonObject();
		try {
			Gson gson = new Gson();
			obj = gson.fromJson(jsonString, JsonObject.class);
			
		} catch (Exception ex) {
			System.out.println(" \n**** Exception ****\n" + ex.getMessage());
		}
		return obj;
	}

}
