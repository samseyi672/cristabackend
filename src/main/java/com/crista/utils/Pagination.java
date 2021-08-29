package com.crista.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Pagination {
	String baseURL        = "";
    int totalRows      = 0;
    int perPage         = 0;
    int numLinks        =  5;
    int currentPage    =  0;
    String firstLink       = "&lsaquo; First";
    String nextLink        = "&gt;";
    String prevLink        = "&lt;";
    String lastLink        = "Last &rsaquo;";
    String fullTagOpen    = "<div class='pagination'>";
    String fullTagClose    = "</div>";
    String firstTagOpen    = "";
    String firstTagClose    = "&nbsp;";
    String lastTagOpen    = "&nbsp;";
    String lastTagClose    = "";
    String curTagOpen        = "&nbsp;<b>";
    String curTagClose    = "</b>";
    String nextTagOpen    = "&nbsp;";
    String nextTagClose    = "&nbsp;";
    String prevTagOpen    = "&nbsp;";
    String prevTagClose    = "";
    String numTagOpen        = "&nbsp;";
    String numTagClose    = "";
    String anchorClass    = "";
    boolean showCount      = true;
    int currentOffset    = 0;
    String contentDiv     = "";
    String additionalParam= "";
    String filter      = "";
       
    public Pagination(Map params){
        if (params.size() > 0){  

            Set set=params.entrySet();//Converting to Set so that we can traverse  
            Iterator itr=set.iterator();  
            while(itr.hasNext()){  
                //Converting to Map.Entry so that we can get key and value separately  
                Map.Entry entry=(Map.Entry)itr.next();  
                //System.out.println(entry.getKey()+" "+entry.getValue());  
                String temp = entry.getKey().toString();
               if(temp.equals("totalRows")){
                   this.totalRows = Integer.parseInt(entry.getValue().toString());                           
                           }                   
               else if(temp.equals("perPage")){
                   this.perPage = Integer.parseInt(entry.getValue().toString());                           
                           }
               else if(temp.equals("currentPage")){
                   this.currentPage = Integer.parseInt(entry.getValue().toString());                           
                           }                
               else if(temp.equals("filter")){
                   this.filter = entry.getValue().toString();                           
                           }
            }                    
           
           
        }
        
        if (this.anchorClass != ""){
            this.anchorClass = "class='"+this.anchorClass+"'";
        }    
    }
    
        
    public String paginate(){ 
        int numPages = 0; String info = "";         
        String output = "";
        // If total number of rows is zero, do not need to continue
        if (this.totalRows == 0 || this.perPage == 0){
           return "";
        }

        // Calculate the total number of pages
        double tempTotal = this.totalRows;
        double tempPage = this.perPage;        
        numPages = (int)Math.ceil(tempTotal / tempPage);
       
        // Is there only one page? will not need to continue
        if (numPages == 1){
            if (this.showCount){
                info = "Showing : " + this.totalRows;
                return info;
            }else{
                return "";
            }
        }

        
        // Showing links notification
        if (this.showCount){
           int currentOffset = this.currentPage;
           info = "Showing " + ( currentOffset + 1 ) + " to " ;
        
           if( ( currentOffset + this.perPage ) < ( this.totalRows - 1 ) )
              info += currentOffset + this.perPage;
           else
              info += this.totalRows;
        
           info += " of " + this.totalRows + " | ";
        
           output += info;
        }
        
        this.numLinks = (int)this.numLinks;
        
        // Is the page number beyond the result range? the last page will show
        if (this.currentPage > this.totalRows){        	
            this.currentPage = (numPages - 1) * this.perPage;
        }
        
        int uriPageNum = this.currentPage;
        
        this.currentPage = Math.round((this.currentPage/this.perPage) + 1);

        // Calculate the start and end numbers. 
        int start = ((this.currentPage - this.numLinks) > 0) ? this.currentPage - (this.numLinks - 1) : 1;
        int end   = ((this.currentPage + this.numLinks) < numPages) ? this.currentPage + this.numLinks : numPages;
      
        // Render the "First" link
        if  (this.currentPage > this.numLinks){
            output += this.firstTagOpen 
                + this.getLink( "" , this.firstLink)
                + this.firstTagClose;
        }

        // Render the "previous" link
        if  (this.currentPage != 1){ 
            int i = uriPageNum - this.perPage;
            String j = "";
            if (i == 0) j = "";           
            output += this.prevTagOpen 
                + this.getLink( j, this.prevLink )
                + this.prevTagClose;
                   
        }

        // Write the digit links
        for (int loop = start -1; loop <= end; loop++){
            int i = (loop * this.perPage) - this.perPage;
                    
            if (i >= 0){
                if (this.currentPage == loop){
                    output += this.curTagOpen+loop+this.curTagClose;
                }else{
                    String n = (i == 0) ? "" : String.valueOf(i);
                    output += this.numTagOpen
                        + this.getLink( n, String.valueOf(loop) )
                        + this.numTagClose;
                }
            }
        }

        // Render the "next" link
        if (this.currentPage < numPages){
            output += this.nextTagOpen 
                 + this.getLink(String.valueOf(this.currentPage * this.perPage) , this.nextLink )
                + this.nextTagClose;                     
        }

        // Render the "Last" link
        if ((this.currentPage + this.numLinks) < numPages){
            int i = ((numPages * this.perPage) - this.perPage);
            output += this.lastTagOpen + this.getLink( String.valueOf(i), this.lastLink ) + this.lastTagClose;
        }

        // Remove double slashes
        output = output.replaceAll("#([^:])//+#", "\\1/");

        // Add the wrapper HTML if exists
        output = this.fullTagOpen+output+this.fullTagClose;
        
        return output;        
    }
    
    public String getLink( String count, String text) {
        String linkClick = "";
        if(this.filter == "" && this.contentDiv == "")
            return "<a href='"+this.baseURL+"?"+count+" ' "+this.anchorClass+">"+text+"</a>";
        int pageCount=0;
        if(!count.isEmpty())pageCount = Integer.parseInt(count);
        
        if(!(this.filter.isEmpty())){
            linkClick = "onclick=\""+this.filter+"("+pageCount+")\" ";
        }else{
            this.additionalParam = "{'page' : pageCount}";
            linkClick = "onclick=\"$.post('"+ this.baseURL+"', "+ this.additionalParam +", function(data){ $('#"+ this.contentDiv + "').html(data); }); return false;\"";
        }
        
        return "<a href=\"javascript:void(0);\" " + this.anchorClass + " "+ linkClick +">"+ text +"</a>";
        
    }
}
