package com.ripley.commerce.productos.vo;

/**
 * 
 * Clase de configuracion de conexion SFTP
 * 
 */
public class SFTPConfiguration {
	private String host = null;
	private String user = null;
	private String key = null;
	private String password = null;
	private String folder = null;
	private String puerto = null;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
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

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString() {
		return "SFTPConfiguration [host=" + host + ", user=" + user + ", key=" + key + ", folder=" + folder + ", puerto=" + puerto + 
				", password=" + password + "]";
	}
}