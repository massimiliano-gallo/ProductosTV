package com.ripley.commerce.productos.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.ripley.commerce.productos.vo.MailConfiguration;

public class ProductosTVUtils {
	private static final Logger log = Logger.getLogger(ProductosTVUtils.class);
	
    /**
	 * Funcion que retorna el caracter por defecto de separacion de los nombres de archivos/carpetas.
	 * En los sistemas UNIX sera '/'; en los sistemas Microsoft Windows sera '\'.
	 * 
	 * @return valor del separador
	 * 
	 */
	public static String getDefaultFileSeparator() {
		return File.separator;
	}
	
    /**
	 * 
	 * Funcion que separa un String segun un delimitador
	 * 
	 * @param value: el valor
	 * @param delim: el delimitador
	 * @return arreglo de String con el String originario separado
	 * 
	 */
    private static String[] fromST2StringArrayST(String value, String delim) {
		StringTokenizer st = new StringTokenizer(value, delim);
		String[] result = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			result[i] = st.nextToken();
			i++;
		}
		return result;
	}
    
	/**
	 * 
	 * Funcion que separa un String segun un delimitador
	 * 
	 * @param value: el valor
	 * @param delim: el delimitador
	 * @return arreglo de String con el String originario separado
	 * 
	 */
    public static String[] fromST2StringArray(String value, String delim) {
    	if (delim.length() > 1) {
    		return fromST2StringArrayST(value, delim);
    	}
    	
    	String ultimo = value.substring(value.length() - 1, value.length());
    	
    	int n = 0;
		String character = "";
		String element = "";
		
		for (int i = 0; i < value.length(); i++) {
			character = value.substring(i, i + 1);
			if (character.equals(delim)) {
				n++;
			}
		}
		
    	String[] result = null;
    	if (!ultimo.equals(delim)) {
        	result = new String[n + 1];
    	} else {
        	result = new String[n];
    	}
    	
    	n = 0;
		while (value.indexOf(delim) != -1) {
			element = value.substring(0, value.indexOf(delim));

			result[n] = element;
			
			value = value.substring(value.indexOf(delim) + 1, value.length());
			
			n++;
		}
		
		if (!ultimo.equals(delim)) {
			result[n] = value;
		}
		
		return result;
	}
    
	/**
	 * 
	 * Funcion que retorna la extension de un archivo
	 * 
	 * @param nombreArchivo: nombre del archivo
	 * @return la extension del archivo
	 * 
	 */
    public static String getExtension(String nombreArchivo) {
    	String estension = "";
    	
		int posicionPunto = nombreArchivo.indexOf(".");
		
		if (posicionPunto != -1) {
			String[] tokens = fromST2StringArray(nombreArchivo, ".");
			estension = tokens[tokens.length - 1];
		}
		
		return estension;
	}
    
	/**
	 * 
	 * Funcion que retorna el dia actual
	 * 
	 * @return el dia
	 * 
	 */
    private static int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}//getCurrentDay
	
	/**
	 * 
	 * Funcion que retorna el mes actual
	 * 
	 * @return el mes
	 * 
	 */
    private static int getCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}//getCurrentMonth
	
	/**
	 * 
	 * Funcion que retorna el ano actual
	 * 
	 * @return el ano
	 * 
	 */
    private static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}//getCurrentYear
	
	/**
	 * 
	 * Funcion que retorna la hora actual
	 * 
	 * @return la hora
	 * 
	 */
    private static int getCurrentHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}//getCurrentHour
	
	/**
	 * 
	 * Funcion que retorna el minuto actual
	 * 
	 * @return el minuto
	 * 
	 */
    private static int getCurrentMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}//getCurrentMinute
	
	/**
	 * 
	 * Funcion que retorna el segundo actual
	 * 
	 * @return el segundo
	 * 
	 */
    private static int getCurrentSecond() {
		return Calendar.getInstance().get(Calendar.SECOND);
	}//getCurrentSecond
    
	/**
	 * 
	 * Funcion que retorna el valor de un parametro rellenado, a la derecha o a la izquierda con un caracter de relleno, hasta que 
	 * alcance el largo querido
	 * 
	 * @param source: valor del parametro
	 * @param length: largo querido
	 * @param padChar: caracter de relleno
	 * @param side: lado de relleno (right o left)
	 * @return valor del parametro rellenado
	 * 
	 */
    public static String padField(String source, int length, char padChar, String side) {
		int padLength = length - source.length();
		StringBuffer padder = new StringBuffer();
		
		for (int i = 0; i < padLength; i++) {
			padder.append(padChar);
		}
		
		if (side.equalsIgnoreCase("right")) {
			return source + padder.toString();
		}
		
		padder.append(source);
		return padder.toString();
	}//padField
    
	/**
	 * 
	 * Funcion que retorna la fecha actual en formato DD/MM/AAAA
	 * 
	 * @return la fecha
	 * 
	 */
    public static String getCurrentDateDDSMMSAAAA() {
		String day = padField("" + getCurrentDay(), 2, '0', "left");
		String month = padField("" + getCurrentMonth(), 2, '0', "left");
		int year = getCurrentYear();
		return day + "/" + month + "/" + year;
	}//getCurrentDateDDSMMSAAAA
    
	/**
	 * 
	 * Funcion que retorna la fecha actual en formato AAAA-MM-DD
	 * 
	 * @return la fecha
	 * 
	 */
    public static String getCurrentDateAAAASMMSDD() {
		String day = padField("" + getCurrentDay(), 2, '0', "left");
		String month = padField("" + getCurrentMonth(), 2, '0', "left");
		int year = getCurrentYear();
		return year + Constants.FECHA_SEPARADOR_IN + month + Constants.FECHA_SEPARADOR_IN + day;
	}//getCurrentDateAAAASMMSDD
    
	/**
	 * 
	 * Funcion que retorna la hora actual en formato HH:MM:SS
	 * 
	 * @return la hora
	 * 
	 */
    public static String getCurrentInstantHHDPMMDPSS() {
		String hour = padField("" + getCurrentHour(), 2, '0', "left");
		String minute = padField("" + getCurrentMinute(), 2, '0', "left");
		String second = padField("" + getCurrentSecond(), 2, '0', "left");
		return hour + ":" + minute + ":" + second;
	}//getCurrentInstantHHDPMMDPSS
    
    /**
	 * 
	 * Funcion que retorna la fecha actual en formato AAAAMMDD
	 * 
	 * @return la fecha
	 * 
	 */
    public static String getCurrentDateAAAAMMDD() {
		String day = padField("" + getCurrentDay(), 2, '0', "left");
		String month = padField("" + getCurrentMonth(), 2, '0', "left");
		int year = getCurrentYear();
		return year + month + day;
	}//getCurrentDateAAAAMMDD
    
	/**
	 * 
	 * Funcion que retorna la hora actual en formato HHMMSS
	 * 
	 * @return la hora
	 * 
	 */
    public static String getCurrentInstantHHMMSS() {
		String hour = padField("" + getCurrentHour(), 2, '0', "left");
		String minute = padField("" + getCurrentMinute(), 2, '0', "left");
		String second = padField("" + getCurrentSecond(), 2, '0', "left");
		return hour + minute + second;
	}//getCurrentInstantHHMMSS
	
	/**
	 * 
	 * Funcion que maneja el debug
	 * 
	 * @param texto: valor del texto que se quiere imprimir
	 * @param type: tipo de debug
	 * 
	 */
	public static void loguea(String texto, String type) {
		if (type.toLowerCase().equals("info")) {
			log.info("**********" + texto);
		} else if (type.toLowerCase().equals("debug")) {
			log.debug("**********" + texto);
		} else if (type.toLowerCase().equals("error")) {
			log.error("**********" + texto);
		}
	}
	
	/**
	 * Funcion que suma o resta dias a la fecha actual
	 * 
	 * @param days: numero de dias a sumar o restar.
	 * 
	 * @return la fecha resultante en el formato deseado
	 * 
	 */
	public static String addDays(int days, String dateFormatNeeded) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormatNeeded);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);    
        return dateFormat.format(cal.getTime());
	}//addDays
	
	/**
	 * 
	 * Funcion que limpia las lineas del archivo eliminando las dobles comillas
	 * 
	 * @param line: linea actual
	 * @return String con la linea sin las dobles comillas
	 * @throws Exception
	 * 
	 */
	public static String getLineCleaned(String line) throws Exception {
		return line.substring(1, line.length() - 1);
	}
	
    /**
	 * 
	 * Funcion que renombra un archivo (lo mueve de una carpeta a otra)
	 * 
	 * @param directoryRoot: carpeta de referencia donde copiar el archivo
	 * @param oldFileName: ruta y nombre del archivo
	 * @param addTimestamp: boolean que indica si hay que agregar fecha y hora en el nuevo nombre o si hay que dejar el nombre que 
	 * 						viene
	 * @param nameWithPath: boolean que indica si el nombre del archivo viene con la ruta o solo es el nombre
	 * 
	 */
    public static void renameFile(String directoryRoot, String oldFileName, boolean addTimestamp, String directoryRespaldo) 
    								throws Exception {
		if (!nullToDef(oldFileName, "").equals("")) {
			String old = directoryRoot + oldFileName;

			loguea("Renombrando archivo " + old + "...", "info");

			if (!FileUtils.fileExists(old)) {
				loguea("File " + old + " no existe...", "error");
			} else {
				String dir = directoryRespaldo;

				if (!FileUtils.directoryExists(dir)) {
					loguea("Creando carpeta " + dir + "...", "info");

					boolean success = FileUtils.makeDirectory(dir);

					if (success) {
						loguea("Se creo la carpeta " + dir + "...", "debug");
					} else {
						loguea("Imposible crear la carpeta " + dir + "...", "debug");
					}
				}

				String newName = getNewFileName(dir, old, addTimestamp);

				if (!FileUtils.moveFile(old, newName)) {
					loguea("Imposible renombrar el archivo " + old + " a traves del objeto JAVA java.io.File...", "error");
					loguea("Intentando con EXEC...", "info");

					UnixUtils.sendMvCommand(old, newName);
					loguea("Archivo " + old + " renombrado en " + newName, "info");
				} else {
					loguea("Archivo " + old + " renombrado en " + newName, "debug");
				}
			}
		}//if (!nullToDef(files[i], "").equals("")) {
	}
    
	/**
	 * 
	 * Funcion que verifica si un String es nulo y en ese caso devuelve un valor por defecto, devuelve el mismo valor si no es nulo
	 * 
	 * @param value: el valor
	 * @param defaultStr: el valor a devolver en caso de parametro nulo
	 * @return el valor
	 * 
	 */
	public static String nullToDef(String value, String defaultStr) {
		if (value != null && !value.trim().equals("")) {
			return value.trim();
		}
		
		return defaultStr;
	}//nullToDef
	
	/**
	 * 
	 * Funcion que retorna el nuevo nombre del archivo para su copia en la carpeta de respaldo
	 * 
	 * @param newDir: carpeta de referencia donde copiar el archivo
	 * @param oldFileName: ruta y nombre del archivo
	 * @param addTimestamp: boolean que indica si hay que agregar fecha y hora en el nuevo nombre o si hay que dejar el nombre que 
	 * 						viene
	 * @return nombre del archivo
	 * 
	 */
    private static String getNewFileName(String newDir, String oldFileName, boolean addTimestamp) {
	    String newName = "";
	    
		//Se rescata el separador de nombres de archivos
		String separator = getDefaultFileSeparator();

		//Se elimina la directory en el nombre del archivo
		String oldName = getFileName(oldFileName, separator);

	    if (addTimestamp) {
	    	String fin = getCurrentDateAAAAMMDD() + "_" + getCurrentInstantHHMMSS();
    		newName = newDir + oldName + "." + fin;
	    } else {
	    	newName = newDir + oldName;
	    }

	    return newName;
	}
    
	/**
	 * 
	 * Funcion que rescata el nombre de un archivo eliminando la directory
	 * 
	 * @param file: nombre del archivo con ruta
	 * @param separator: separador de carpetas
	 * @return nombre del archivo
	 * 
	 */
	public static String getFileName(String file, String separator) {
		if (!file.equals("")) {
			String[] fileArray = fromST2StringArray(file, separator);
			return fileArray[fileArray.length - 1];
		}

		return "";
	}
	
	/**
	 * 
	 * Funcion que verifica si hay que enviar un correo informativo: esto ocurre si, en el archivo de propiedades, esta indicado el 
	 * destinatario
	 * 
	 * @param mailConfiguration: configuraciones para el envio de correos
	 * @return true si hay que enviar el correo
	 * 
	 */
    public static boolean isToSendInfoMail(MailConfiguration mailConfiguration) {
		if (!mailConfiguration.getTo().equals("")) {
			return true;
		}
		
		return false;
	}//isToSendInfoMail
}