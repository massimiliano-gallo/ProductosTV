package com.ripley.commerce.productos.utils;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.ripley.commerce.productos.vo.MailConfiguration;

public class MailUtils {
	/**
	 * 
	 * Funcion que envia un correo electronico
	 * 
	 * @param mailConfiguration: configuraciones para el envio de correos
	 * @param sbCorreo: el texto del correo
	 * @param subject: el asunto del correo
	 * @return boolean con el resultado del envio
	 * 
	 */
	public static boolean envia(MailConfiguration mailConfiguration, StringBuffer sbCorreo, String subject) {
		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", mailConfiguration.getProtocolo());
			props.put("mail.smtp.host", mailConfiguration.getHost());
			props.put("mail.smtp.port", mailConfiguration.getPuerto());

			Session mailSession = Session.getInstance(props);

			Message msg_client = new MimeMessage(mailSession);

			msg_client.setFrom(new InternetAddress(mailConfiguration.getFrom()));

			msg_client.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailConfiguration.getTo()));

			msg_client.setSentDate(new Date());

			msg_client.setSubject(subject);
			
			msg_client.saveChanges();

			MimeMultipart mimeMultipart = new MimeMultipart();

			MimeBodyPart text = new MimeBodyPart();
			text.setDisposition("inline");
			text.setContent(sbCorreo.toString(), "text/plain");
		    
			mimeMultipart.addBodyPart(text);

			msg_client.setContent(mimeMultipart);

			Transport.send(msg_client);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}