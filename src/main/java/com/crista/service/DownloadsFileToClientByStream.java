package com.crista.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.google.common.net.HttpHeaders;

import lombok.Data;

@Data
public class DownloadsFileToClientByStream{
	 private SXSSFWorkbook workbook  = new SXSSFWorkbook(100);
	 private Sheet sheet  ; 
	  private void fillHeader(List<?> columns,Sheet sheet) {
		    //sheet  =  workbook.createSheet(sheetName) ;
		    for(int  row  = 0; row <1 ; row++) {
		    Row cellrow  = sheet.createRow(row) ;
		     for(int cellcol  = 0; cellcol< columns.size();cellcol++) {
		    	 Cell cell  = cellrow.createCell(cellcol) ; 
		    	 cell.setCellValue(String.valueOf(columns.get(cellcol)));// use  the default style
		        }
		    }
	   }
	  // this can  be  use with any object or entities 
	public ResponseEntity<StreamingResponseBody> streamFileToclient(Consumer consumer,String type,List<? extends Object> data) {
		StreamingResponseBody responseBody = new StreamingResponseBody() {
         // String data  = "" ;
			@Override
			public void writeTo(OutputStream outputStream) throws IOException {		
		    // call  api from  here 
				//sheet  =  workbook.createSheet(sheetName) ;
			  // fillHeader(columns,sheet);// create  row header 
			   // this will create excel body 
			  consumer.accept(data);   
			 ByteArrayOutputStream outByteStream  = new ByteArrayOutputStream() ;
			  workbook.write(outByteStream);
			  byte[] outArray  =   outByteStream.toByteArray() ;
			  workbook.dispose();
			  workbook.close();
             ByteArrayInputStream  is  =  new ByteArrayInputStream(outArray); 
                byte[] allbytes  = IOUtils.toByteArray(is) ;
                outputStream.write(allbytes) ;
				   outputStream.flush();
			}
		};
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + LocalDateTime.now()+type)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(responseBody);
	}
}











































