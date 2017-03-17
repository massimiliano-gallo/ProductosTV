package com.ripley.commerce.productos.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.ripley.commerce.productos.vo.SFTPConfiguration;

public class SFTPUtils {
	private static final String CHANNEL_TYPE = "sftp";
	
	/**
	 * 
	 * Funcion que crea un objeto de tipo Session para la conexion al Servidor SFTP a traves de llave publica
	 * 
	 * @param configuration: datos para la conexion al Servidor SFTP
	 * @return Session con la conexion efectuada
	 * @throws Exception
	 * 
	 */
	private static Session getSFTPSession(SFTPConfiguration configuration) throws Exception {
		JSch jsch = new JSch();
		Session session = null;

		boolean isConnectionWithKey = isConnectionWithKey(configuration);
		
		if (isConnectionWithKey) {
			jsch.addIdentity(configuration.getKey());
		}

		session = jsch.getSession(configuration.getUser(), configuration.getHost(), Integer.valueOf(configuration.getPuerto()));

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		
		if (!isConnectionWithKey) {
			session.setPassword(configuration.getPassword());
		}

		if (!session.isConnected()) {
			session.connect();
		}

        return session;
	}//getSFTPSession
	
	/**
	 * 
	 * Funcion que verifica si la conexion al Servidor SFTP es a traves de llave publica
	 * 
	 * @param configuration: datos para la conexion al Servidor SFTP
	 * @return boolean: true si la conexion al Servidor SFTP es a traves de llave publica
	 * @throws Exception
	 * 
	 */
	private static boolean isConnectionWithKey(SFTPConfiguration configuration) throws Exception {
		return (!configuration.getKey().equals("") && configuration.getPassword().equals(""));
	}//isConnectionWithKey
	
	/**
	 * 
	 * Funcion que cierra la conexion al Servidor SFTP
	 * 
	 * @param channel: el canal de conexion
	 * @param channelSftp: el canal de conexion SFTP
	 * @param session: la sesion
	 * @throws Exception
	 * 
	 */
	private static void closeConnections(Channel channel, ChannelSftp channelSftp, Session session)
										throws Exception {
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.exit();
		}

