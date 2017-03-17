package com.ripley.commerce.productos.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UnixUtils {
	/**
	 * 
	 * Funcion que mueve archivos a traves del comando mv de UNIX
	 * 
	 * @param oldName: archivo a mover comprensivo de la ruta
	 * @param newName: nuevo nombre del archivo comprensivo de la ruta de destino
	 * 
	 * @throws Exception
	 * 
	 */
	public static void sendMvCommand(String oldName, String newName) throws Exception {
		ProductosTVUtils.loguea("Enviando comando MV...", "debug");

		String s = null;

		try {
			// run the Unix "mv" command
			// using the Runtime exec method:
			ProductosTVUtils.loguea("Moviendo archivo " + oldName + " a " + newName, "info");
			String command = "mv " + oldName + " " + newName;
			Process p = Runtime.getRuntime().exec(command);
			ProductosTVUtils.loguea("mv OK...", "info");
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			// read the output from the command
			ProductosTVUtils.loguea("Here is the standard output of the command:\n", "info");
			while ((s = stdInput.readLine()) != null) {
				ProductosTVUtils.loguea(s, "info");
			}

			// read any errors from the attempted command
			ProductosTVUtils.loguea("Here is the standard error of the command (if any):\n", "info");
			while ((s = stdError.readLine()) != null) {
				ProductosTVUtils.loguea(s, "info");
			}
		} catch (IOException e) {
			ProductosTVUtils.loguea("Exception happened - here's what I know:", "error");
			e.printStackTrace();
		}
	}//sendMvCommand
}