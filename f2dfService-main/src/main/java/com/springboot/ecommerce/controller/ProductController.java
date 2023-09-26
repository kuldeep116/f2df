package com.springboot.ecommerce.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.model.ProductCategory;
import com.springboot.ecommerce.model.ProductFeatureValue;
import com.springboot.ecommerce.model.ProductSubCategory;
import com.springboot.ecommerce.model.Transaction;
import com.springboot.ecommerce.model.TransactionSale;
import com.springboot.ecommerce.model.TransactionSaleSold;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.pojo.ProductAction;
import com.springboot.ecommerce.pojo.ReportRequest;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.pojo.RequestTrans;
import com.springboot.ecommerce.repository.CommonDao;
import com.springboot.ecommerce.service.HomeService;
import com.springboot.ecommerce.service.ProductService;
import com.springboot.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/product")
@Tag(name = "Product API")
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	HomeService homeService;

	@Autowired
	CommonDao commonDao;

	@Autowired
	UserService userService;

	@Value("${f2df.addshow.count}")
	private String addShowCOunt;

	@Value("${f2df.app.root}")
	public static String rootPath;

	private static Logger logger = Logger.getLogger(ProductController.class.getName());

	@SuppressWarnings("resource")
	public String saveImageOnServer(String byteCode, String path) {
		byte[] imageByte = Base64.decodeBase64(byteCode);
		try {
			// new FileOutputStream(rootPath + path).write(imageByte);
			new FileOutputStream("/home/f2df/ea-tomcat85/webapps/" + path).write(imageByte);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	// Save Files
	private final Path root = Paths.get("/img/product");

	private String saveUploadedFiles(MultipartFile file) throws IOException {
		String imageName = new Date().getTime() + file.getOriginalFilename();
		Files.copy(file.getInputStream(), this.root.resolve(imageName));
		return root + "/" + imageName;
	}

	@PostMapping(value = "/uploadImg", consumes = { "multipart/form-data" })
	private String uploadImg(@RequestPart("images") MultipartFile[] files) {
		try {
			for (MultipartFile file : files) {
				String imageName = new Date().getTime() + file.getOriginalFilename();
				Files.copy(file.getInputStream(), this.root.resolve(imageName));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "Sucessfull upload";
	}

	public String productImagePath = "/img/product";
	public String productInvoicePath = "/img/invoice";

	@PostMapping(value = "/addProduct", consumes = { "application/json" })
	public ResponseEntity<Object> addProduct(@RequestBody Product product, UriComponentsBuilder ucBuilder,
			HttpServletRequest request) {
		try {
			ProductCategory cat = new ProductCategory();
			cat.setPc_id(product.getPc_id());

			ProductSubCategory subCategory = new ProductSubCategory();
			subCategory.setPsc_id(product.getPsc_id());

			product.setProductCategory(cat);
			product.setSubCategory(subCategory);

			product.setCreateDate(new Date());
			product.setProductDesc("InActive");
			product.setUpdateDate(new Date());
			String defaultBrandImg = "/img/defaultProductImg/" + product.getPsc_id();
			for (ProductFeatureValue pf : product.getFeatureDetailsValue()) {
				if (pf.getProductFeatureKey().toUpperCase().contains("PRICE (RS.)/UNIT")) {
					logger.debug(" Price123 " + pf.getProductFeatureValue());
					String value = "";
					if (pf.getProductFeatureValue().contains("-")) {
						value = pf.getProductFeatureValue().replaceAll("\\s", "").split("-")[0];
					} else {
						value = pf.getProductFeatureValue().replaceAll("\\s", "");
					}
					if (value.contains("PRICE")) {
						logger.debug(" Price " + value);
						value = value.replace("PRICE(RS.)/UNIT", "");
						logger.debug(" Price1 " + value);
					}
					try {
						product.setProductFee(Integer.valueOf(value.trim()));
					} catch (Exception e) {
						product.setProductFee(0);
					}
					product.setOldFee(product.getProductFee() + calculatePercentage(product.getProductFee(), 10));
					product.setDisFee(10);
				} else if (pf.getProductFeatureKey().toUpperCase().contains("ORGANIC")) {
					if (pf.getProductFeatureValue().toUpperCase().contains("YES")) {
						product.setOrganic(true);
					}
				}
				if (pf.getProductFeatureKey().contains("MRP (RS.)")) {
					String value = "";
					if (pf.getProductFeatureValue().contains("-")) {
						value = pf.getProductFeatureValue().replaceAll("\\s", "").split("-")[0];
					} else {
						value = pf.getProductFeatureValue().replaceAll("\\s", "");
					}
					product.setOldFee(Integer.valueOf(value));
				}
				StringBuilder agreementStr = new StringBuilder("I Agree that F2DF will pay Rs (");
				/*
				 * this is seller price which set by company which trying to
				 * sell
				 */
				if (pf.getProductFeatureKey().contains("SELLER PRICE (RS.)")) {
					String value = "";

					if (pf.getProductFeatureValue().contains("-")) {
						value = pf.getProductFeatureValue().replaceAll("\\s", "").split("-")[0];
					} else {
						value = pf.getProductFeatureValue().replaceAll("\\s", "");
					}

					product.setDisFee(Integer.valueOf(value));
					agreementStr = agreementStr.append(value);
					agreementStr = agreementStr.append(
							") including GST charges To me after adjusting the Platform fee/ Delivery Charges (whatever applicable).");
					product.setDigitalAggrement(agreementStr.toString());
					pf.setProductFeatureKey("");
					pf.setProductFeatureValue("");
					// product.getFeatureDetailsValue().forEach(action);
				}
				if (pf.getProductFeatureKey().contains("F2DF PLATEFORM CHARGES (RS.)")) {
					String value = "";
					if (pf.getProductFeatureValue().contains("-")) {
						value = pf.getProductFeatureValue().replaceAll("\\s", "").split("-")[0];
					} else {
						value = pf.getProductFeatureValue().replaceAll("\\s", "");
					}

					product.setProductFee(product.getDisFee() + Integer.valueOf(value));
					pf.setProductFeatureKey("");
					pf.setProductFeatureValue("");
					// product.getFeatureDetailsValue().forEach(action);
				}
			}
			if (product.getMultiProductImages() != null && product.getMultiProductImages().length >= 0) {
				if (product.getMultiProductImages().length >= 1) {
					product.setImg1(product.getMultiProductImages()[0].split(",")[1]);
				}
				if (product.getMultiProductImages().length >= 2) {
					product.setImg2(product.getMultiProductImages()[1].split(",")[1]);
				}
				if (product.getMultiProductImages().length >= 3) {
					product.setImg3(product.getMultiProductImages()[2].split(",")[1]);
				}
				if (product.getMultiProductImages().length >= 4) {
					product.setImg4(product.getMultiProductImages()[3].split(",")[1]);
				}
			}
			if (product.getImg1() != null && !product.getImg1().equals("")) {
				String path = saveImageOnServer(product.getImg1(),
						productImagePath + File.separator + new Date().getTime() + ".jpg");
				product.setImg1(path);
			} else {
				product.setImg1(defaultBrandImg);
			}
			if (product.getImg2() != null && !product.getImg2().equals("")) {
				String path = saveImageOnServer(product.getImg2(),
						productImagePath + File.separator + new Date().getTime() + ".jpg");
				product.setImg2(path);
			} else {
				product.setImg2("/img/product/noImg.webp");
			}
			if (product.getImg1() != null && !product.getImg1().equals("")) {
				String path = saveImageOnServer(product.getImg1(),
						productImagePath + File.separator + new Date().getTime() + ".jpg");
				product.setImg3(path);
			} else {
				product.setImg3("/img/product/noImg.webp");
			}
			product.setImg4(defaultBrandImg);
			// ProductFeatureValue obj= (ProductFeatureValue)
			// product.getFeatureDetailsValue().stream().filter(val ->
			// val.getProductFeatureKey().equals("Price (Rs.)/Unit"));
			if (product.getUserId() >= 0) {
				User user = userService.getUser(product.getUserId());
				product.setMobile(String.valueOf(user.getMobile()));
			}

			int productId = productService.createProduct(product);
			HomeController.homemap = new HashMap<String, Object>();
			HomeController.rentCategrory = new ArrayList<ProductCategory>();
			HomeController.AllCat = new ArrayList<ProductCategory>();

			return generateResponse("Product save succesfully", "", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error occured during save product " + e.getMessage());
			return generateResponse("Product not able to save succesfully" + e.getMessage(), "",
					HttpStatus.NOT_ACCEPTABLE);
		}
	}

	public int calculatePercentage(double obtained, double percent) {
		return (int) ((obtained * percent) / 100);
	}

	@Operation(summary = "get payment order id against payment the product", tags = { "Product" })
	@PostMapping(value = "/getOrderId", headers = "Accept=application/json")
	public ResponseEntity<Object> getOrderId(@RequestBody Transaction transaction) throws RazorpayException {
		RazorpayClient razorpay = new RazorpayClient("rzp_live_MmKm1tIl8HM05R", "NwjedeYNrjiMUZVRFXwyxDvX");

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", transaction.getPaidAmount());
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "receipt#" + new Date().getTime());
		JSONObject notes = new JSONObject();
		notes.put("notes_key_1", "Payment against product for " + transaction.getPaidAmount());

		orderRequest.put("notes", notes);

		Order order = razorpay.Orders.create(orderRequest);
		return generateResponse("Order create successfully order id is", order.get("id"), HttpStatus.OK);
	}
	
	
	
	
	
	

	@Operation(summary = "Updates the product which is specified by id", tags = { "Product" })
	@PostMapping(value = "/editProduct/", headers = "Accept=application/json")
	public ResponseEntity<Object> updateProduct(@RequestBody ProductAction product) {
		Product productObj = new Product();
		if (product.getImg1() != null && !product.getImg1().equals("")) {
			String path = saveImageOnServer(product.getImg1().split(",")[1],
					productImagePath + File.separator + new Date().getTime() + ".jpg");
			productObj.setImg1(path);
		}
		productObj.setP_id(product.getP_id());
		productObj.setProductDesc(product.getAction());
		// productObj.setQtyDone(product.getQty());
		productService.editProduct(productObj);
		return generateResponse(" Product update succesfully ", "", HttpStatus.OK);
	}

	@Operation(summary = "Boost the product which is specified by id", tags = { "Product" })
	@PostMapping(value = "/boostProduct", headers = "Accept=application/json")
	public ResponseEntity<Object> boostProduct(@RequestBody Transaction req) {
		Product product = new Product();
		product.setRecommended(true);
		product.setAssured(true);
		product.setP_id(req.getP_Id());
		logger.debug("Boost your product recoomended " + product.isRecommended() + " assured " + product.isAssured()
				+ " product id " + product.getP_id());
		productService.boostProduct(product);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		req.setCreateDate(timeStamp);
		req.setUpdateDate(timeStamp);
		req.setCartId(0); // 0 is added for boost product only
		commonDao.saveTransaction(req);
		return generateResponse(" Your Product Boost succesfully ", "", HttpStatus.OK);
	}

	@PostMapping(value = "/deleteProduct", headers = "Accept=application/json")
	public ResponseEntity<Object> deleteProduct(@RequestBody RequestProduct req) {
		productService.removeProductById(req.getProductId());
		logger.debug("Product deleted id is  " + req.getProductId());
		return generateResponse(" Product delete succesfully ", "", HttpStatus.OK);
	}

	@PostMapping(value = "/getProduct", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getProductById(@RequestBody RequestProduct req) {

		Product product = productService.findProductById(req.getProductId());
		if (product == null) {
			return generateResponse(" Product data ", product, HttpStatus.NOT_FOUND);
		}
		return generateResponse(" Product data ", product, HttpStatus.OK);
	}

	@PostMapping(value = "/getProductDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getProductDetails(@RequestBody RequestProduct req) {
		Product product = productService.findProductById(req.getProductId());
		if (product != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", "Product details by id");
			map.put("status", HttpStatus.OK);
			map.put("productDetails", product);
			req.setPsc_id(product.getSubCategory().getPsc_id());
			map.put("simillarProduct", productService.getAllProductByFilter(req));
			map.put("userProduct", productService.getProductsByUser(product.getUserId()));
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		}
		return generateResponse(" NO Product found ", product, HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/products", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllProducts(@RequestBody RequestProduct req) {
		int page = req.getPage();
		int pageNo = 0;
		if (page == 0) {
			pageNo = 0;
		} else {
			pageNo = page * req.getSize();
		}
		if(req.getHome() == null){
			req.setHome("All");
		}
		List<Product> products = productService.getProductList(pageNo, req.getSize(), req.getHome());

		return generateResponse(" Product list ", products, HttpStatus.OK);
	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

	@PostMapping(value = "/products-subcategory", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllProductsInTheSameSubCategory(@RequestBody RequestProduct req) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (((req.getMinCost() != 0 && req.getMaxCost() != 0) && req.getBrandName() != null)) {
			List<Product> products = productService.getProductListInTheSameSubCategory(req.getPsc_id(),
					req.getMinCost(), req.getMaxCost(), req.getBrandName());

			map.put("message", "Product get by sub cat and banner ");
			map.put("status", 200);
			map.put("data", products);
			map.put("bannerList", homeService.getBannerBySubCatId(req.getPsc_id()));

			return new ResponseEntity<Object>(map, HttpStatus.OK);

		} else {
			List<Product> products = productService.getProductListInTheSameSubCategory(req.getPsc_id(), 0, 1000000);

			map.put("message", "Product get by sub cat and banner ");
			map.put("status", 200);
			map.put("data", products);
			map.put("bannerList", homeService.getBannerBySubCatId(req.getPsc_id()));

			return new ResponseEntity<Object>(map, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/getProductsByFilter", headers = "Accept=application/json")
	public ResponseEntity<Object> getProductsByFilter(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (req.getSize() == 0) {
			/* Adde3d for website only */
			req.setSize(18);
		}
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		if (req.getMinCost() == 0) {
			req.setMinCost(0);
		}
		if (req.getMaxCost() == 0) {
			req.setMaxCost(10000000);
		}
		req.setPageNo(pageNo);

		List<Product> products = productService.getAllProductByFilter(req);
		logger.debug(" req " + req.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Product get by  cat and banner ");
		map.put("status", 200);
		map.put("data", products);
		map.put("itemsPerPage", 18);
		map.put("length", productService.getCountProductByFilter(req));
		map.put("bannerList", homeService.getBannerBySubCatId(req.getPsc_id()));

		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/getProductsByUser", headers = "Accept=application/json")
	public ResponseEntity<Object> getProductsByUser(@RequestBody RequestProduct req) {

		List<Product> products = productService.getProductsByUser(req.getUserId());
		ReportRequest request = new ReportRequest();
		request.setUserId(req.getUserId());
		int totalEquiry = commonDao.countInterested(req.getUserId(), "Enquiry");
		request.setPageNo(0);
		request.setUserId(req.getUserId());
		request.setSize(totalEquiry);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Product getProductsByUser");
		map.put("status", 200);
		map.put("data", products);
		map.put("enquiry", commonDao.getEnquiryForProducts(request));
		map.put("enquiryCount", totalEquiry);
		map.put("length", productService.productCount());

		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/products-category", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllProductsUnderTheSameCategory(@RequestBody RequestProduct req) {

		List<Product> products = productService.getAllProductsUnderTheSameCategory(req.getPc_id());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Product get by  cat and banner ");
		map.put("status", 200);
		map.put("data", products);
		map.put("bannerList", homeService.getBannerByCatId(req.getPc_id()));

		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/saveTransaction", headers = "Accept=application/json")
	public ResponseEntity<Object> saveTransaction(@RequestBody Transaction req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		req.setCreateDate(timeStamp);
		req.setUpdateDate(timeStamp);
		try {
			RazorpayClient razorpay = new RazorpayClient("rzp_live_MmKm1tIl8HM05R", "NwjedeYNrjiMUZVRFXwyxDvX");
			logger.debug("Going to save transaction " + req.toString());
			commonDao.saveTransaction(req);
			JSONObject paymentRequest = new JSONObject();
			paymentRequest.put("amount", req.getPaidAmount());
			paymentRequest.put("currency", "INR");
			Payment payment = razorpay.Payments.capture(req.getPaymentId(), paymentRequest);
		} catch (Exception e) {
			logger.debug("ERROR ORCCURED DURING save transaction " + e.toString());
			homemap.put("message", "Transaction not save succesfully " + e.getMessage());
			homemap.put("status", 501);
			return new ResponseEntity<Object>(homemap, HttpStatus.NOT_IMPLEMENTED);
		}
		homemap.put("message", "Transaction save succesfully ");
		homemap.put("status", 200);
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/saveBuyTrans", headers = "Accept=application/json")
	public ResponseEntity<Object> saveBuyTrans(@RequestBody List<TransactionSale> req) {
		Map<String, Object> homemap = new HashMap<String, Object>();

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		List<Integer> patmentsIds = new ArrayList<>();
		try {
			for (TransactionSale transObj : req) {
				transObj.setCreateDate(timeStamp);
				transObj.setUpdateDate(timeStamp);
				String path = saveImageOnServer(transObj.getInvoice(),
						productInvoicePath + File.separator + new Date().getTime() + ".pdf");
				transObj.setInvoice(path);
				TransactionSale trans = commonDao.saveBuyTransaction(transObj);
				patmentsIds.add(trans.getId());
			}
		} catch (Exception e) {
			logger.debug("ERROR ORCCURED DURING save sale transaction " + e.toString());
			homemap.put("message", "Transaction not save succesfully " + e.getMessage());
			homemap.put("status", 501);
			return new ResponseEntity<Object>(homemap, HttpStatus.NOT_IMPLEMENTED);
		}
		homemap.put("message", "save succesfully ");
		homemap.put("status", 200);
		homemap.put("data", patmentsIds);
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/saveSaleTrans", headers = "Accept=application/json")
	public ResponseEntity<Object> saveSaleTrans(@RequestBody TransactionSaleSold req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		//req.setPaymentId(String.join(",", req.getPaymentList().toString()));
		logger.debug(req.getPaymentId());
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		req.setCreateDate(timeStamp);
		req.setUpdateDate(timeStamp);
		req.setStatus("Sold");
		try {
			String path = saveImageOnServer(req.getInvoice(),
					productInvoicePath + File.separator + new Date().getTime() + ".pdf");
			req.setInvoice(path);
			commonDao.saveSoldTransaction(req);
			for (String i : req.getPaymentId().split(",")) {
				TransactionSale obj = new TransactionSale();
				obj.setId(Integer.valueOf(i));
				commonDao.saveBuyTransaction(obj);
			}
		} catch (Exception e) {
			logger.debug("ERROR ORCCURED DURING save saveSoldTransaction transaction " + e.toString());
			homemap.put("message", "Transaction not save succesfully " + e.getMessage());
			homemap.put("status", 501);
			return new ResponseEntity<Object>(homemap, HttpStatus.NOT_IMPLEMENTED);
		}
		homemap.put("message", "save succesfully ");
		homemap.put("status", 200);
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getSaleTrans", headers = "Accept=application/json")
	public ResponseEntity<Object> getSaleTrans(@RequestBody RequestTrans req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", "getSaleTrans succesfully ");
		homemap.put("status", 200);
		homemap.put("data", commonDao.getSaleTrans(req));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getBuyTrans", headers = "Accept=application/json")
	public ResponseEntity<Object> getBuyTrans(@RequestBody RequestTrans req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", "getBuyTrans succesfully ");
		homemap.put("status", 200);
		homemap.put("data", commonDao.getBuyTrans(req));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getSuccessBuyer", headers = "Accept=application/json")
	public ResponseEntity<Object> getSuccessBuyer(@RequestBody RequestTrans req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", "getSuccessBuyer succesfully ");
		homemap.put("status", 200);
		homemap.put("data", commonDao.getSuccessBuyer(req));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getOrderDetails", headers = "Accept=application/json")
	public ResponseEntity<Object> getOrderDetails(@RequestBody RequestTrans req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", "Get Order Details succesfully ");
		homemap.put("status", 200);
		homemap.put("data", commonDao.getOrderDetails(req));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getBuyerProductList", headers = "Accept=application/json")
	public ResponseEntity<Object> getBuyerProductList(@RequestBody RequestProduct req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", "getBuyerProductList succesfully ");
		homemap.put("status", 200);
		homemap.put("data", commonDao.getBuyerProductList(req));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/brands-and-count-subcategory/{psc_id}", headers = "Accept=application/json")
	public List<Product> getBrandNamesOfProductsUnderTheSameSubCategory(@RequestBody RequestProduct req) {
		return (List<Product>) productService.getBrandNameAndCountOfProductListInTheSameSubcategory(req.getPsc_id());
	}

	@GetMapping(value = "/brands-and-count-category/{pc_id}", headers = "Accept=application/json")
	public List<Product> getBrandNamesOfProductsUnderTheSameCategory(
			@Parameter(description = "ID of the product category") @PathVariable("pc_id") int pc_id) {
		return (List<Product>) productService.getBrandNameAndCountOfProductListInTheSameCategory(pc_id);
	}
}
