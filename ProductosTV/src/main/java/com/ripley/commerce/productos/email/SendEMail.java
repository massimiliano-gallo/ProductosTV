package com.ripley.commerce.productos.email;

import com.ripley.commerce.productos.utils.MailUtils;
import com.ripley.commerce.productos.utils.ProductosTVUtils;
import com.ripley.commerce.productos.vo.MailConfiguration;

public class SendEMail {
	/**
	 * 
	 * Funcion que envia un correo con el resultado del proceso.
	 * 
	 * @param mailConfiguration: configuraciones para el envio de correos
	 * @param textoCorreo: texto del correo
	 * @param inicio: hora de inicio del proceso
	 * @throws Exception
	 * 
	 */
	public void sendMail(MailConfiguration mailConfiguration, String textoCorreo, String inicio) throws Exception {
		//Se envia mail
		StringBuffer sbCorreo = new StringBuffer(textoCorreo);
		
		ProductosTVUtils.loguea("sbCorreo: ", "debug");
		ProductosTVUtils.loguea(sbCorreo.toString(), "debug");
		
		String subject = "Elaboracion de archivo de productos del día " + inicio;
		
		boolean mailEnviada = MailUtils.envia(mailConfiguration, sbCorreo, subject);
		ProductosTVUtils.loguea("envio mail OK? " + mailEnviada, "info");
	}
}