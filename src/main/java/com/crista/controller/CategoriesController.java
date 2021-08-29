package com.crista.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.crista.enties.Category;
import com.crista.securityconfig.JwtRequest;
import com.crista.service.CategoriesService;
import com.crista.service.DownloadsFileToClientByStream;

import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

	@Autowired
	CategoriesService catService;

	@Value("${upload.dir}")
	String fileDirectory;

	@PostMapping(value = "/category", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")
	public ResponseEntity<?> createDetails(@Valid @ModelAttribute Category category, BindingResult result,
			@RequestParam(value = "imageurl") MultipartFile file) throws IOException {
		System.out.println("got here to category creation 2 ");
		File filetoupload = new File(fileDirectory + "/" + file.getOriginalFilename());
		filetoupload.createNewFile();
		FileOutputStream out = new FileOutputStream(filetoupload);
		out.write(file.getBytes());
		;
		
		out.close();
		System.out.println(category);
//		    	String randomstring  =  RandomString.make(20);
//		    	category.setTrackingnumber(randomstring);
		category.setImageurl(filetoupload.getAbsolutePath()); // save the package info
		catService.createCategories(category);
		return new ResponseEntity<String>("Categories created", HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<?> createDetails(@Valid @ModelAttribute Category category, BindingResult result) {
		System.out.println("got here to category " + category);
		return new ResponseEntity<Category>(catService.createCategories(category), HttpStatus.OK);
	}

	 @GetMapping("/category")
	public List<Category> getCategory() {
		System.out.println("got here to category ");
		return catService.getAllCategories();
	}

	  @PutMapping("/category")
	public String updateCategory (@Valid @ModelAttribute Category category,BindingResult result) {
		System.out.println("got here to category "+category);
		catService.createCategories(category);
		return "successful";
	}
	      @PostMapping("/category")
		public String postCategory (@Valid @ModelAttribute Category category,BindingResult result) {
			System.out.println("got here to category "+category);
			catService.createCategories(category);
			return "successful";
		}

	      @DeleteMapping("/category")
		public String deleteCategory (@Valid @ModelAttribute Category category,BindingResult result) {
			System.out.println("got here to category "+category);
			catService.deleteCategories(category);
			return "successful";
		}
	     
        @GetMapping("/filereport")
      public ResponseEntity<StreamingResponseBody> excel(){
        	 DownloadsFileToClientByStream dwds = new DownloadsFileToClientByStream() ;
        	    //Sheet sh  =  dwds.getWorkbook().createSheet(sheetname) ;
        	List<Category> cat  =  	catService.getAllCategories()  ;
        	System.out.println(cat.toString());  
        	AtomicInteger counter  =  new AtomicInteger() ;
        	//int counter =  0 ;
        	 counter.set(0);
        	ResponseEntity<StreamingResponseBody> rs= 
        			dwds.streamFileToclient(new Consumer<List<Category>>(){
        		@Override
				public void accept(List<Category> t) {				 
			        	// counter.set(0);
					  System.out.println("in accept "+t.toString());
					    // int  counter  = 0 ;
					  List<String> columns  = Arrays.asList(new String[]{"categoryname","pricerange"}) ;
					  Sheet sh  =  dwds.getWorkbook().createSheet("categorylist") ;
					  for(int  row  = 0; row <1 ; row++) {
			     		    Row cellrow  = sh.createRow(row) ;
			     		     for(int cellcol  = 0; cellcol< columns.size();cellcol++) {
			     		    	 Cell cell  = cellrow.createCell(cellcol) ; 
			     		    	 cell.setCellValue(String.valueOf(columns.get(cellcol)));// use  the default style
			     		        }
			     		    }
		        		for(Category cate:t) {
		        		System.out.println("in loop "+cate.getCategoryname());
		        		System.out.println(" counter "+counter);
		        		int count  = counter.get() ;
		        		System.out.println(" sheet "+sh+" workbook "+ dwds.getWorkbook());
		        		Row row  = sh.createRow(count) ;
		        		Cell cell  =    row.createCell(0) ;
		        		cell.setCellValue(cate.getCategoryname());
		        		Cell cell_1  =    row.createCell(1) ;
		        		cell_1.setCellValue(cate.getPricerange());
		        		  counter.incrementAndGet() ;
		        		   //counter++ ;
		        	     }
				}    		
        	},"category.xlsx",cat)  ;
    	   return rs ;
      }
	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		return new ResponseEntity<String>("Removed", HttpStatus.OK);
	}
}
