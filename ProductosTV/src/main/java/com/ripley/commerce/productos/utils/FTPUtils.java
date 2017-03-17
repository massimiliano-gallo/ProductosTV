package com.ripley.commerce.productos.utils;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.ripley.commerce.productos.vo.FTPConfiguration;

public class FTPUtils {
	/**
	 * 
	 * Funcion que se conecta a un Servidor FTP
	 * 
	 * @param configuration: datos para la conexion al Servidor FTP
	 * @return FTPClient con la conexion efectuada
	 * @throws Exception
	 * 
	 */
	public static FTPClient getFtpConexion(FTPConfiguration configuration) throws Exception {
		FTPClient ftp = new FTPClient();

		//try to connect
		ftp.connect(configuration.getIp());

		//login to server
		if (!ftp.login(configuration.getUser(), configuration.getPassword())) {
			ProductosTVUtils.loguea("Usuario No Valido", "error");
			ftp.logout();
			return null;
		}

		int reply = ftp.getReplyCode();

		//FTPReply stores a set of constants for FTP reply codes. 
		if (!FTPReply.isPositiveCompletion(reply)) {
			ProductosTVUtils.loguea("Error en la conexion...", "error");
			ftp.disconnect();
			return null;
		}

		//enter passive mode
		ftp.enterLocalPassiveMode();
		ftp.setFileType(FTP.BINARY_FILE_TYPE);

		return ftp;
	}//getFtpConexion
	
	/**
	 * 
	 * Funcion que copia unos archivos a un Servidor FTP
	 * 
	 * @param configuration: datos para la conexion al Servidor FTP
	 * @param ftp: cliente FTP conectado
	 * @param files: rutas y nomres de los archivos
	 * @return boolean con el resultado de la copia
	 * @throws Exception
	 * 
	 */
	public static boolean create(FTPConfiguration configuration, FTPClient ftp, String[] files)
								throws Exception {
		ProductosTVUtils.loguea("Copiando archivos al servidor FTP...", "info");
		
		for (int i = 0; i < files.length; i++) {
			if (!files[i].equals("")) {
				//Se rescata el separador de nombres de archivos
				String separator = ProductosTVUtils.getDefaultFileSeparator();

				String[] tokens = ProductosTVUtils.fromST2StringArray(files[i], separator);

				String toFtpFile = configuration.getFolder() + tokens[tokens.length - 1];

				InputStream inputStream = new FileInputStream(files[i]);

				ftp.storeFile(toFtpFile, inputStream);

				inputStream.close();	
			}
		}
		
		ftp.disconnect();
		
		return true;
	}//create
	
	/**
	 * 
	 * Funcion que sube al Servidor FTP los archivos creados en el EXPORT
	 * 
	 * @param configuration: datos para la conexion al Servidor FTP
	 * @param files: nombre de los archivos
	 * @return boolean con el resultado de la copia
	 * @throws Exception
	 * 
	 */
	public static boolean copiaAServidorFTP(FTPConfiguration configuration, String[] files) throws Exception {
		boolean ok = false;
		
		FTPClient ftpClient = FTPUtils.getFtpConexion(configuration);
		
		if (ftpClient.isConnected())  {
			ok = FTPUtils.create(configuration, ftpClient, files);
		} else {
			ProductosTVUtils.loguea("**********Error en conexion FTP...", "error");
		}
    	
    	return ok;
    }
}