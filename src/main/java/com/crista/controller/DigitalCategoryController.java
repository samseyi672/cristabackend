package com.crista.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crista.enties.SubCategories;
import com.crista.service.SubCategoryService;

@RestController
@RequestMapping("/digital")
public class DigitalCategoryController {

	@Autowired
	DigitalCategoryService subCatService;
	
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
		//subCatService.subCreateCategories(subcategory);
		return new ResponseEntity<String>("sub Categories created", HttpStatus.OK);
	}
}
