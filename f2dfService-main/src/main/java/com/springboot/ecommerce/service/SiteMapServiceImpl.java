package com.springboot.ecommerce.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.springboot.ecommerce.model.News;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.model.ProductCategory;
import com.springboot.ecommerce.model.ProductSubCategory;
import com.springboot.ecommerce.model.SiteMapIndex;
import com.springboot.ecommerce.model.SiteUrl;
import com.springboot.ecommerce.model.SiteUrlNew;
import com.springboot.ecommerce.model.SiteUrlSet;
import com.springboot.ecommerce.repository.CommonDao;
import com.springboot.ecommerce.repository.ProductCategoryDao;

@Component
@PropertySource("classpath:application.properties")
public class SiteMapServiceImpl implements SiteMapService {

	private static final Logger logger = Logger.getLogger(SiteMapServiceImpl.class);

	@Value("${file.Name}")
	private String fileName;
	@Value("${meta.data}")
	private String META_DATA;
	@Value("${meta.data1}")
	private String META_DATA1;

	private String fileLocation = "/home/f2df/ea-tomcat85/webapps/img/sitemap/";

	private String baseUrl = "https://f2df.in/img/sitemap/";

	@Autowired
	CommonDao dao;

	@Autowired
	ProductCategoryService categoryService;

	@Autowired
	ProductSubCategoryService subCategoryService;

	@Autowired
	ProductService productService;

