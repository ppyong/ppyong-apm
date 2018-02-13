package com.devluff.commons.markup;

import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlParser {
	
	private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);
	private Document oDocument;
	private Element oRootElement;
	
	private Element oCurrentElement;
	private int nCurrentPosition;			
	
	private Element oParentElement;
	
	private int nDepth;
	
	private String strEncoding;
	
	public XmlParser() {
		this("UTF-8");
	}
	
	public XmlParser(String strEncoding) {
		this.strEncoding = strEncoding;
	}
	
	public boolean loadXml(String strXmlPath) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			org.w3c.dom.Document w3cDocument = documentBuilder.parse(strXmlPath);
			oDocument = new DOMBuilder().build(w3cDocument);	
			//oRootElement = oDocument.getRootElement();
		}catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean findElement(String strElementName) {
		// first finding
		if(oRootElement == null) {
			Element oTempElement = oDocument.getRootElement();
			String strTempElementName = oTempElement.getName();
			if(strTempElementName.equals(strElementName)) {
				oRootElement = oTempElement;
				oCurrentElement = oTempElement;
				nCurrentPosition = 0;
				return true;
			}else {
				return false;
			}
		}
		
		List<Element> listChildrenElement = null;
		// first finding after intoToElement 
		if(oCurrentElement == null) {
			listChildrenElement = oParentElement.getChildren();
		}
		// other..
		else {
			listChildrenElement = oCurrentElement.getChildren();
		}
		
		for(int i = nCurrentPosition; i < listChildrenElement.size(); i++) {
			Element oTempCurrentElement = listChildrenElement.get(i);
			String strTempCurrentElementName = oTempCurrentElement.getName();
			if(strTempCurrentElementName.equals(strElementName)) {
				nCurrentPosition = i;
				oCurrentElement = oTempCurrentElement;
				return true;
			}
		}
		
		return false;
	}
	
	public void resetPosition() {
		nCurrentPosition = 0;
	}
	
	public void setAttribute(String strAttributeName, String strAttributeValue) {
		oCurrentElement.setAttribute(strAttributeName, strAttributeValue);
	}
	
	public String getAttribute(String strAttributeName) {
		Attribute oCurrentAttribute = oCurrentElement.getAttribute(strAttributeName);
		if(oCurrentAttribute == null)
			return "";
		
		String strCurrentAttribute = oCurrentAttribute.getValue();
		if(strCurrentAttribute != null)
			return strCurrentAttribute;
		
		return "";
	}
	
	public void intoToElement() {
		nDepth+=1;
		oParentElement = oCurrentElement;
		oCurrentElement = null;
		nCurrentPosition = 0;
	}
	
	public boolean outOfElement() {
		if(nDepth == 0) {
			return false;
		}
		nDepth-=1;
		oCurrentElement = oParentElement;
		nCurrentPosition = 0;
		oParentElement = oCurrentElement.getParentElement();
		return true;
	}
	
	public String getContent() {
		return oCurrentElement.getText();
	}
	
	public void setContent(String strContent) {
		oCurrentElement.setText(strContent);
	}
	
	public void setCDATAContent(String strContent) {
		CDATA oTempCDATA = new CDATA(strContent);
		oCurrentElement.setContent(oTempCDATA);
	}
	
	public String getDoc() {
		Element oTempRoot = null;
		if(oRootElement != null)
			oTempRoot = oRootElement;
		
		oTempRoot = oDocument.getRootElement();
		
		XMLOutputter oOutp = new XMLOutputter();
		
		Format oFm = Format.getPrettyFormat();
		oFm.setEncoding(strEncoding);
        oOutp.setFormat(oFm);
        
	    try {
	    		StringWriter sw = new StringWriter();
	    	 	oOutp.output(oTempRoot, sw);
	 	    StringBuffer oSb = sw.getBuffer();
	 	    return oSb.toString();
	    }catch (Exception e) {
	    		logger.error(e.getMessage());
	    		return "";
		}
	}
	
	public boolean saveToXML(String strDestPath) {
		Element oTempRoot = null;
		if(oRootElement != null)
			oTempRoot = oRootElement;
		
		oTempRoot = oDocument.getRootElement();
		
		XMLOutputter oOutp = new XMLOutputter();
		
		Format oFm = Format.getPrettyFormat();
		oFm.setEncoding(strEncoding);
        oOutp.setFormat(oFm);
        
        FileWriter oFw = null; 
	    try {
	    		oFw = new FileWriter(strDestPath);
	    	 	oOutp.output(oTempRoot, oFw);
	 	    return true;
	    }catch (Exception e) {
	    		logger.error(e.getMessage());
	    		return false;
		}
	}
}
