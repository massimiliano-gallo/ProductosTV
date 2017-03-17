package com.ripley.commerce.productos.vo;

/**
 * 
 * Clase de configuracion de envio de correos
 * 
 */
public class MailConfiguration {
	private String from = null;
	private String to = null;
	private String host = null;
	private int puerto;
	private String protocolo = null;
	private String subject = null;
	private String mensaje = null;
	private String user = null;
	private String pass = null;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	public String getProtocolo() {
		return protocolo;
	}
	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

	public String toString() {
		return "MailConfiguration [from=" + from + ", to=" + to + ", host=" + host + ", puerto=" + puerto + 
				", protocolo=" + protocolo + ", subject=" + subject + ", mensaje=" + mensaje + ", user=" + user + 
				", pass=" + pass + "]";
	}
}