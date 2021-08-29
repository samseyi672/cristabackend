package com.crista.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.crista.enties.Brands;
import com.crista.enties.Colour;
import com.crista.enties.Product;
import com.crista.enties.Size;
import com.crista.exceptions.ProductException;
import com.crista.repositories.BrandsRepository;
import com.crista.repositories.ColourRepository;
import com.crista.repositories.SizeRepository;
import com.crista.service.MapValidationServiceError;
import com.crista.service.ProductsService;
import com.crista.utils.ProductAttributeSummary;
import com.crista.utils.ProductDisplay;

import io.jsonwebtoken.lang.Collections;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductsService prdService;
	@Value("${upload.dir}")
	String fileDirectory;

	@Value("${backendurl.req}")
	String backendurl;

	@Autowired
	private MapValidationServiceError mapValidationErrorService;
	
	@Autowired
	private SizeRepository sizeRepo ;
	
	@Autowired
	private BrandsRepository brandsRepo ;
	
	@Autowired
	private ColourRepository colourRepo ;
	
	@GetMapping("/test")
	public String test() {
		return "this  is the test " ;
	}

	@PostMapping("/create")
	public ResponseEntity<?> create(@Valid @ModelAttribute Product product,
			@RequestParam(value = "prdfile") MultipartFile file) throws IOException {
		System.out.println(" product " + product);
		System.out.println("product files");
		File filetoupload = new File(fileDirectory + "/" + file.getOriginalFilename());
		filetoupload.createNewFile();
		FileOutputStream out = new FileOutputStream(filetoupload);
		out.write(file.getBytes());
		out.close();
		System.out.println(" file name " + filetoupload.getName());
		System.out.println(" many sizes " + product.getManysizes());
		System.out.println(" colour " + product.getColour());
		System.out.println(" brand " +product.getModel().split(","));
		String[] prd  = product.getModel().split(",") ;
	    product.setImageurl(filetoupload.getAbsolutePath());
	    product.setPriceofproduct(String.valueOf(Double.parseDouble(product.getProductprice())-Double.parseDouble(product.getTax())));
		System.out.println("price of product "+product.getPriceofproduct());
	    Product p = prdService.create(product);
		System.out.println("product id "+p.getId());		
		//ExecutorService service  = Executors.
	    CompletableFuture.runAsync(()->{
	    	try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	System.out.println("entering into stream");
	    	  List<Size> listofsize =  java.util.Collections.synchronizedList(Arrays.asList(product.getManysizes())).stream()
	                  .map(size->{
	                	    System.out.println(size);
	                        System.out.println("processing size "+size);
	          	    		Size s  = new Size() ;
	          	    		s.setSizetype(size);
	          	    		s.setProductid(p.getId());
	          	    		 return s ;                     
	                     }).collect(Collectors.toList());          
	    	System.out.println("saving size "+listofsize.size()); 
	    	         sizeRepo.saveAll(listofsize);
		         }) ;
	       CompletableFuture.runAsync(()->{	 
	    		try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	System.out.println("beginning to work on colour ");
		    	//return processColourBrandsAndSize(product, finish);
		    	List<Colour> listofcolour = java.util.Collections.synchronizedList(Arrays.asList(product.getColour())).stream().map((colour)->{
					 Colour c  = new Colour() ;
				     c.setColourname(colour);
				     c.setProductid(p.getId());		  
					 return c ;
				       }).collect(Collectors.toList()) ;
		    	System.out.println("saved size "+listofcolour.size());
				colourRepo.saveAll(listofcolour) ;
		      }).thenRunAsync(()->{
		    		System.out.println("beginning to work on brands ");
		    	 	List<Brands> listofbrands = java.util.Collections.synchronizedList(Arrays.asList(prd)).stream().map((br)->{
		    	 		Brands b  = new Brands() ;
		    	 		 b.setProductid(product.getId());
		    	 		 b.setBrandname(br);
			    		 return b ;
			    	       }).collect(Collectors.toList()) ;
		    	 System.out.println("going to save brands "+listofbrands.size());
		    	   brandsRepo.saveAll(listofbrands) ;  // save  all size
	           });
//	       System.exit(0);
		return new ResponseEntity<String>("successful", HttpStatus.OK);
	}

	private String processColourBrandsAndSize(Product product, String finish) {
		List<Colour> listofcolour = Arrays.asList(product.getColour()).stream().map((colour)->{
			Colour c  = new Colour() ;
		     c.setColourname(colour);
		     c.setProductid(product.getId());		  
			 return c ;
		       }).collect(Collectors.toList()) ;
		System.out.println(" ending "+ finish);
		colourRepo.saveAll(listofcolour) ;
		 return "finish2" ;
	}

	@GetMapping("/find/{id}")
	public Product findProductById(HttpServletRequest request, @PathVariable("id") long id,
			@RequestParam(value = "detail", required = false) String detail) {
		System.out.println("context path " + request.getContextPath() + " " + request.getRequestURI() + " address "
				+ request.getRemoteAddr());
		String requestUri = "details";
		if (detail != null) {
			System.out.println("in details ");
			if (detail.equalsIgnoreCase(requestUri)) {
				Product prd = prdService.findProduct(id);
				System.out.println("before " + prd.getImageurl());
				prd.setImageurl(new File(prd.getImageurl()).getName());
				System.out.println(prd.getImageurl());
				prd.setValnumber(null);
				prd.setVendorname(null);
				// prd.setShortsummary(null);
				prd.setMetatitle(null);
				prd.setMetadescription(null);
				prd.setExpirydate(null);
				prd.setDateofcreation(null);
				prd.setFrontpage(false);
				if (prd.getState().equalsIgnoreCase("enable")) {
					prd.setState(null);
				} else if (prd.getState().equalsIgnoreCase("disable")) {
					return new Product(); // return empty products to users
				}
				return prd;
			}
		}
		return prdService.findProduct(id);
	}

	@GetMapping("/findallproduct")
	public List<Product> findallproduct(HttpServletRequest request, @RequestParam("vendorname") String vendorname) {
		return prdService.findByVendorname(vendorname);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		prdService.delete(id);
		return new ResponseEntity<String>("Products removed", HttpStatus.OK);
	}

	@PostMapping("/product")
	public String product(@RequestParam(value = "prdfile") MultipartFile file) throws IOException {
		System.out.println("product files");
		File filetoupload = new File(fileDirectory + "/" + file.getOriginalFilename());
		filetoupload.createNewFile();
		FileOutputStream out = new FileOutputStream(filetoupload);
		out.write(file.getBytes());
		out.close();
		// System.out.println(product);
		// product.setImageurl(filetoupload.getAbsolutePath()); // save the package info
		System.out.println(" saving the product files ");
//		SparkSession spark  = SparkSession.builder().appName("JetCart")
//				.config("spark.sql.dir.warehouse.dir","file:///c:temp/")
//				.master("local[5]")
//				.getOrCreate() ;
		String back = "\\";
		String replace = "/";
		String filepath = filetoupload.getAbsolutePath().replace("\\", "/");
		System.out.println(filepath);
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		FileInputStream excelFiletoProcessed = 
				new FileInputStream(new File(filepath));
		Workbook myworkBook = new XSSFWorkbook(excelFiletoProcessed);
		System.out.println("excel file loaded .....");
		Sheet sheet = myworkBook.getSheetAt(0);
		Map<Integer, List<String>> data = new HashMap<>();
		int i = 0;
		for (Row row : sheet) {
			if (i == 0) {
				i++;
				continue;
			}
			data.put(i, new ArrayList<String>());
			for (Cell cell : row) {
				switch (cell.getCellTypeEnum()) {
				case STRING:
					data.get(new Integer(i)).add(cell.getRichStringCellValue().getString());
					System.out.print(" " + cell.getRichStringCellValue().getString() + " ");
					break;
				case NUMERIC:
					// if it is a date
					if (DateUtil.isCellDateFormatted(cell)) {
						data.get(new Integer(i)).add(cell.getDateCellValue() + "");
						System.out.print(" " + cell.getDateCellValue() + " ");
					} else {
						data.get(new Integer(i)).add(cell.getNumericCellValue() + "");
						System.out.print(" " + cell.getNumericCellValue() + " ");
					}
					break;
				case BOOLEAN:
					data.get(new Integer(i)).add(cell.getBooleanCellValue() + "");
					System.out.print(" " + cell.getBooleanCellValue() + " ");
					break;
				case FORMULA:
					data.get(new Integer(i)).add(cell.getCellFormula() + "");
					System.out.print(" " + cell.getCellFormula() + " ");
					break;
				default:
					data.get(new Integer(i)).add(" ");
					break;
				}

			}
			System.out.println("\nthis is a new row");
			i++;
		}
		System.out.println(" data " + data);

		System.out.println("processing  and uploading to database ");
		Collection<List<String>> filecontents = data.values();
		System.out.println(" contents " + filecontents);
		List<Product> products = filecontents.stream().map(mapper -> {
			System.out.println(" mapper " + mapper);
			Product p = new Product();
			String[] prditems = mapper.toArray(new String[mapper.size()]);
			int y = 0;
			for (String item : prditems) {
				System.out.println(" item " + y + " " + item);
				y++;
			}
			System.out.println(" prditems " + prditems[0]);
			p.setProductname(prditems[0]);
			p.setShortsummary(prditems[1]);
			p.setProdtype(prditems[2]);
			p.setMetatitle(prditems[3]);
			p.setCategoryid(prditems[4]);
			p.setProddescription(prditems[5]);
			p.setTax(prditems[6]);
			p.setValnumber(prditems[7]);
			p.setFrontpage(Boolean.parseBoolean(prditems[8].toLowerCase()));
			p.setProductprice(prditems[9]);
			p.setSubcategoryid(prditems[10]);
			p.setProductstatus(prditems[11]);
			p.setState(prditems[12]);
			// System.out.println(" date "+ prditems[13]);
			LocalDateTime ldt = LocalDateTime.parse(prditems[13],
					DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss zzz yyyy"));
			System.out.println(" date " + ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = sdt.parse(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				p.setExpirydate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			p.setVendorname(prditems[14]);
			p.setModel(prditems[15]);
			p.setMetadescription(prditems[16]);
			p.setProductcode(prditems[17]);
			p.setProductquantity(prditems[18]);
			p.setSize(prditems[19]);
			return p;
		}).collect(Collectors.toList());
		System.out.println("products " + products);
		prdService.save(products); // save products
////	Dataset<Row> excedata  = spark.read().format("com.crealytics.spark.excel")
////			.option("header","true")
////			.option("useHeader","true")
////			.option("inferSchema","true")
////			.load(filepath)  ;
////	        excedata.show(10);
//		prdService.save(products);
		System.out.println("preparing  to call the image api ");
		return "file uploaded";
	}

	@PostMapping(value = "/uploadfiles", consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")
	public String uploadfiles(HttpServletRequest request, @RequestParam("vendorname") String vendorname)
			throws IOException {
		System.out.println("product images files ......" + " vendorname " + vendorname);
		// load images products where images are null
		// List<Product> list = prdService.findByVendorname(vendorname) ;
		// System.out.println(" list "+list+ " size "+list.size());
		List<Product> products = prdService.findProductByImageUrl(vendorname);
		System.out.println("Products " + products);
		MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
//		    System.out.println(" vendorname 2 "+multipart.getParameter("vendorname")) ;
		Iterator<String> iter = multipart.getFileNames();
		List<Product> listofnewproducts = java.util.Collections.synchronizedList(new ArrayList<>());
		int i = 0;
		while (iter.hasNext()) {
			String uploadfile = iter.next();
			MultipartFile file = multipart.getFile(uploadfile);
			// check the list for file name.
			File filetoupload = new File(fileDirectory + "/" + file.getOriginalFilename());
			filetoupload.createNewFile();
			System.out.println(" file name before creation " + filetoupload.getName());
			// check before writing to disk
			String filename = filetoupload.getName();
			// check the entire disk
			System.out.println(" products " + products + " filename " + filename);
			for (Product p : products) { //
				try {
					System.out.println(" p " + p);
					if (p.getProductname().equalsIgnoreCase(filename)) {
						System.out.println(" inside if ");
						p.setImageurl(filetoupload.getAbsolutePath());
						FileOutputStream out = new FileOutputStream(filetoupload);
						out.write(file.getBytes());
						out.close();
						listofnewproducts.add(p);
						System.out.println(" file name uploaded " + filetoupload.getName());
					}
					i++;
					System.out.println("counter " + i);
				} catch (Exception ex) {
					System.out.println("exception " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		prdService.save(listofnewproducts);
		System.out.println("list of new" + listofnewproducts);
		return "All files uploaded  successfully ";
	}

	@GetMapping("/allproduct")
	public String getallProduct(@RequestParam("pageSize") int pageSize, @RequestParam("pageNumber") int pageNumber) {
		System.out.println("entered ............");
		return prdService.getAllproducts(pageSize, pageNumber);
	}
    
	       @GetMapping("/displayandloadsimilaritems/{id}")
	  public List<?> displayandloadsimilaritems(@PathVariable("id") long id){
	   		Product p = prdService.findProduct(id);
			if (p == null) {
				throw new ProductException("product not found ");
			}
			// take some properties out product
			ProductAttributeSummary productAttributeSummary = new ProductAttributeSummary();
			productAttributeSummary.setVendorname(p.getVendorname());
			productAttributeSummary.setId(p.getId());
			productAttributeSummary.setImageurl(new File(p.getImageurl()).getName());
			productAttributeSummary.setModel(p.getModel());
			productAttributeSummary.setCategoryid(p.getCategoryid());
			productAttributeSummary.setProddescription(p.getProddescription());
			productAttributeSummary.setProductname(p.getProductname());
			productAttributeSummary.setProductquantity(p.getProductquantity());
			productAttributeSummary.setProductstatus(p.getProductstatus());
			productAttributeSummary.setShortsummary(p.getShortsummary());
			productAttributeSummary.setSize(p.getSize());
			productAttributeSummary.setState("");
			productAttributeSummary.setTax("");
			productAttributeSummary.setProductprice(p.getProductprice());
		    // get the names of simialr products 
		  List<Product> listOfProducts  =  prdService.loadSimilarItems2(productAttributeSummary.getProductname(),5) ;
		  //process  the list 
		List<ProductAttributeSummary>  listOfProductsAttri =   listOfProducts.parallelStream().map(f->{
			ProductAttributeSummary productAttributeSummary2 = new ProductAttributeSummary();
			productAttributeSummary2.setVendorname(p.getVendorname());
			productAttributeSummary2.setId(p.getId());
			productAttributeSummary2.setImageurl(new File(p.getImageurl()).getName());
			productAttributeSummary2.setModel(p.getModel());
			productAttributeSummary2.setCategoryid(p.getCategoryid());
			productAttributeSummary2.setProddescription(p.getProddescription());
			productAttributeSummary2.setProductname(p.getProductname());
			productAttributeSummary2.setProductquantity(p.getProductquantity());
			productAttributeSummary2.setProductstatus(p.getProductstatus());
			productAttributeSummary2.setShortsummary(p.getShortsummary());
			productAttributeSummary2.setSize(p.getSize());
			productAttributeSummary2.setState("");
			productAttributeSummary2.setTax("");
			productAttributeSummary2.setProductprice(p.getProductprice());  
			return productAttributeSummary2 ;
		  }).collect(Collectors.toList()) ;
		 listOfProductsAttri.add(productAttributeSummary) ;
		   return listOfProductsAttri ;
	   }
	@GetMapping("/display/{id}")
	public ProductAttributeSummary product(@PathVariable("id") long id) {
		System.out.println("entered ............");
		Product p = prdService.findProduct(id);
		if (p == null) {
			throw new ProductException("product not found ");
		}
		// take some properties out product
		ProductAttributeSummary productAttributeSummary = new ProductAttributeSummary();
		productAttributeSummary.setVendorname(p.getVendorname());
		productAttributeSummary.setId(p.getId());
		productAttributeSummary.setImageurl(new File(p.getImageurl()).getName());
		productAttributeSummary.setModel(p.getModel());
		productAttributeSummary.setCategoryid(p.getCategoryid());
		productAttributeSummary.setProddescription(p.getProddescription());
		productAttributeSummary.setProductname(p.getProductname());
		productAttributeSummary.setProductquantity(p.getProductquantity());
		productAttributeSummary.setProductstatus(p.getProductstatus());
		productAttributeSummary.setShortsummary(p.getShortsummary());
		productAttributeSummary.setSize(p.getSize());
		productAttributeSummary.setState("");
		productAttributeSummary.setTax("");
		productAttributeSummary.setProductprice(p.getProductprice());
		return productAttributeSummary;
	}

	@GetMapping("/cartdisplay")
	public List<ProductDisplay> displayCart(@RequestParam("min") int min, @RequestParam("max") int max) {
		System.out.println("entered method ......");
		List<Product> cart = prdService.getProductForDisplay(min, max);
		List<ProductDisplay> display = cart.parallelStream().map(p -> {
			ProductDisplay prd = new ProductDisplay();
			prd.setId(p.getId());
			prd.setProductname(p.getProductname());
			prd.setProductprice(p.getProductprice());
			prd.setProductoldprice(p.getProductoldprice());
			prd.setImageurl(p.getImageurl());
			return prd;
		}).collect(Collectors.toList());
		return display;
	}

	@GetMapping("/cartspecial")
	public List<ProductDisplay> displayCartBySpecial(@RequestParam("min") int min, @RequestParam("max") int max) {
		System.out.println("entered method ......");
		List<Product> cart = prdService.getProductForDisplayBySpecial(min, max);
		List<ProductDisplay> display = cart.parallelStream().map(p -> {
			ProductDisplay prd = new ProductDisplay();
			prd.setId(p.getId());
			prd.setProductname(p.getProductname());
			prd.setProductprice(p.getProductprice());
			prd.setProductoldprice(p.getProductoldprice());
			prd.setImageurl(p.getImageurl());
			return prd;
		}).collect(Collectors.toList());
		return display;
	}

	@GetMapping("/cartnewarrival")
	public List<ProductDisplay> displayCartByNewArrival(@RequestParam("min") int min, @RequestParam("max") int max) {
		System.out.println("entered method ......");
		List<Product> cart = prdService.getProductForDisplayByNewarrival(min, max);
		List<ProductDisplay> display = cart.parallelStream().map(p -> {
			ProductDisplay prd = new ProductDisplay();
			prd.setId(p.getId());
			prd.setProductname(p.getProductname());
			prd.setProductprice(p.getProductprice());
			prd.setProductoldprice(p.getProductoldprice());
			prd.setImageurl(p.getImageurl());
			return prd;
		}).collect(Collectors.toList());
		return display;
	}

	@GetMapping("/cartfeatured")
	public List<ProductDisplay> displayCartByFeatured(@RequestParam("min") int min, @RequestParam("max") int max) {
		System.out.println("entered method ......");
		List<Product> cart = prdService.getProductForDisplayByFeatured(min, max);
		List<ProductDisplay> display = cart.parallelStream().map(p -> {
			ProductDisplay prd = new ProductDisplay();
			prd.setId(p.getId());
			prd.setProductname(p.getProductname());
			prd.setProductprice(p.getProductprice());
			prd.setProductoldprice(p.getProductoldprice());
			prd.setImageurl(p.getImageurl());
			return prd;
		}).collect(Collectors.toList());
		// process the dom images to display
		return display;
	}

	@GetMapping("/byspecial")
	public String productBySpecial(int pageSize, int pageNumber) {
		return prdService.paginateProductBySpecial(pageSize, pageNumber, backendurl);
	}

	@GetMapping("/byfeatured")
	public String productByFeatured(int pageSize, int pageNumber) {
		return prdService.paginateProductByFeatured(pageSize, pageNumber, backendurl);
	}

	@GetMapping("/bynewarrival")
	public String productByNewArrival(int pageSize, int pageNumber) {
		return prdService.paginateProductByNewArrival(pageSize, pageNumber, backendurl);
	}

	@GetMapping("/loadproduct")
	public Map<String,List<Product>> loadProductForMarketPlace() {
		return prdService.loadProductForMarketPlace(backendurl);
	}
	@GetMapping("/loadcollections")
	public String loadCollections(@RequestParam("pageNumber") int pageNumber,
			@RequestParam("pageSize") int pageSize) {
		return prdService.loadProductForCollectionByPagination(pageSize, pageNumber,backendurl) ;
	}
	@GetMapping("/loadproductcategory/{productcat}")
	public List<ProductAttributeSummary>loadProductCategory(@PathVariable("productcat") String productcat) {
		System.out.println("entered into controller ........");
		return prdService.loadCategorySubCategoryidAndProductName(0,30,productcat);
	}
	
	  @GetMapping("/loadsimilaritems")
	public List<ProductDisplay> loadsimilaritems(@RequestParam("thename") String thename,
			@RequestParam("price") String price,@RequestParam(name ="size",required = false) 
	int size){
		  List<Product> listOfProducts  =  prdService.loadSimilarItems(thename, price, size) ;	  
		List<ProductDisplay> listOfDisplayProducts  = listOfProducts.parallelStream().map(f->{
			  ProductDisplay d  = new ProductDisplay() ;
			  d.setProductname(f.getProductname());
			  d.setProductprice(f.getProductprice());
			  d.setId(f.getId());
			  d.setImageurl(new File(f.getImageurl()).getName());
			  return d ;
		  }).collect(Collectors.toList()) ; 
	    return  listOfDisplayProducts ;	
	}
	     @GetMapping("/loadsimilaritems2")
		public List<ProductDisplay> loadsimilaritems2(@RequestParam("thename") String thename,
				@RequestParam("price") String price,@RequestParam(name ="size",required = false) 
		int size){
			  List<Product> listOfProducts  =  prdService.loadSimilarItems2(thename,size) ;	  
			List<ProductDisplay> listOfDisplayProducts  = listOfProducts.parallelStream().map(f->{
				  ProductDisplay d  = new ProductDisplay() ;
				  d.setProductname(f.getProductname());
				  d.setProductprice(f.getProductprice());
				  d.setId(f.getId());
				  d.setImageurl(new File(f.getImageurl()).getName());
				  System.out.println("description " +f.getProddescription());
				  d.setProductdescription(f.getProddescription());
				  return d ;
			  }).collect(Collectors.toList()) ; 
		    return  listOfDisplayProducts ;	
		}
	         @GetMapping("/loadsearchitem")
			public List<ProductDisplay> loadsimilaritems2(@RequestParam("thename") String thename,
					@RequestParam(name ="size",required = false) 
			int size){
				  List<Product> listOfProducts  =  prdService.loadSimilarItems2(thename,size) ;	  
				List<ProductDisplay> listOfDisplayProducts  = listOfProducts.parallelStream().map(f->{
					  ProductDisplay d  = new ProductDisplay() ;
					  d.setProductname(f.getProductname());
					  d.setProductprice(f.getProductprice());
					  d.setId(f.getId());
					  d.setImageurl(new File(f.getImageurl()).getName());
					  System.out.println("description " +f.getProddescription());
					  d.setProductdescription(f.getProddescription());
					  return d ;
				  }).collect(Collectors.toList()) ; 
			    return  listOfDisplayProducts ;	
			} 
	         
	      @GetMapping("/loadsearchproduct")
	     public List<ProductDisplay> loadsearchproduct(@RequestParam("id")  long id,@RequestParam("size") int size) throws InterruptedException, ExecutionException{	    	
	    	  List<Product> listOfProducts  =   prdService.loadSearchProduct(id,size) ;	
	    	 //  return listOfProducts ;
	    	  List<ProductDisplay> listOfDisplayProducts  = listOfProducts.parallelStream().map(f->{
				  ProductDisplay d  = new ProductDisplay() ;
				  d.setProductname(f.getProductname());
				  d.setProductprice(f.getProductprice());
				  d.setId(f.getId());
				  d.setImageurl(new File(f.getImageurl()).getName());
				  System.out.println("description " +f.getProddescription());
				  d.setProductdescription(f.getProddescription());
				  return d ;
			  }).collect(Collectors.toList()) ; 
		    return  listOfDisplayProducts ; 
	      }
	      
	      @GetMapping("/categoryitems")
	   public String loadcategoryitems(@RequestParam(name="size",required = false) 
	   String size[] ,@RequestParam(name="brands",required = false) String brands[],
	   @RequestParam(name="type",required = false) String type[],
	   @RequestParam(name="price",required = false) String price,
	   @RequestParam(name="pageNumber",required = false) String pageNumber,
	   @RequestParam(name="pageSize",required = false) String pageSize,
	   @RequestParam(name="colour",required = false) String colour[]){
	     System.out.println("price "+price);	
	     System.out.println("brands "+brands.length+" content "+brands[0].isEmpty());
	     System.out.println("type "+type.length+" content "+ type[0].isEmpty());
	     //System.out.println("brands"+brands.length);
	     System.out.println("pageNumber "+pageNumber);	
	     System.out.println("pageSize "+pageSize);	
	     String result  = prdService.dynamicQuery(size, brands, type, price, pageNumber, pageSize, colour) ;
//	    	  return "brands "+brands.length+" price "+price+" type "+type.length+" pagenumber "+
//	     pageNumber+" pagesize "+pageSize+" size "+size.length+" colour "+colour.length ;  
	    	return result ;
	      }   
}





















































