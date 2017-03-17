package com.ripley.commerce.productos;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProductosTV {
	/**
	 * 
	 * Clase que prueba a mover archivos en un Servidor SFTP
	 * 
	 * @param args
	 * 
	 */
    public static void main(String[] args) {
    	ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-config.xml");

    	try {
    		JobProductosTV job = (JobProductosTV) cxt.getBean("jobProductosTV");
    		job.initProcess();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}