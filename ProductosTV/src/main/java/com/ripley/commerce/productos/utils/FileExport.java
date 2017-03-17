package com.ripley.commerce.productos.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileExport {
	/**
	 * 
	 * Funcion que cierra un archivo
	 * 
	 * @param out: Writer que necesita ser cerrado
	 * @throws Exception
	 * 
	 */
    private void closeFile(Writer out) throws Exception {
    	if (out != null) {
    		out.close();
		}
    }
    
	/**
	 * 
	 * Funcion que escribe una linea
	 * 
	 * @param lineValue: linea a escribir
	 * @return StringBuffer con la linea yel salto de linea al final
	 * @throws Exception
	 * 
	 */
	public StringBuffer saveLine(String lineValue) throws Exception {
    	StringBuffer sbRow = new StringBuffer();
    	sbRow.append(lineValue);
    	sbRow.append(Constants.LINE_SEPARATOR);
    	return sbRow;
    }
	
	/**
	 * 
	 * Funcion que crea un archivo
	 * 
	 * @param fileName: nombre del archivo
	 * @param data: datos a escribir
	 * @throws Exception
	 * 
	 */
    public void createFile(String fileName, String data) throws Exception {
    	Writer out = null;

    	try {
    		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME));
    		out.append(data);
    		out.flush();
    	} finally {
    		closeFile(out);
    	}
    }
}