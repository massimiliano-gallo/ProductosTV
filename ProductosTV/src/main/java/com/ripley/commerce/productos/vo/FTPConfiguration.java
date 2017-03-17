package com.ripley.commerce.productos.vo;

/**
 * 
 * Clase de configuracion de conexion FTP
 * 
 */
public class FTPConfiguration {
	private String ip = null;
	private String user = null;
	private String password = null;
	private String folder = null;
	private String puerto = null;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public String getPuerto() {
		return puerto;
	}
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String toString() {
		return "FTPConfiguration [ip=" + ip + ", user=" + user + ", password=" + password + ", folder=" + folder + 
				", puerto=" + puerto + "]";
	}
}