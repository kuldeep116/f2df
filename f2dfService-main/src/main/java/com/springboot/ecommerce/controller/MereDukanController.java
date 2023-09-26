package com.springboot.ecommerce.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ecommerce.model.MereDukan;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.pojo.RequestF2df;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.service.DukanService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/myDukan")
@Tag(name = "MY Dukan API")
public class MereDukanController {

	@Autowired
	DukanService dukanService;
	private static Logger logger = Logger.getLogger(MereDukanController.class.getName());

	@SuppressWarnings("resource")
	public String saveImageOnServer(String byteCode, String path) {
		String dir = "/home/f2df/ea-tomcat85/webapps";
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

	@PostMapping(value = "/addMyDukan", headers = "Accept=application/json")
	public ResponseEntity<Object> createDukan(@RequestBody MereDukan mereDukan) {
		try {
			String logoBase64String = "";
			if (mereDukan.getLogo() != null) {
				if (mereDukan.getLogo().contains(",")) {
					logoBase64String = mereDukan.getLogo().split(",")[1];
				} else {
					logoBase64String = mereDukan.getLogo();
				}
				String path = saveImageOnServer(logoBase64String,
						"/img/mereDukan" + File.separator + new Date().getTime() + ".jpg");
				mereDukan.setLogo(path);
			}
			String link1 = mereDukan.getDukanLink().substring(mereDukan.getDukanLink().lastIndexOf('/') + 1);
			mereDukan.setDukanLink(link1.replaceAll("\\s", ""));
			int id = dukanService.createDukan(mereDukan);
			return generateResponse("Mydukan save succesfully", id, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("Error occured during add mere dukan " + e.getMessage());
			return generateResponse("myDukan fail to save ", 0, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping(value = "/checkDukanLink", headers = "Accept=application/json")
	public ResponseEntity<Object> checkDukanLink(@RequestBody RequestF2df req) {
		String dukan = req.getMereDukanLink().substring(req.getMereDukanLink().lastIndexOf('/') + 1);
		int id = dukanService.checkDukanExist(dukan.replaceAll("\\s", ""));
		if (id != 0) {
			return generateResponse("This dukan name already use by already another user", id, HttpStatus.OK);
		} else {
			return generateResponse("Dukan name is availble for you !! ", 0, HttpStatus.OK);
		}

	}

	@PostMapping(value = "/getAllDukan", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllDukan(@RequestBody RequestProduct req) {
		List<MereDukan> dukanList = dukanService.getAllDukan(req.getPageNo(), req.getSize());
		return generateResponse("Dukan is exist", dukanList, HttpStatus.OK);

	}

	@PostMapping(value = "/getDukanDetail", headers = "Accept=application/json")
	public ResponseEntity<Object> getDukanDetail(@RequestBody RequestProduct req) {
		List<String> gallaryList = new ArrayList<String>();
		gallaryList.add("/img/banner/11.jpg");
		gallaryList.add("/img/banner/11.jpg");

		List<User> enquiryList = new ArrayList<User>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Get Dukan details");
		map.put("status", HttpStatus.OK);
		map.put("dukanDetails", dukanService.getDukan(req.getId()));
		map.put("gallaryList", gallaryList);
		map.put("reviewCount", 123);

		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

	public static void main(String[] args) {
		String string = "kamalKIApniDukan";
		System.out.println(string.substring(string.lastIndexOf('/') + 1));
	}
}