		if (channel != null && channel.isConnected()) {
			channel.disconnect();
		}

		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}//closeConnections
	
	/**
	 * 
	 * Funcion que crea un objeto de tipo Channel para la conexion al Servidor SFTP a traves de llave publica
	 * 
	 * @param session: la sesion
	 * @return objeto de tipo Channel para la conexion al Servidor SFTP
	 * @throws Exception
	 * 
	 */
	private static Channel getSFTPChannel(Session session) throws Exception {
		Channel channel = null;

		channel = session.openChannel(CHANNEL_TYPE);
		channel.connect();

		return channel;
	}
	
	/**
	 * 
	 * Funcion que crea un objeto de tipo ChannelSftp para la conexion al Servidor SFTP a traves de llave publica
	 * 
	 * @param channel: el canal de conexion
	 * @return objeto de tipo ChannelSftp para la conexion al Servidor SFTP
	 * @throws Exception
	 * 
	 */
	private static ChannelSftp getChannelSftp(Channel channel) throws Exception {
		return (ChannelSftp)channel;
	}
	
	/**
	 * 
	 * Funcion que retorna la lista de archivos existentes en una carpeta de un Servidor SFTP cuyo nombre comienza por  
	 * "Publicado_", luego tiene la fecha de hoy dia en formato YYYY-MM-DD, y tiene extencion csv
	 * 
	 * @param configuration: datos para la conexion al Servidor SFTP
	 * @return arreglo de LsEntry con la lista de archivos
	 * @throws Exception
	 * 
	 */
	public static ChannelSftp.LsEntry[] getFilesInSFTPServerDirectory(SFTPConfiguration configuration) throws Exception {
		ProductosTVUtils.loguea("Bajando lista de archivos desde el servidor SFTP...", "debug");

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		int nFiles = 0;
		int nFile = 0;
		ChannelSftp.LsEntry[] result = null;
		
		try {
			session = getSFTPSession(configuration);

			channel = getSFTPChannel(session);
			
			channelSftp = getChannelSftp(channel);

			Vector<ChannelSftp.LsEntry> files = getFilesInSFTPServerDirectory(configuration, channelSftp);

			//Se consideran todos los archivos que cumplen con el patron
			//Se busca para la fecha actual, de no existir el archivo, se verifica si existe el de ayer
			//24/02: se busca el archivo de manana y, si no esta, el de hoy dia
//			for (int i = 0; i <= 1; i++) {
			for (int i = 1; i >= 0; i--) {
				String newDate = ProductosTVUtils.addDays(1 * i, Constants.DATE_FORMAT_IN);
				
				for (ChannelSftp.LsEntry file : files) {
					if (file.getAttrs().isDir()) {
						continue;
					}

					boolean isProductFileNameOK = isProductFileNameOK(file.getFilename(), newDate);

					if (isProductFileNameOK) {
						nFiles++;
						break;
					}
				}

				if (nFiles > 0) {
					result = new ChannelSftp.LsEntry[nFiles];
					
					for (ChannelSftp.LsEntry file : files) {
						if (file.getAttrs().isDir()) {
							continue;
						}

						boolean isProductFileNameOK = isProductFileNameOK(file.getFilename(), newDate);

						if (isProductFileNameOK) {
							result[nFile] = file;
							nFile++;
							return result;
						}
					}
				}
			}
		} finally {
			closeConnections(channel, channelSftp, session);
		}

		return result;
	}//getFilesInSFTPServerDirectory
	
	/**
	 * 
	 * Funcion que retorna la lista de archivos existentes en una carpeta de un Servidor SFTP
	 * 
	 * @param configuration: datos para la conexion al Servidor SFTP
	 * @param channelSftp: el canal de conexion SFTP
	 * @return Vector de LsEntry con la lista de archivos
	 * @throws Exception
	 * 
	 */
	private static Vector<ChannelSftp.LsEntry> getFilesInSFTPServerDirectory(SFTPConfiguration configuration, ChannelSftp channelSftp) 
																			throws Exception {
		ProductosTVUtils.loguea("Rescatando lista de archivos desde el servidor SFTP...", "debug");
		Vector<ChannelSftp.LsEntry> files = null;
		
		channelSftp.cd(configuration.getFolder());
		files = channelSftp.ls("*");

		return files;
	}//getFilesInSFTPServerDirectory
	
	/**
	 * 
	 * Funcion que verifica si el nombre de un archivo comienza con "Publicado_", luego tiene la fecha de hoy dia (o de ayer, si no 
	 * existe el archivo para la fecha actual) en formato YYYY-MM-DD, y tiene extencion csv, para tomarlo en cuenta en el proceso
	 * 
	 * @param fileName: nombre del archivo
	 * @param date: fecha a considerar en el nombre del archivo
	 * @return boolean: true si hay que considerar el archivo
	 * 
	 */
    private static boolean isProductFileNameOK(String fileName, String date) {
		String fileNameOK = Constants.FILE_NAME_IN + Constants.FILE_SEPARADOR_IN + date + "." + Constants.FILE_EXTENSION_IN;

		if (fileName.toUpperCase().equals(fileNameOK.toUpperCase())) {
			return true;
		}

		return false;
	}
    
    /**
	 * 
	 * Funcion que baja un archivo desde un Servidor SFTP
	 * 
	 * @param configuration: datos para la conexion al Servidor SFTP
	 * @param path: ruta del archivo
	 * @param fileName: nombre del archivo
	 * @return boolean con el resultado del download
	 * @throws Exception
	 * 
	 */
	public static boolean download(SFTPConfiguration configuration, String path, String fileName)
									throws Exception {
		ProductosTVUtils.loguea("Bajando archivo " + configuration.getFolder() + fileName + " desde el servidor SFTP...", "info");
		
		boolean ok = false;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try {
			session = getSFTPSession(configuration);

			channel = getSFTPChannel(session);
			
			channelSftp = getChannelSftp(channel);

			channelSftp.get(configuration.getFolder() + fileName, path + fileName);

			ok = true;
		} catch (Exception ex) {
			ProductosTVUtils.loguea("Error en el download SFTP...", "error");
			ex.printStackTrace();
			ok = false;
		} finally {
			closeConnections(channel, channelSftp, session);
		}

		return !ok;
	}//download
	
	/**
	 * 
	 * Funcion que copia unos archivos a un Servidor SFTP
	 * 
	 * @param configuration: datos para la conexion al Servidor SFTP
	 * @param files: nombre de los archivos
	 * @return boolean con el resultado de la copia
	 * @throws Exception
	 * 
	 */
	public static boolean upload(SFTPConfiguration configuration, String file) throws Exception {
		ProductosTVUtils.loguea("Copiando archivos al servidor SFTP...", "info");

		Channel channel = null;
		ChannelSftp channelSftp = null;
		InputStream fis = null;
		Session session = null;
		boolean ok = false;

		try {
			session = getSFTPSession(configuration);

			channel = getSFTPChannel(session);
			
			channelSftp = getChannelSftp(channel);

			try {
				channelSftp.cd(configuration.getFolder());
			} catch (SftpException e) {
				channelSftp.mkdir(configuration.getFolder());
				channelSftp.cd(configuration.getFolder());
			}

			//Se rescata el separador de nombres de archivos
			String separator = ProductosTVUtils.getDefaultFileSeparator();

			String[] tokens = ProductosTVUtils.fromST2StringArray(file, separator);

			String toSftpFile = configuration.getFolder() + tokens[tokens.length - 1];

			fis = new FileInputStream(file);

			channelSftp.put(fis, toSftpFile);

			ok = true;
			fis.close();
		} catch (Exception ex) {
			ProductosTVUtils.loguea("Error en el upload SFTP...", "error");
			ex.printStackTrace();
			ok = false;
		} finally {
			closeConnections(channel, channelSftp, session);
		}

		return ok;
	}//upload
}