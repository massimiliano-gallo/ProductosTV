package com.ripley.commerce.productos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	/**
	 * 
	 * Funcion que mueve un archivo
	 * 
	 * @param oldFileName: nombre antiguo del archivo
	 * @param newFileName: nombre nuevo del archivo
	 * @return boolean con el resultado del movimiento del archivo
	 * 
	 */
	public static boolean moveFile(String oldFileName, String newFileName) {
		return getFile(oldFileName).renameTo(getFile(newFileName));
	}
	
	/**
	 * 
	 * Funcion que verifica si un archivo existe
	 * 
	 * @param fileName: nombre del archivo
	 * @return boolean que indica si el archivo existe
	 * 
	 */
	public static boolean fileExists(String fileName) {
		return getFile(fileName).exists();
	}
	
	/**
	 * 
	 * Funcion que verifica si una carpeta existe
	 * 
	 * @param dir: nombre de la carpeta
	 * @return boolean que indica si la carpeta existe
	 * 
	 */
	public static boolean directoryExists(String dir) {
		return getFile(dir).isDirectory();
	}
	
	/**
	 * 
	 * Funcion que crea una carpeta
	 * 
	 * @param dir: nombre de la carpeta
	 * @return boolean con el resultado de la creacion de la carpeta
	 * 
	 */
	public static boolean makeDirectory(String dir) {
		return getFile(dir).mkdirs();
	}
	
	/**
	 * 
	 * Funcion que crea un objeto de tipo File
	 * 
	 * @param path: nombre y ruta del archivo
	 * @return File con el archivo creado
	 * 
	 */
	private static File getFile(String path) {
		return new File(path);
	}
	
	/**
	 * 
	 * Funcion que copia un archivo
	 * 
	 * @param oldFileName: nombre antiguo del archivo
	 * @param newFileName: nombre nuevo del archivo
	 * @throws Exception
	 * 
	 */
	public static void copyFile(String oldFileName, String newFileName) throws Exception {
		InputStream inStream = null;
		OutputStream outStream = null;
		
    	try {
    		File afile = getFile(oldFileName);
    		File bfile = getFile(newFileName);

    		inStream = new FileInputStream(afile);
    		outStream = new FileOutputStream(bfile);

    		byte[] buffer = new byte[1024];

    		int length;
    		
    		//copy the file content in bytes
    		while ((length = inStream.read(buffer)) > 0) {
    			outStream.write(buffer, 0, length);
    		}
		} finally {
			inStream.close();
			outStream.close();
		}
	}
}