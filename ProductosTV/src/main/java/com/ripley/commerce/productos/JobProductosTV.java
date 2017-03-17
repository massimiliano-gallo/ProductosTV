package com.ripley.commerce.productos;

import java.io.BufferedReader;
import java.io.FileReader;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.ripley.commerce.productos.email.SendEMail;
import com.ripley.commerce.productos.utils.Constants;
import com.ripley.commerce.productos.utils.FTPUtils;
import com.ripley.commerce.productos.utils.FileExport;
import com.ripley.commerce.productos.utils.ProductosTVUtils;
import com.ripley.commerce.productos.utils.SFTPUtils;
import com.ripley.commerce.productos.vo.FTPConfiguration;
import com.ripley.commerce.productos.vo.MailConfiguration;
import com.ripley.commerce.productos.vo.SFTPConfiguration;

public class JobProductosTV {
	private FileExport fileExport;
	String directoryRoot = "";
	String directoryRespaldoIN = "";
	String directoryRespaldoOUT = "";
	private SFTPConfiguration sftpImportConfiguration;
	private FTPConfiguration ftpConfiguration;
	private MailConfiguration mailConfiguration;
	
	/**
	 * 
	 * Clase que baja el archivo de productos desde un Servidor SFTP, 
	 * 
	 * @throws Exception
	 * 
	 */
	public void initProcess() throws Exception {
		boolean error = false;
		StringBuffer dataToWrite = new StringBuffer();
		String fileName = "";
		String textoCorreo = "";
		ChannelSftp.LsEntry[] sftpFiles = null;
		
		String inicio = ProductosTVUtils.getCurrentDateDDSMMSAAAA() + " a las " + ProductosTVUtils.getCurrentInstantHHDPMMDPSS();

		ProductosTVUtils.loguea("Inicio proceso de elaboracion de archivo de productos: " + inicio, "info");

		//Se rescata la lista de archivos presentes en el Servidor SFTP
		try {
			sftpFiles = listFiles();
			
			if (sftpFiles != null && sftpFiles.length > 0)  {
				ProductosTVUtils.loguea("Se encontraron " + sftpFiles.length + " files en el Servidor SFTP...", "info");

				for (int i = 0; i < sftpFiles.length; i++) {
					//Se bajan los archivos desde el Servidor SFTP
					error = copyFile(sftpFiles[i]);
					
					if (error)  {
						textoCorreo = "No se pudo bajar archivo " + sftpImportConfiguration.getFolder() + sftpFiles[i].getFilename() + " desde el Servidor SFTP";
						textoCorreo += Constants.LINE_SEPARATOR + "Elaboracion de archivo de productos termino con error.";
						ProductosTVUtils.loguea(textoCorreo + "...", "error");
					} else {
						textoCorreo = "Download archivo " + sftpImportConfiguration.getFolder() + sftpFiles[i].getFilename() + " OK...";
						dataToWrite = manageFile(sftpFiles[i].getFilename());
						
						fileName = createFileProducts(dataToWrite);
						
						//Se termina el proceso
						try {
							boolean ok = endProcess(sftpFiles[i].getFilename(), fileName);
							
							if (!ok) {
								ProductosTVUtils.loguea("Error en upload archivo...", "error");
								textoCorreo += Constants.LINE_SEPARATOR + "Error en upload archivo " + directoryRoot + fileName + "...";
								textoCorreo += Constants.LINE_SEPARATOR + "Elaboracion de archivo de productos termino con error.";
							} else {
								textoCorreo += Constants.LINE_SEPARATOR + "Upload archivo " + directoryRoot + fileName + " OK...";
								textoCorreo += Constants.LINE_SEPARATOR + "Elaboracion de archivo de productos termino exitosamente.";	
							}
				    	} catch (Exception e) {
							textoCorreo += Constants.LINE_SEPARATOR + "Error en upload archivo " + directoryRoot + fileName + "...";
							textoCorreo += Constants.LINE_SEPARATOR + "Elaboracion de archivo de productos termino con error.";
							e.printStackTrace();
				    	}
					}
				}//for (int i = 0; i < ftpFiles.length; i++) {
			} else {
				textoCorreo = "No se pudo obtener lista de archivos en Servidor SFTP";
				ProductosTVUtils.loguea(textoCorreo + "...", "error");
				textoCorreo += Constants.LINE_SEPARATOR + "Elaboracion de archivo de productos termino con error.";
			}
    	} catch (JSchException jse) {
			textoCorreo = "No se pudo obtener lista de archivos en Servidor SFTP";
			textoCorreo += Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR + "Elaboracion de archivo de productos termino con error.";
			jse.printStackTrace();
    	}

		ProductosTVUtils.loguea("Fin proceso de elaboracion de archivo de productos: " + ProductosTVUtils.getCurrentDateDDSMMSAAAA() + " a las " + ProductosTVUtils.getCurrentInstantHHDPMMDPSS(), "info");
		
		if (ProductosTVUtils.isToSendInfoMail(mailConfiguration)) {
			//Se envia un correo con el resultado
			sendEMail(mailConfiguration, textoCorreo, inicio);
		} else {
			ProductosTVUtils.loguea("No se envia correo.", "info");
		}
	}

