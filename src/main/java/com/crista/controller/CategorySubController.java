package com.crista.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import com.crista.enties.SubCategories;
import com.crista.service.DownloadsFileToClientByStream;
import com.crista.service.SubCategoryService;

@RestController
@RequestMapping("/subcat")
public class CategorySubController {
	
	@Autowired
	SubCategoryService subCatService;
	
	@Value("${upload.dir}")
	String fileDirectory;

	@PostMapping(value = "/createsubcategory", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")
	public ResponseEntity<?> createDetails(@Valid @ModelAttribute SubCategories subcategory, BindingResult result,
			@RequestParam(value = "imageur") MultipartFile file) throws IOException {
		System.out.println("sub category creation 2 "+ subcategory);
		File filetoupload = new File(fileDirectory + "/" + file.getOriginalFilename());
		filetoupload.createNewFile();
		FileOutputStream out = new FileOutputStream(filetoupload);
		out.write(file.getBytes());
		out.close();
		System.out.println(subcategory);
		subcategory.setImageurl(filetoupload.getAbsolutePath()); // save the package info
		System.out.println(" saving the subcategories ");
		subCatService.subCreateCategories(subcategory);
		return new ResponseEntity<String>("sub Categories created", HttpStatus.OK);
	}
	  
	 @GetMapping("/subcategory")      
    public List<SubCategories> findAllSubCategories(){
		List<String> sub = subCatService.findAllSub() ;
		System.out.println(" sub "+ sub);
		List<SubCategories> listofsub  = sub.parallelStream().map(mapper->{
		  System.out.println(" mapper "+mapper);
		SubCategories cat   = new SubCategories() ;
		     String content[]  = mapper.split(",")  ;
		     cat.setId(Long.parseLong(content[0]));
		     cat.setSubcategoryname(content[1]);
		     cat.setPricerange(content[2]);
		    return cat ;
		    }).collect(Collectors.toList());
    	 return listofsub ;
    }
	    @GetMapping("/filereport")
     public ResponseEntity<StreamingResponseBody> excel(){
       	 DownloadsFileToClientByStream dwds = new DownloadsFileToClientByStream() ;
       	    //Sheet sh  =  dwds.getWorkbook().createSheet(sheetname) ;
       	List<SubCategories> cat  =  	subCatService.getAllSubCategories()  ;
       	System.out.println(cat.toString());  
       	AtomicInteger counter  =  new AtomicInteger() ;
       	//int counter =  0 ;
       	 counter.set(0);
       	ResponseEntity<StreamingResponseBody> rs= dwds.streamFileToclient(new Consumer<List<SubCategories>>(){
       		@Override
				public void accept(List<SubCategories> t) {				 
			        	// counter.set(0);
					  System.out.println("in accept "+t.toString());
					    // int  counter  = 0 ;
					  List<String> columns  = Arrays.asList(new String[]{"subcategoryname","pricerange"}) ;
					  Sheet sh  =  dwds.getWorkbook().createSheet("subcategorylist") ;
					  for(int  row  = 0; row <1 ; row++) {
			     		    Row cellrow  = sh.createRow(row) ;
			     		     for(int cellcol  = 0; cellcol< columns.size();cellcol++) {
			     		    	 Cell cell  = cellrow.createCell(cellcol) ; 
			     		    	 cell.setCellValue(String.valueOf(columns.get(cellcol)));// use  the default style
			     		        }
			     		    }
		        		for(SubCategories cate:t) {
		        		System.out.println("in loop "+cate.getSubcategoryname());
		        		System.out.println(" counter "+counter);
		        		int count  = counter.get() ;
		        		System.out.println(" sheet "+sh+" workbook "+ dwds.getWorkbook());
		        		Row row  = sh.createRow(count) ;
		        		Cell cell  =    row.createCell(0) ;
		        		cell.setCellValue(cate.getSubcategoryname());
		        		Cell cell_1  =    row.createCell(1) ;
		        		cell_1.setCellValue(cate.getPricerange());
		        		  counter.incrementAndGet() ;
		        		   //counter++ ;
		        	     }
				}    		
       	},"category.xlsx",cat)  ;
   	   return rs ;
     }

		  @PutMapping("/subcategory")
		public String updateCategory (@Valid @ModelAttribute SubCategories subcategory,BindingResult result) {
			System.out.println("got here to category "+subcategory);
			subCatService.subCreateCategories(subcategory);
			return "successful";
		}
		      @PostMapping("/subcategory")
			public String postCategory (@Valid @ModelAttribute SubCategories subcategory,BindingResult result) {
				System.out.println("got here to category "+ subcategory);
				subCatService.subCreateCategories(subcategory);
				return "successful";
			}

		      @DeleteMapping("/subcategory")
			public String deleteCategory (@Valid @ModelAttribute SubCategories subcategory,BindingResult result) {
				System.out.println("got here to category "+ subcategory);
				subCatService.delete(subcategory);
				return "successful";
			}
	       @DeleteMapping("/delete")
		public ResponseEntity<?> delete(@PathVariable("id") long id) {
			return new ResponseEntity<String>("Removed", HttpStatus.OK);
		}
}



































