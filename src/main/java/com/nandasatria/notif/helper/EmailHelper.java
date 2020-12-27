package com.nandasatria.notif.helper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * Class which act as bridge of the smtp services, <b>do not use</b> this directly, 
 * as smtp service was provided for specific purposes
 */
@Service("EmailSender")
public class EmailHelper {

	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private Configuration freemarkerConfig;
	
	@Value("${spring.mail.username}")
	private String username;

	
	/**
	 * Act as {@link JavaMailSender} wrapper will be used and recognized as Self-Service Reset Password. 
	 * This method will send JavaMail MIME message to given recipient with given FreeMarker template.
	 * 
	 * @param to
	 * @param subject
	 * @param bccArray
	 * @param template
	 * @param templateParam
	 * @throws MessagingException
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void sendMail(String to, String subject, String [] bccArray, String template, Map<String, String> templateParam) 
			throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, 
			ParseException, IOException, TemplateException {

		MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Template t = freemarkerConfig.getTemplate(template);
        
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, templateParam);
        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject(subject);
        helper.setFrom(username);
        
        if (bccArray != null) { helper.setBcc(bccArray); }
		
		emailSender.send(mimeMessage);
	}
	
	
	/**
	 * Act as {@link JavaMailSender} wrapper will be used and recognized as Self-Service Reset Password. 
	 * This method will send JavaMail MIME message to given recipient with given FreeMarker template.
	 * 
	 * @param to
	 * @param subject
	 * @param bccArray
	 * @param template
	 * @param templateParam
	 * @throws MessagingException
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void sendMail(String[] to, String subject, String [] bccArray, String template, Map<String, String> templateParam, String additionalContent) 
			throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, 
			ParseException, IOException, TemplateException {

		MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Template t = freemarkerConfig.getTemplate(template);
        
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, templateParam);
        helper.setTo(to);
        helper.setText(html + additionalContent, true);
        helper.setSubject(subject);
        helper.setFrom(username);
        
        if (bccArray != null) { helper.setBcc(bccArray); }
		
		emailSender.send(mimeMessage);
	}
	
	/**
	 * Act as {@link JavaMailSender} wrapper will be used and recognized as iseEmailSender. 
	 * This method will send JavaMail MIME message to given recipient with given FreeMarker template.
	 * 
	 * @param to
	 * @param subject
	 * @param bccArray
	 * @param template
	 * @param keyUrl
	 * @param linkUrl
	 * @throws MessagingException
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Deprecated
	public void sendMail(String to, String subject,  String content) 
			throws MessagingException, IOException {

		MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        
        helper.setTo(to);
        helper.setText(content, true);
        helper.setSubject(subject);
        helper.setFrom(username);
		
		emailSender.send(mimeMessage);
	}

}
