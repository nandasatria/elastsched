package com.nandasatria.notif.service;

import java.io.IOException;
import java.util.HashMap;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nandasatria.notif.helper.EmailHelper;
import com.nandasatria.util.JsonUtil;
import com.nandasatria.util.UUIDUtil;

import freemarker.template.TemplateException;

@Service("EmailService")
public class EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private EmailHelper emailHelper;

	@Value("${email.template}")
	private String emailTemplate;

	/**
	 * Send email to specific email address for notify
	 * 
	 * @param email input {@link String} param
	 * @return
	 */
	public void sendNotif(String email, HashMap<String, String> param) {
		String transactionId = UUIDUtil.getUUIDtoString();
		this.sendNotif(transactionId, email, param);
	}

	/**
	 * Send email to specific email address for notify
	 * 
	 * @param transactionId input {@link String} param
	 * @param email         input {@link String} param
	 * @return
	 */
	public void sendNotif(String transactionId, String email, HashMap<String, String> param) {

		try {
			emailHelper.sendMail(email, "Alert Elastic", null, emailTemplate, param);
			LOGGER.info(transactionId + "| Send email to user [" + email + "] Success");

		} catch (MessagingException | IOException | TemplateException ex) {
			LOGGER.error("Send email to user  [" + email + "] Failed with error : " + ex.getMessage());
		}
	}

	public void sendNotif(String transactionId, String[] email, HashMap<String, String> param,
			String additionalContent) {

		try {

			emailHelper.sendMail(email, "Alert Elastic", null, emailTemplate, param, additionalContent);
			LOGGER.info(transactionId + "| Send email to user [" + JsonUtil.writeToString(email) + "] Success");

		} catch (MessagingException | IOException | TemplateException ex) {
			LOGGER.error("Send email to user  [" + JsonUtil.writeToString(email) + "] Failed with error : "
					+ ex.getMessage());
		}
	}

	public void sendNotif(String transactionId, String[] email, String subject, HashMap<String, String> param,
			String additionalContent) {

		try {

			emailHelper.sendMail(email, subject, null, emailTemplate, param, additionalContent);
			LOGGER.info(transactionId + "| Send email to user [" + JsonUtil.writeToString(email) + "] Success");

		} catch (MessagingException | IOException | TemplateException ex) {
			LOGGER.error("Send email to user  [" + JsonUtil.writeToString(email) + "] Failed with error : "
					+ ex.getMessage());
		}
	}

}