	/**
	 * 
	 * Funcion que devuelve la lista de archivos presentes en la carpeta de trabajo en el Servidor SFTP
	 * 
	 * @return lista de archivos presentes en la carpeta de trabajo en el Servidor SFTP
	 * @throws Exception
	 * 
	 */
	private ChannelSftp.LsEntry[] listFiles() throws Exception {
		ChannelSftp.LsEntry[] sftpFiles = null;

		sftpFiles = SFTPUtils.getFilesInSFTPServerDirectory(sftpImportConfiguration);

		return sftpFiles;
	}

	/**
	 * 
	 * Funcion que baja un archivo desde el Servidor SFTP
	 * 
	 * @param sftpFile: archivo en el Servidor SFTP
	 * @return boolean con el resultado del download
	 * @throws Exception
	 * 
	 */
	private boolean copyFile(ChannelSftp.LsEntry sftpFile) throws Exception {
		boolean error = false;

		error = SFTPUtils.download(sftpImportConfiguration, directoryRoot, sftpFile.getFilename());

		return error;
	}
	
	/**
	 * 
	 * Funcion que elabora el archivo de productos eliminando la primera liena y sacando las dobles comillas
	 * 
	 * @param file: archivo a elaborar
	 * @throws Exception
	 * 
	 */
	private StringBuffer manageFile(String file) throws Exception {
		BufferedReader in = null;
		int numeroLinea = 0;
		StringBuffer dataToWrite = new StringBuffer();
		
		//Se recorre el archivo a importar
		ProductosTVUtils.loguea("Elaborando archivo " + directoryRoot + file + "...", "info");
		numeroLinea = 0;
		in = new BufferedReader(new FileReader(directoryRoot + file));

		//Se lee el archivo linea por linea
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			numeroLinea++;
			ProductosTVUtils.loguea("linea " + numeroLinea + ": " + line, "debug");

			if (!line.equals("")) {
				ProductosTVUtils.loguea("Elaborando linea " + numeroLinea + ": " + line, "debug");

				//La primera linea es de intestacion: no se considera
				//Desde la segunda linea en adelante hay que sacar las dobles comillas
				if (numeroLinea != 1) {
					String lineOK = ProductosTVUtils.getLineCleaned(line);

			    	dataToWrite.append(appendLineValue(lineOK));
				}//if (numeroLinea != 1) {
			}//if (!line.equals("")) {
		}//for (String line = in.readLine(); line != null; line = in.readLine()) {

		in.close();
		
