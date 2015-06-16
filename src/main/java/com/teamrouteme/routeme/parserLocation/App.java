package com.teamrouteme.routeme.parserLocation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.json.JSONArray;
import org.parse4j.Parse;
import org.parse4j.ParseObject;

/**
 * Hello world!
 *
 */
public class App 
{	
	private static final String APPID ="61vwm3BXU0U6Vvz78Kasq5Ovih6HWw27UZn7WrAR";
	private static final String APPIDREST ="R2DKi0uvvjd2tcJPLEmsFDe02mZLlMazEub31ioA";
	
	private static final String ORARIOFERIALE ="orario feriale";
	private static final String LINEA ="linea";
	
	
    public static void main( String[] args ) throws Exception
    {
        
        Parse.initialize(APPID, APPIDREST);
        
        
        String mytext = extractTextFromPDFDocument("42fe.pdf");
        
        String[] mySplit = mytext.split("\n");
        
        parseAndSave(mySplit);
        
        System.out.println( mytext);
        
    }

    private static void parseAndSave(String [] rows) {
		// TODO Auto-generated method stub
    	
    	int i=0;
    	
    	ParseObject orariCitta = new ParseObject("bus_salerno");
    	String tipoCorsa;
    	
    	String lower;
    	
    	String regexOrario = "[0-9]";
    	
    	JSONArray jsonTags = new JSONArray();
    	
    	for(String row : rows){
    		
    		lower = row.toLowerCase();
    		
    		if(lower.contains(ORARIOFERIALE))
    			orariCitta.put("tipo_corsa", "F");
    		if(lower.contains(LINEA))
    			orariCitta.put("linea", row);
    		if(lower.contains("andata"))
    			continue;
    		
    		
    		if(Pattern.matches(".*[0-9]+[.][0-9]+.*", lower)){
    			//orariCitta.put();
    			
    			String [] timesStop = lower.split(" ");
    			
    			boolean flag = false;
    			
    			for(String stop : timesStop){
    				
    				if(!flag)
    					jsonTags.put(stop);
    				
    				flag = true;
  
    			}
    			
    		}
    		
    		orariCitta.put("fermate", jsonTags);
    		
    		i++;
    	}
    	
    	orariCitta.saveInBackground();
    	
		
	}

	public static String extractTextFromPDFDocument(String documentName) throws Exception {
		
    	InputStream inputStream = new FileInputStream(documentName);
    	
    	PDFTextStripper pdfTextStripper = null;
		PDDocument pdDocument = null;
		String extractText = null;

		PDFParser parser = new PDFParser(inputStream);
		parser.parse();
		pdDocument = parser.getPDDocument();
		pdfTextStripper = new PDFTextStripper();
		extractText = pdfTextStripper.getText(pdDocument);
		pdDocument.close();

		return extractText;
	}

    
}
