package com.springboot.ecommerce.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ecommerce.model.Images;
import com.springboot.ecommerce.model.News;
import com.springboot.ecommerce.pojo.ImageDTO;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.pojo.ResultDTO;
import com.springboot.ecommerce.repository.CommonDao;
import com.springboot.ecommerce.service.HomeService;
import com.springboot.ecommerce.service.SiteMapService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/news")
@Tag(name = "News API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsController {

	@Autowired
	CommonDao dao;

	@Autowired
	HomeService homeService;
	
	@Autowired
	private SiteMapService siteMapService;

	private static Logger logger = Logger.getLogger(NewsController.class.getName());
	private static String UPLOAD_DIR = "/home/f2df/ea-tomcat85/webapps/img/News/";
	public static Map<String, Object> newsmap = new HashMap<String, Object>();

	@PostMapping(value="/uploadImg")
    public ResponseEntity<Object> uploadFile(@RequestBody ImageDTO news) throws IOException {
		String imageName ="";
		for (String file : news.getMultiImages()) {
			System.out.println("file");
			Images imgObj = new Images();
			if (file.isEmpty()) {
				break;
			}
			
		   imageName ="news_"+new Date().getTime()+".jpg";
			String uploadFilePath = UPLOAD_DIR +imageName ;
			System.out.println("file name"+uploadFilePath);
			byte[] imageByte = Base64.decodeBase64(file.split(",")[1]);
			new FileOutputStream("/home/f2df/ea-tomcat85/webapps/img/News_temp/"+imageName).write(imageByte);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Img save successfully");
		map.put("status", HttpStatus.OK);
		map.put("data", imageName);
		return new ResponseEntity<Object>(map, HttpStatus.OK);
		
	}

	
	@PostMapping(value = "/add", headers = "Accept=application/json")
	public ResponseEntity<Object> createNews(@RequestBody ImageDTO news) {
		System.out.println(news.getHeading());
		//byte[] valueDecoded = Base64.decodeBase64(news.getHeading());
		//String heading = new String(valueDecoded, StandardCharsets. UTF_8);
		//System.out.println("Decoded value is " + heading);
		News newsObj = new News();
		List<Images> imagesObj;
		try { 
			imagesObj = saveUploadedFiles(news.getMultiImages(),news.getImageName().trim());
			newsObj.setHeading(news.getHeading());
			newsObj.setDetails(news.getDesc());
			newsObj.setImages(imagesObj);
			newsObj.setCat_ids(String.join(",", news.getCategoryId()));
			newsObj.setVideoLink(news.getVideoLink());
			newsObj.setImages(imagesObj);
			newsObj.setTag(news.getTag());
			newsObj.setCreateDate(new Date());
			newsObj.setStatus("0");
			newsObj.setImageName(news.getImageName().trim());
			newsObj.setAltTag(news.getAltTag());
			newsObj.setKeywords(news.getKeywords());
			newsObj.setDescription(news.getDescription());
			newsObj.setTitle(news.getTitle());
			dao.add(newsObj);
			return generateResponse("News save succesfully", "OK", HttpStatus.OK);
		} catch (IOException e) {
			logger.debug("Error in add news " + e.toString());
			return generateResponse("News fail to save ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping(value = "/updateNews", headers = "Accept=application/json")
	public ResponseEntity<Object> updateNews(@RequestBody ImageDTO news) {
		System.out.println(news.getHeading());
		//byte[] valueDecoded = Base64.decodeBase64(news.getHeading());
		//String heading = new String(valueDecoded, StandardCharsets. UTF_8);
		//System.out.println("Decoded value is " + heading);
		News newsObj = new News();
		List<Images> imagesObj;
		try { 
			newsObj.setId(news.getNewsId());
			newsObj.setAltTag(news.getAltTag());
			newsObj.setKeywords(news.getKeywords());
			newsObj.setDescription(news.getDescription());
			newsObj.setTitle(news.getTitle());
			newsObj.setUrl(news.getUrl());
			dao.updateNews(newsObj);
			return generateResponse("News save succesfully", "OK", HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("Error in add news " + e.toString());
			return generateResponse("News fail to save ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@GetMapping(value = "/news/{id}", headers = "Accept=application/json")
	public ResponseEntity<Object> deleteNews(@PathVariable("id") int id) {
		//featureService.removeSubCategoryFeatureById(id);
		dao.deleteNews(id);
		return generateResponse("feature save successfully", "", HttpStatus.OK);
	}
	

	@PostMapping(value = "/getNews", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getNews(@RequestBody News news) {
		News newsObj = dao.getNewsById(news.getId());
		if (newsObj == null) {
			return generateResponse(" NEWS by id ", "", HttpStatus.NOT_FOUND);
		}
		return generateResponse("News by id ", newsObj, HttpStatus.OK);
	}

	@PostMapping(value = "/getAllNews", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllNews(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		req.setPage(pageNo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "API News with all data and banner ");
		map.put("status", 200);
		map.put("news", dao.getAllNews(pageNo, req.getSize()));
		map.put("newsCount", dao.getAllNewsCount());
		map.put("bannerList", homeService.getBanner("News"));
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}
	public String videoLinkSeprate(String link) {
		try{
		String[] s1=link.split("=");
		String newStr=s1[1];
		if(newStr.contains("&")){
			String[] s2=newStr.split("&");
			return s2[0];
		}else{
			return newStr;
		}
		}catch(Exception e){
			logger.debug("Exception occured "+e.getMessage());
		}
		return "";
	}
	@PostMapping(value = "/getAllVideoNews", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllVideoNews(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		req.setPageNo(pageNo);
		List<News> newList=new ArrayList<>();
		List<News> videoNews=dao.getAllVideoNews(req.getPageNo(), req.getSize());
		for(News news:videoNews){
			news.setVideoId(videoLinkSeprate(news.getVideoLink()));
			newList.add(news);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Home API testimonials with all data ");
		map.put("status", 200);
		map.put("news", newList);
		map.put("newsCount", dao.getAllNewsCount());
		map.put("bannerList", homeService.getBanner("News"));
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

	// Save Files
	private List<Images> saveUploadedFiles(String[] files, String imgName) throws IOException {
		List<Images> images = new ArrayList<>();
		String imageName =imgName+".jpg";
		for (String file : files) {
			System.out.println("file");
			Images imgObj = new Images();
			if (file.isEmpty()) {
				break;
			}
			byte[] imageByte = Base64.decodeBase64(file.split(",")[1]);
			new FileOutputStream("/home/f2df/ea-tomcat85/webapps/img/News/"+imageName).write(imageByte);
			imgObj.setFilePath("/img/News/"+imageName);
			imgObj.setOriginalFileName(imageName);
			images.add(imgObj);
			imageName=imgName+new Date().getTime()+".jpg";;
		}
		return images;
	}
	public static void main(String args[]) {
		String str="कमल";
		byte[] byteArr = str.getBytes();
		String str1 = new String(byteArr, StandardCharsets. UTF_8);
        System.out.println("string: " + imgExisting("C:\\opt\\Kamal\\banner-new\\banner-new\\1111.jpg"));

    }
	public static boolean imgExisting(String path) {
		File f = new File(path);
		boolean istrue = false;
		if (f.exists()) {
			istrue = true;
		}
		return istrue;
	}
	
	@GetMapping("/generate-sitemap")
	public ResponseEntity<ResultDTO> generateSiteMap() {
        siteMapService.generateSiteMap();
		return ResponseEntity.ok(null);
	}



}