		return dataToWrite;
	}
	
	/**
	 * 
	 * Funcion que termina el proceso:
	 * se copia el archivo entrante en la carpeta IN;
	 * se copia el archivo resultante en la carpeta OUT;
	 * se sube el archivo resultante al Servidor SFTP;
	 * 
	 * @param files: archivos importados
	 * @throws Exception
	 * 
	 */
	private boolean endProcess(String fileIN, String fileOUT) throws Exception {
		//Se copia el archivo entrante en la carpeta IN
		ProductosTVUtils.renameFile(directoryRoot, fileIN, true, directoryRespaldoIN);
		
		//Se suben los archivos creados al Servidor SFTP
		String[] files = {directoryRoot + fileOUT};
		boolean ok = FTPUtils.copiaAServidorFTP(ftpConfiguration, files);
		
		//Se copia el archivo resultante en la carpeta OUT
		ProductosTVUtils.renameFile(directoryRoot, fileOUT, true, directoryRespaldoOUT);
		
		return ok;
	}
	
	/**
	 * 
	 * Funcion que crea el archivo de productos correcto
	 * 
	 * @param dataToWrite: lineas a escribir en el archivo resultante
	 * @return String con el nombre del archivo de productos exportado
	 * @throws Exception
	 * 
	 */
	private String createFileProducts(StringBuffer dataToWrite) throws Exception {
		//Data odierna en formato DDMMYYYY
		//24/02: el archivo tiene que estar nombrado con el dia de manana
//		String newDate = ProductosTVUtils.addDays(0, Constants.DATE_FORMAT_OUT);
		String newDate = ProductosTVUtils.addDays(1, Constants.DATE_FORMAT_OUT);

		//Se crea el archivo con el resultado del EXPORT
		String fileName = Constants.FILE_NAME_OUT + newDate + "." + Constants.FILE_EXTENSION_OUT;
//		fileName = Constants.FILE_NAME_OUT + "01011903" + "." + Constants.FILE_EXTENSION_OUT;
		ProductosTVUtils.loguea("fileName: " + fileName, "info");
		
		fileExport.createFile(directoryRoot + fileName, dataToWrite.toString());
		
		return fileName;
	}
	
	/**
	 * 
	 * Funcion que escribe una linea en el archivo de EXPORT
	 * 
	 * @param lineValue: linea a escribir
	 * @return StringBuffer con la linea para su escritura en el archivo exportado 
	 * @throws Exception
	 * 
	 */
	private StringBuffer appendLineValue(String lineValue) throws Exception {
		return fileExport.saveLine(lineValue);
	}
	
	/**
	 * 
	 * Funcion que envia un correo con el resultado
	 * 
	 * @param mailConfiguration: configuraciones para el envio de correos
	 * @param textoCorreo: texto del correo
	 * @param inicio: hora de inicio del proceso
	 * @throws Exception
	 * 
	 */
	private void sendEMail(MailConfiguration mailConfiguration, String textoCorreo, String inicio) 
							throws Exception {
		SendEMail sem = new SendEMail();
		sem.sendMail(mailConfiguration, textoCorreo, inicio);
	}
	
	public void setFileExport(FileExport fileExport) {
		this.fileExport = fileExport;
	}
	
	public void setDirectoryRoot(String directoryRoot) {
		this.directoryRoot = directoryRoot;
	}

	public void setDirectoryRespaldoIN(String directoryRespaldoIN) {
		this.directoryRespaldoIN = directoryRespaldoIN;
	}
	
	public void setDirectoryRespaldoOUT(String directoryRespaldoOUT) {
		this.directoryRespaldoOUT = directoryRespaldoOUT;
	}
	
	public void setSftpImportConfiguration(SFTPConfiguration sftpImportConfiguration) {
		this.sftpImportConfiguration = sftpImportConfiguration;
	}
	
	public void setFtpConfiguration(FTPConfiguration ftpConfiguration) {
		this.ftpConfiguration = ftpConfiguration;
	}
	
	public void setMailConfiguration(MailConfiguration mailConfiguration) {
		this.mailConfiguration = mailConfiguration;
	}
}