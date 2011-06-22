/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.chart.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.engines.AbstractEngineAction;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.mime.MimeUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;




/**
 * The Class ExportHighChartsAction.
 * 
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class ExportHighChartsAction extends AbstractEngineAction {	
	
	// INPUT PARAMETERS
	public static String SVG = "svg";
	public static String OUTPUT_FORMAT = "type";
/*
	public static String OUTPUT_FORMAT_PNG = "image/png";
	public static String OUTPUT_FORMAT_JPEG = "image/jpeg";
	public static String OUTPUT_FORMAT_PDF = "application/pdf";
	public static String OUTPUT_FORMAT_SVG = "image/svg+xml";
*/
 

	public static String OUTPUT_FORMAT_PNG = "PNG";
	public static String OUTPUT_FORMAT_JPEG = "JPG";
	public static String OUTPUT_FORMAT_PDF = "PDF";
	public static String OUTPUT_FORMAT_SVG = "SVG+XML";

	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(ExportHighChartsAction.class);
    private static final BASE64Decoder DECODER = new BASE64Decoder();
    
    public static final String ENGINE_NAME = "SpagoBIChartEngine";
		
    public void service(SourceBean request, SourceBean response) {
    	
    	logger.debug("IN");
    	InputStream inputStream = null;
    	OutputStream outputStream = null;
    	
    	try {
			super.service(request, response);
			
			freezeHttpResponse();
			
			String svg = this.getAttributeAsString(SVG);
			inputStream = new ByteArrayInputStream(svg.getBytes("UTF-8"));
			String outputType = this.getAttributeAsString(OUTPUT_FORMAT);
			if (outputType == null || outputType.trim().equals("")) {
				logger.debug("Output format not specified, default is " + OUTPUT_FORMAT_JPEG);
				outputType = OUTPUT_FORMAT_JPEG;
			}
			
			File exportFile = null;
			
			String ext = null;
			if (outputType.equalsIgnoreCase(OUTPUT_FORMAT_PNG)) {
				ext = ".png";
				exportFile = File.createTempFile("chart", ext);
				outputStream = new FileOutputStream(exportFile);
				transformSVGIntoPNG(inputStream, outputStream);
			} else if (outputType.equalsIgnoreCase(OUTPUT_FORMAT_JPEG)) {
				ext = ".jpg";
				exportFile = File.createTempFile("chart", ext);
				outputStream = new FileOutputStream(exportFile);
				transformSVGIntoJPEG(inputStream, outputStream);
			} else if (outputType.equalsIgnoreCase(OUTPUT_FORMAT_PDF)) {
				ext = ".pdf";
				exportFile = File.createTempFile("chart", ext);
				outputStream = new FileOutputStream(exportFile);
				transformSVGIntoPDF(inputStream, outputStream);
			} else if (outputType.equalsIgnoreCase(OUTPUT_FORMAT_SVG)) {
				ext = ".svg";
				exportFile = File.createTempFile("chart", ext);
				outputStream = new FileOutputStream(exportFile);
				writeSVG(inputStream, outputStream);
			} else {
				throw new SpagoBIEngineRuntimeException("Output format [" + outputType + "] not supperted");
			}
			
			String mimetype = MimeUtils.getMimeType(exportFile);
			
			try {
				writeBackToClient(exportFile, null, false, exportFile.getName(), mimetype);
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch (Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			} 
			logger.debug("OUT");
		}		

	}
    
	private void transformSVGIntoPDF(InputStream inputStream,
			OutputStream outputStream) throws IOException, DocumentException {
		FileOutputStream imageFileOutputStream = null;
		File imageFile = null;
		try {
			imageFile = File.createTempFile("chart", ".jpg");
			imageFileOutputStream = new FileOutputStream(imageFile);
			transformSVGIntoPNG(inputStream, imageFileOutputStream);
			
		    Document pdfDocument = new Document(PageSize.A4.rotate());
		    PdfWriter docWriter = PdfWriter.getInstance(pdfDocument, outputStream);
		    pdfDocument.open();
		    Image jpg = Image.getInstance(imageFile.getPath());
		    fitImage(jpg);
	    
		    pdfDocument.add(jpg);
		    pdfDocument.close();
		    docWriter.close();
		} finally {
			if (imageFileOutputStream != null) {
				try {
					imageFileOutputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (imageFile.exists()) {
				imageFile.delete();
			}
		}
	}
	
	/**
	 * Set the dimension of the image to fit the A4 page size
	 * The layout of the page should be horizontal 
	 * @param jpg the image to fit
	 */
	private void fitImage(Image jpg){
	    if(jpg.getWidth()>PageSize.A4.getHeight()){
	    	float imgScaledWidth = PageSize.A4.getHeight()-100;
	    	float imgScaledHeight = (imgScaledWidth/jpg.getWidth())*jpg.getHeight();
	    	jpg.scaleAbsolute(imgScaledWidth,imgScaledHeight);	
	    }	
	    if(jpg.getHeight()>PageSize.A4.getWidth()){
	    	float imgScaledHeight = PageSize.A4.getWidth()-100;
	    	float imgScaledWidth = (imgScaledHeight/jpg.getHeight())*jpg.getWidth();
	    	jpg.scaleAbsolute(imgScaledWidth,imgScaledHeight);	
	    }	
	}

	private void writeSVG(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte[] buf = new byte[1024];
		int b = -1;
		while((b = inputStream.read(buf)) != -1) {
			outputStream.write(buf, 0, b);
		}
	}

	public static void transformSVGIntoJPEG (InputStream inputStream,	OutputStream outputStream) {
		// create a JPEG transcoder
		JPEGTranscoder t = new JPEGTranscoder();
		
		// set the transcoding hints
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(1));
		t.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(1000));
		t.addTranscodingHint(JPEGTranscoder.KEY_ALLOWED_SCRIPT_TYPES, "*");
		t.addTranscodingHint(JPEGTranscoder.KEY_CONSTRAIN_SCRIPT_ORIGIN, new Boolean(true));
		t.addTranscodingHint(JPEGTranscoder.KEY_EXECUTE_ONLOAD, new Boolean(true));
		
		// create the transcoder input
		Reader reader = new InputStreamReader(inputStream);
		TranscoderInput input = new TranscoderInput(reader);
		
		// create the transcoder output
		TranscoderOutput output = new TranscoderOutput(outputStream);
		
		// save the image
		try {
			t.transcode(input, output);
		} catch (TranscoderException e) {
			logger.error("Impossible to convert svg to jpeg: " + e.getCause(), e);
			throw new SpagoBIEngineRuntimeException("Impossible to convert svg to jpeg: " + e.getCause(), e);
		}
	}
	
	public static void transformSVGIntoPNG (InputStream inputStream, OutputStream outputStream) {
		// create a PNG transcoder
		PNGTranscoder t = new PNGTranscoder();
		
		// set the transcoding hints
		t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(1000));
		t.addTranscodingHint(PNGTranscoder.KEY_ALLOWED_SCRIPT_TYPES, "*");
		t.addTranscodingHint(PNGTranscoder.KEY_CONSTRAIN_SCRIPT_ORIGIN, new Boolean(true));
		t.addTranscodingHint(PNGTranscoder.KEY_EXECUTE_ONLOAD, new Boolean(true));
		
		// create the transcoder input
		Reader reader = new InputStreamReader(inputStream);
		TranscoderInput input = new TranscoderInput(reader);
		
		// create the transcoder output
		TranscoderOutput output = new TranscoderOutput(outputStream);
		
		// save the image
		try {
			t.transcode(input, output);
		} catch (TranscoderException e) {
			logger.error("Impossible to convert svg to jpeg: " + e.getCause(), e);
			throw new SpagoBIEngineRuntimeException("Impossible to convert svg to jpeg: " + e.getCause(), e);
		}
	}

}