	public void writeParentSiteMapXML(String xmlFileName, boolean mainSiteMap, ArrayList<SiteMapIndex> siteUrlSetList) {
		logger.info("Under the roof of SiteMapServiceImpl method name createSiteMapXML ::: ");
//		SiteUrlSet siteUrlSet	= calculateSitmapData();
		// fileLocation = "D:\\sitemap\\";
		// fileLocation = "/smimgfs/uat/sitemap/";
		int i = 1;
		for (SiteMapIndex siteUrlSet : siteUrlSetList) {
			String sitemapfile = "";
			if (mainSiteMap) {
				sitemapfile = fileLocation + xmlFileName + ".xml";
			} else {
				sitemapfile = fileLocation + xmlFileName + "_" + i + ".xml";
			}

			try {
				System.out.println("FILE PATH >>>>>>>>>>>>>>>>>>>" + sitemapfile);
				File file = new File(sitemapfile);
				if (file.exists()) {
					Date date = new Date();
					String movePath = fileLocation + "sitemapMove/sitemap_" + date.getTime() + ".xml";
					File renameFile = new File(movePath);
					File moveFile = new File(movePath);
					boolean isCopied = file.renameTo(renameFile);
					boolean isMoved = renameFile.renameTo(moveFile);

				}
				System.out.println("File Exist ::" + file.exists());
				JAXBContext jaxbContext = JAXBContext.newInstance(SiteMapIndex.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(siteUrlSet, file);
				jaxbMarshaller.marshal(siteUrlSet, System.out);
				System.out.println("<<<<<<<<*******XML WRITING DONE****>>>>>>>>");
			} catch (JAXBException e) {
				e.printStackTrace();
			}

			try {
				String content = FileUtils.readFileToString(new File(sitemapfile), StandardCharsets.UTF_8);

				i++;
				content = content.replace("<urlset>", META_DATA);
				content = content.replace("<sitemapindex>", META_DATA1);

				File file = new File(sitemapfile);
				FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
				System.out.println("==================== FILE WRITE DONE ==========================");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		System.out.println("****************ALL FILES FINISH***********************************");
	}

	private boolean generateParentSiteMap() {
		try {
			ArrayList<SiteUrlNew> staticSiteMapList = addChildUrlInSiteMap();
			ArrayList<SiteMapIndex> staticSiteMapUrlSetList = new ArrayList<SiteMapIndex>();
			SiteMapIndex siteMapSet = new SiteMapIndex();
			siteMapSet.setSiteUrlList(staticSiteMapList);
			staticSiteMapUrlSetList.add(siteMapSet);
			writeParentSiteMapXML("sitemap", true, staticSiteMapUrlSetList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	private boolean generateNewsSiteMap() {
		ArrayList<SiteUrl> mainNewsSiteMapList = new ArrayList<SiteUrl>();
		ArrayList<SiteUrlNew> newSiteMapList = new ArrayList<SiteUrlNew>();
		try {
			List<News> newsList = dao.getAllNewsForSiteMap();
			newsList.forEach(action -> {
				if (action.getHeading() != null && action.getId() != 0) {
					SiteUrl siteUrl = new SiteUrl();
					String url = baseUrl + "krishi-darshan/news/" + dataSanity(action.getHeading()).toLowerCase() + "/"
							+ action.getId();
					siteUrl.setLoc(url);
					siteUrl.setPriority("0.90");
					mainNewsSiteMapList.add(siteUrl);
				}
			});

			logger.info("Total News List size is ::" + mainNewsSiteMapList.size());

			List<List<SiteUrl>> subNewsSiteMapList = com.google.common.collect.Lists.partition(mainNewsSiteMapList,
					100);

			int count = 1;
			ArrayList<SiteUrl> activeUrlList = new ArrayList<SiteUrl>();
			for (List<SiteUrl> action : subNewsSiteMapList) {
				SiteUrl mainSiteUrl = new SiteUrl();
				mainSiteUrl.setLoc(baseUrl.concat("news-" + count + ".xml"));
				activeUrlList.add(mainSiteUrl);
				ArrayList<SiteUrl> myList = new ArrayList<SiteUrl>();
				myList.addAll(action);

				ArrayList<SiteUrlSet> siteUrlSetListForLease = new ArrayList<SiteUrlSet>();
				SiteUrlSet siteMapSetForRent = new SiteUrlSet();
				siteMapSetForRent.setSiteUrlList(myList);
				siteUrlSetListForLease.add(siteMapSetForRent);
				writeSiteMapXML("news-" + count, true, siteUrlSetListForLease);

				SiteUrlNew siteUrl1 = new SiteUrlNew();
				siteUrl1.setLoc(baseUrl.concat("news-" + count + ".xml"));
				newSiteMapList.add(siteUrl1);
				count++;

			}

			SiteMapIndex siteMapUrl = new SiteMapIndex();
			siteMapUrl.setSiteUrlList(newSiteMapList);

			ArrayList<SiteMapIndex> siteMapUrlSetList = new ArrayList<SiteMapIndex>();
			siteMapUrlSetList.add(siteMapUrl);
			writeParentSiteMapXML("news", true, siteMapUrlSetList);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;

	}

	private boolean generateVideoNewsSiteMap() {
		ArrayList<SiteUrl> mainNewsSiteMapList = new ArrayList<SiteUrl>();
		ArrayList<SiteUrlNew> newSiteMapList = new ArrayList<SiteUrlNew>();
		try {
			List<News> newsList = dao.getAllVideoNewsForSiteMap();
			newsList.forEach(action -> {
				if (action.getHeading() != null && action.getId() != 0) {
					SiteUrl siteUrl = new SiteUrl();
					String url = baseUrl + "krishi-darshan/video-news/" + dataSanity(action.getHeading()).toLowerCase()
							+ "/" + action.getId();
					siteUrl.setLoc(url);
					siteUrl.setPriority("0.90");
					mainNewsSiteMapList.add(siteUrl);
				}
			});

			logger.info("Total News List size is ::" + mainNewsSiteMapList.size());

			List<List<SiteUrl>> subNewsSiteMapList = com.google.common.collect.Lists.partition(mainNewsSiteMapList,
					100);

			int count = 1;
			ArrayList<SiteUrl> activeUrlList = new ArrayList<SiteUrl>();
			for (List<SiteUrl> action : subNewsSiteMapList) {
				SiteUrl mainSiteUrl = new SiteUrl();
				mainSiteUrl.setLoc(baseUrl.concat("video-news-" + count + ".xml"));
				activeUrlList.add(mainSiteUrl);
				ArrayList<SiteUrl> myList = new ArrayList<SiteUrl>();
				myList.addAll(action);

				ArrayList<SiteUrlSet> siteUrlSetListForLease = new ArrayList<SiteUrlSet>();
				SiteUrlSet siteMapSetForRent = new SiteUrlSet();
				siteMapSetForRent.setSiteUrlList(myList);
				siteUrlSetListForLease.add(siteMapSetForRent);
				writeSiteMapXML("video-news-" + count, true, siteUrlSetListForLease);

				SiteUrlNew siteUrl1 = new SiteUrlNew();
				siteUrl1.setLoc(baseUrl.concat("video-news-" + count + ".xml"));
				newSiteMapList.add(siteUrl1);
				count++;

			}

			SiteMapIndex siteMapUrl = new SiteMapIndex();
			siteMapUrl.setSiteUrlList(newSiteMapList);

			ArrayList<SiteMapIndex> siteMapUrlSetList = new ArrayList<SiteMapIndex>();
			siteMapUrlSetList.add(siteMapUrl);
			writeParentSiteMapXML("video-news", true, siteMapUrlSetList);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;

	}

	private boolean generateCategorySiteMap() {
		ArrayList<SiteUrl> mainCategorySiteMapList = new ArrayList<SiteUrl>();
		ArrayList<SiteUrlNew> categorySiteMapList = new ArrayList<SiteUrlNew>();
		try {
			List<ProductCategory> productCategoryList = categoryService.getCategoryForSiteMap();
			productCategoryList.forEach(action -> {
				if (action.getCategoryName() != null && action.getPc_id() != 0) {
					SiteUrl siteUrl = new SiteUrl();
					String url = baseUrl + "category/" + dataSanity(action.getCategoryName()).toLowerCase() + "/"
							+ action.getPc_id();
					siteUrl.setLoc(url);
					siteUrl.setPriority("0.90");
					mainCategorySiteMapList.add(siteUrl);
				}
			});

			logger.info("Total Category List size is ::" + mainCategorySiteMapList.size());

			List<List<SiteUrl>> subCategorySiteMapList = com.google.common.collect.Lists
					.partition(mainCategorySiteMapList, 100);

			int count = 1;
			ArrayList<SiteUrl> activeUrlList = new ArrayList<SiteUrl>();
			for (List<SiteUrl> action : subCategorySiteMapList) {
				SiteUrl mainSiteUrl = new SiteUrl();
				mainSiteUrl.setLoc(baseUrl.concat("category-" + count + ".xml"));
				activeUrlList.add(mainSiteUrl);
				ArrayList<SiteUrl> myList = new ArrayList<SiteUrl>();
				myList.addAll(action);

				ArrayList<SiteUrlSet> siteUrlSetListForLease = new ArrayList<SiteUrlSet>();
				SiteUrlSet siteMapSetForRent = new SiteUrlSet();
				siteMapSetForRent.setSiteUrlList(myList);
				siteUrlSetListForLease.add(siteMapSetForRent);
				writeSiteMapXML("category-" + count, true, siteUrlSetListForLease);

				SiteUrlNew siteUrl1 = new SiteUrlNew();
				siteUrl1.setLoc(baseUrl.concat("category-" + count + ".xml"));
				categorySiteMapList.add(siteUrl1);
				count++;

			}

			SiteMapIndex siteMapUrl = new SiteMapIndex();
			siteMapUrl.setSiteUrlList(categorySiteMapList);

			ArrayList<SiteMapIndex> siteMapUrlSetList = new ArrayList<SiteMapIndex>();
			siteMapUrlSetList.add(siteMapUrl);
			writeParentSiteMapXML("category", true, siteMapUrlSetList);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;

	}

	private boolean generateSubCategorySiteMap() {
		ArrayList<SiteUrl> mainCategorySiteMapList = new ArrayList<SiteUrl>();
		ArrayList<SiteUrlNew> categorySiteMapList = new ArrayList<SiteUrlNew>();
		try {
			List<ProductCategory> productCategoryList = categoryService.getCategoryForSiteMap();
			productCategoryList.forEach(action -> {

				if (action.getCategoryName() != null && action.getPc_id() != 0) {
					List<ProductSubCategory> productsubCategoryList = subCategoryService.getSubCategoryList();
					productsubCategoryList.forEach(subCategory -> {
						if (subCategory.getSubCategoryName() != null) {
							SiteUrl siteUrl = new SiteUrl();
							String url = baseUrl + "sub-category/" + dataSanity(action.getCategoryName()).toLowerCase()
									+ "/" + dataSanity(subCategory.getSubCategoryName()).toLowerCase() + "/"
									+ subCategory.getPsc_id();
							siteUrl.setLoc(url);
							siteUrl.setPriority("0.90");
							mainCategorySiteMapList.add(siteUrl);
						}
					});

				}
			});

			logger.info("Total Sub Category List size is ::" + mainCategorySiteMapList.size());

			List<List<SiteUrl>> subCategorySiteMapList = com.google.common.collect.Lists
					.partition(mainCategorySiteMapList, 100);

			int count = 1;
			ArrayList<SiteUrl> activeUrlList = new ArrayList<SiteUrl>();
			for (List<SiteUrl> action : subCategorySiteMapList) {
				SiteUrl mainSiteUrl = new SiteUrl();
				mainSiteUrl.setLoc(baseUrl.concat("sub-category-" + count + ".xml"));
				activeUrlList.add(mainSiteUrl);
				ArrayList<SiteUrl> myList = new ArrayList<SiteUrl>();
				myList.addAll(action);

				ArrayList<SiteUrlSet> siteUrlSetListForLease = new ArrayList<SiteUrlSet>();
				SiteUrlSet siteMapSetForRent = new SiteUrlSet();
				siteMapSetForRent.setSiteUrlList(myList);
				siteUrlSetListForLease.add(siteMapSetForRent);
				writeSiteMapXML("sub-category-" + count, true, siteUrlSetListForLease);

				SiteUrlNew siteUrl1 = new SiteUrlNew();
				siteUrl1.setLoc(baseUrl.concat("sub-category-" + count + ".xml"));
				categorySiteMapList.add(siteUrl1);
				count++;

			}

			SiteMapIndex siteMapUrl = new SiteMapIndex();
			siteMapUrl.setSiteUrlList(categorySiteMapList);

			ArrayList<SiteMapIndex> siteMapUrlSetList = new ArrayList<SiteMapIndex>();
			siteMapUrlSetList.add(siteMapUrl);
			writeParentSiteMapXML("sub-category", true, siteMapUrlSetList);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;

	}

	private boolean generateProduct() {
		ArrayList<SiteUrl> mainCategorySiteMapList = new ArrayList<SiteUrl>();
		ArrayList<SiteUrlNew> categorySiteMapList = new ArrayList<SiteUrlNew>();
		try {
			List<Product> productList = productService.getProductListForSiteMap();
			productList.forEach(action -> {

				if (action.getProductName() != null && action.getProductCategory() != null
						&& action.getProductCategory().getCategoryName() != null && action.getSubCategory() != null
						&& action.getSubCategory().getSubCategoryName() != null) {

					SiteUrl siteUrl = new SiteUrl();
					String url = baseUrl + dataSanity(action.getProductCategory().getCategoryName()).toLowerCase() + "/"
							+ dataSanity(action.getSubCategory().getSubCategoryName()).toLowerCase() + "/"
							+ action.getP_id();
					siteUrl.setLoc(url);
					siteUrl.setPriority("0.90");
					mainCategorySiteMapList.add(siteUrl);

				}
			});

			logger.info("Total Product List size is ::" + mainCategorySiteMapList.size());

			List<List<SiteUrl>> subCategorySiteMapList = com.google.common.collect.Lists
					.partition(mainCategorySiteMapList, 100);

			int count = 1;
			ArrayList<SiteUrl> activeUrlList = new ArrayList<SiteUrl>();
			for (List<SiteUrl> action : subCategorySiteMapList) {
				SiteUrl mainSiteUrl = new SiteUrl();
				mainSiteUrl.setLoc(baseUrl.concat("product-" + count + ".xml"));
				activeUrlList.add(mainSiteUrl);
				ArrayList<SiteUrl> myList = new ArrayList<SiteUrl>();
				myList.addAll(action);

				ArrayList<SiteUrlSet> siteUrlSetListForLease = new ArrayList<SiteUrlSet>();
				SiteUrlSet siteMapSetForRent = new SiteUrlSet();
				siteMapSetForRent.setSiteUrlList(myList);
				siteUrlSetListForLease.add(siteMapSetForRent);
				writeSiteMapXML("product-" + count, true, siteUrlSetListForLease);

				SiteUrlNew siteUrl1 = new SiteUrlNew();
				siteUrl1.setLoc(baseUrl.concat("product-" + count + ".xml"));
				categorySiteMapList.add(siteUrl1);
				count++;

			}

			SiteMapIndex siteMapUrl = new SiteMapIndex();
			siteMapUrl.setSiteUrlList(categorySiteMapList);

			ArrayList<SiteMapIndex> siteMapUrlSetList = new ArrayList<SiteMapIndex>();
			siteMapUrlSetList.add(siteMapUrl);
			writeParentSiteMapXML("product", true, siteMapUrlSetList);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;

	}

	public void writeSiteMapXML(String xmlFileName, boolean mainSiteMap, ArrayList<SiteUrlSet> siteUrlSetList) {
		logger.info("Under the roof of SiteMapServiceImpl method name createSiteMapXML ::: ");
//		SiteUrlSet siteUrlSet	= calculateSitmapData();
		// fileLocation = "D:\\sitemap\\";
		// fileLocation = "/smimgfs/uat/sitemap/";
		int i = 1;
		for (SiteUrlSet siteUrlSet : siteUrlSetList) {
			String sitemapfile = "";
			if (mainSiteMap) {
				sitemapfile = fileLocation + xmlFileName + ".xml";
			} else {
				sitemapfile = fileLocation + xmlFileName + "_" + i + ".xml";
			}

			try {
				System.out.println("FILE PATH >>>>>>>>>>>>>>>>>>>" + sitemapfile);
				File file = new File(sitemapfile);
				if (file.exists()) {
					Date date = new Date();
					String movePath = fileLocation + "sitemapMove/sitemap_" + date.getTime() + ".xml";
					File renameFile = new File(movePath);
					File moveFile = new File(movePath);
					boolean isCopied = file.renameTo(renameFile);
					boolean isMoved = renameFile.renameTo(moveFile);

				}
				System.out.println("File Exist ::" + file.exists());
				JAXBContext jaxbContext = JAXBContext.newInstance(SiteUrlSet.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(siteUrlSet, file);
				jaxbMarshaller.marshal(siteUrlSet, System.out);
				System.out.println("<<<<<<<<*******XML WRITING DONE****>>>>>>>>");
			} catch (JAXBException e) {
				e.printStackTrace();
			}

			try {
				String content = FileUtils.readFileToString(new File(sitemapfile), StandardCharsets.UTF_8);

				i++;
				content = content.replace("<urlset>", META_DATA);

				File file = new File(sitemapfile);
				FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
				System.out.println("==================== FILE WRITE DONE ==========================");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		System.out.println("****************ALL FILES FINISH***********************************");
	}

	private String dataSanity(String data) {

		if (data != null && data.trim().length() > 0) {

			data = data.replaceAll("&", "-and-").replaceAll("\\(.*\\)", "");
			return data = data.trim().replaceAll(" ", "-");
		}

		return null;
	}

	@Override
	public void generateSiteMap() {
		try {
			generateParentSiteMap();

			generateNewsSiteMap();

			generateVideoNewsSiteMap();

			generateCategorySiteMap();
			
			generateProduct();

			generateSubCategorySiteMap();
			
			generateStaticSiteMap();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private boolean generateStaticSiteMap() {
		try {
			ArrayList<SiteUrl> staticSiteMapList = addStaticURL();
			ArrayList<SiteUrlSet> staticSiteMapUrlSetList = new ArrayList<SiteUrlSet>();
			SiteUrlSet siteMapSet = new SiteUrlSet();
			siteMapSet.setSiteUrlList(staticSiteMapList);
			staticSiteMapUrlSetList.add(siteMapSet);
			writeSiteMapXML("staticpage", true, staticSiteMapUrlSetList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	private ArrayList<SiteUrl> addStaticURL() {
		ArrayList<SiteUrl> siteUrlList_BUY = new ArrayList<SiteUrl>();
		System.out.println("addStaticURL::::::::: ");

		SiteUrl siteUrl1 = new SiteUrl();
		siteUrl1.setLoc("https://f2df.com/");
		siteUrl1.setPriority("1.0");
		siteUrlList_BUY.add(siteUrl1);

		SiteUrl siteUrl2 = new SiteUrl();
		siteUrl2.setPriority("1.0");
		siteUrl2.setLoc("https://f2df.com/sign-in");
		siteUrlList_BUY.add(siteUrl2);

		SiteUrl siteUrl4 = new SiteUrl();
		siteUrl4.setPriority("1.0");
		siteUrl4.setLoc("https://f2df.com/product/list ");
		siteUrlList_BUY.add(siteUrl4);

		SiteUrl siteUrl5 = new SiteUrl();
		siteUrl5.setPriority("1.0");
		siteUrl5.setLoc("https://f2df.com/product/list%3Fcat_id%3D11");
		siteUrlList_BUY.add(siteUrl5);

		SiteUrl siteUrl6 = new SiteUrl();
		siteUrl6.setPriority("0.6");
		siteUrl6.setLoc("https://f2df.com/about-us");
		siteUrlList_BUY.add(siteUrl6);

		SiteUrl siteUrl7 = new SiteUrl();
		siteUrl7.setPriority(".6");
		siteUrl7.setLoc("https://f2df.com/about-us/leadership-board");
		siteUrlList_BUY.add(siteUrl7);

		SiteUrl siteUrl8 = new SiteUrl();
		siteUrl2.setPriority(".9");
		siteUrl8.setLoc("https://f2df.com/krishi-darshan/news/list");
		siteUrlList_BUY.add(siteUrl8);

		SiteUrl siteUrl9 = new SiteUrl();
		siteUrl9.setPriority("1.0");
		siteUrl9.setLoc("https://f2df.com/gallery/photo-gallery");
		siteUrlList_BUY.add(siteUrl9);

		SiteUrl siteUrl10 = new SiteUrl();
		siteUrl10.setPriority("1.0");
		siteUrl10.setLoc("https://f2df.com/about-us/faq");
		siteUrlList_BUY.add(siteUrl10);
//============================================================		
		SiteUrl siteUrl11 = new SiteUrl();
		siteUrl11.setPriority(".9");
		siteUrl11.setLoc("https://f2df.com/policies/privacy-policy");
		siteUrlList_BUY.add(siteUrl11);

		SiteUrl siteUrl12 = new SiteUrl();
		siteUrl12.setPriority(".9");
		siteUrl12.setLoc("https://f2df.com/policies/terms-conditions");
		siteUrlList_BUY.add(siteUrl12);

		SiteUrl siteUrl13 = new SiteUrl();
		siteUrl12.setPriority("1.0");
		siteUrl13.setLoc("https://f2df.com/policies/cancellation-policy");
		siteUrlList_BUY.add(siteUrl13);

		SiteUrl siteUrl14 = new SiteUrl();
		siteUrl14.setPriority(".6");
		siteUrl14.setLoc("https://f2df.com/policies/shipping-delivery-policy");
		siteUrlList_BUY.add(siteUrl14);

		SiteUrl siteUrl15 = new SiteUrl();
		siteUrl15.setPriority("1.0");
		siteUrl15.setLoc("https://f2df.com/policies/seller-m-term-conditions");
		siteUrlList_BUY.add(siteUrl15);
		
		SiteUrl siteUrl16 = new SiteUrl();
		siteUrl16.setPriority("1.0");
		siteUrl16.setLoc("https://f2df.com/farmer-training/upcoming-training");
		siteUrlList_BUY.add(siteUrl16);
		
		SiteUrl siteUrl17 = new SiteUrl();
		siteUrl17.setPriority("1.0");
		siteUrl17.setLoc("https://f2df.com/grow-business/promote-your-lead");
		siteUrlList_BUY.add(siteUrl17);
		
		
		SiteUrl siteUrl18 = new SiteUrl();
		siteUrl18.setPriority("1.0");
		siteUrl18.setLoc("https://f2df.com/krishi-darshan/video-news");
		siteUrlList_BUY.add(siteUrl18);
		
		SiteUrl siteUrl19 = new SiteUrl();
		siteUrl19.setPriority("1.0");
		siteUrl19.setLoc("https://f2df.com/banking/apply-for-insurance");
		siteUrlList_BUY.add(siteUrl19);
		
		SiteUrl siteUrl20 = new SiteUrl();
		siteUrl20.setPriority("1.0");
		siteUrl20.setLoc("https://f2df.com/contact-us");
		siteUrlList_BUY.add(siteUrl20);
		
				
	
		return siteUrlList_BUY;
	}


	private ArrayList<SiteUrlNew> addChildUrlInSiteMap() {
		ArrayList<SiteUrlNew> siteUrlList_BUY = new ArrayList<SiteUrlNew>();
		System.out.println("addStaticURL::::::::: ");

		SiteUrlNew siteUrl = new SiteUrlNew();
		siteUrl.setLoc(baseUrl + "staticpage.xml");
		// siteUrl.setPriority("1.0");
		siteUrlList_BUY.add(siteUrl);

		siteUrl = new SiteUrlNew();
		siteUrl.setLoc(baseUrl + "category.xml");
		// siteUrl.setPriority("1.0");
		siteUrlList_BUY.add(siteUrl);

		siteUrl = new SiteUrlNew();
		siteUrl.setLoc(baseUrl + "sub-category.xml");
		// siteUrl.setPriority("1.0");
		siteUrlList_BUY.add(siteUrl);

		siteUrl = new SiteUrlNew();
		siteUrl.setLoc(baseUrl + "product.xml");
		// siteUrl.setPriority("1.0");
		siteUrlList_BUY.add(siteUrl);

		siteUrl = new SiteUrlNew();
		siteUrl.setLoc(baseUrl + "news.xml");
		// siteUrl.setPriority("1.0");
		siteUrlList_BUY.add(siteUrl);

		siteUrl = new SiteUrlNew();
		siteUrl.setLoc(baseUrl + "video-news.xml");
		// siteUrl.setPriority("1.0");
		siteUrlList_BUY.add(siteUrl);

		siteUrlList_BUY.add(siteUrl);

		return siteUrlList_BUY;
	}

}
