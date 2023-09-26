package com.springboot.ecommerce.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class FileUploadUtility {
	
	@SuppressWarnings("resource")
	public String saveImageOnServer(String byteCode, String path) {
		String dir = "/home/f2df/ea-tomcat85/webapps/ROOT";
		byte[] imageByte = Base64.decodeBase64(byteCode);

		try {
			new FileOutputStream(dir + path).write(imageByte);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}
}
