package com.springboot.ecommerce.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Component
public class Util {

	//@Value("${com.carematix.twilio.account-sid}")
	private String ACCOUNT_SID;

	//@Value("${com.carematix.twilio.auth-token}")
	private String AUTH_TOKEN;

	//@Value("${com.carematix.twilio.from-number}")
	private String fromNumber;

	private static Logger logger = Logger.getLogger(Util.class.getName());

	public String postMail(String url, String mailTo, String mailFrom, String subect, String content)
			throws IOException {
		return postMail(url, mailTo, "", mailFrom, subect, content);
	}

	public Object sendSecretCodeMessage(String secretKey, String mobileNumber) throws Exception {
		if (!mobileNumber.contains("+1")) {
			mobileNumber = "+1" + mobileNumber;
		}
		try {
			Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
			Message message = Message.creator(new com.twilio.type.PhoneNumber(mobileNumber),
					new com.twilio.type.PhoneNumber(fromNumber), " F2df : Verification code is " + secretKey)
					.create();
		} catch (Exception e) {
			// logger.("Exception in semding otp. "+e.getLocalizedMessage());

		}
		return null;
	}

	public String postMail(String url, String mailTo, String mailCc, String mailFrom, String subect, String content)
			throws IOException {
		StringBuilder serviceURL = new StringBuilder(url + "?eventid=" + "&mailto=" + mailTo + "&mailfrom=" + mailFrom
				+ "&subject=" + URLEncoder.encode(subect, "UTF-8") + "&content=" + URLEncoder.encode(content, "UTF-8"));

		if (!mailCc.equalsIgnoreCase("")) {
			serviceURL.append("&mailcc=" + mailCc);
		}

		StringBuilder sBuilder = new StringBuilder();
		URL url1 = new URL(serviceURL.toString());
		HttpURLConnection http = (HttpURLConnection) url1.openConnection();

		http.setUseCaches(false);
		http.setRequestMethod("GET");
		http.setRequestProperty("accept", "application/json");
		http.getInputStream();
		http.disconnect();

		return sBuilder.toString();
	}

}
