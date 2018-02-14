package com.devluff.commons.markup;

import java.io.ByteArrayInputStream;
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
	
	private Element oLastElement;
	
	private int nDepth;
	
	private String strEncoding;
	
	public XmlParser() {
		this("UTF-8");
	}
	
	public XmlParser(String strEncoding) {
		this.strEncoding = strEncoding;
	}
	
	private void clearXmlData() {
		oRootElement = null;
		oCurrentElement = null;
		nCurrentPosition = 0;
		oParentElement = null;
		oLastElement = null;
		nDepth = 0;
	}
	
	public boolean setDoc(String strXmlContent) {
		clearXmlData();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			org.w3c.dom.Document w3cDocument = documentBuilder.parse(new ByteArrayInputStream(strXmlContent.getBytes()));
			oDocument = new DOMBuilder().build(w3cDocument);	
			//oRootElement = oDocument.getRootElement();
		}catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean loadXml(String strXmlPath) {
		clearXmlData();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			org.w3c.dom.Document w3cDocument = documentBuilder.parse(strXmlPath);
			oDocument = new DOMBuilder().build(w3cDocument);	
		}catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean findElement(String strElementName) {
		if(oDocument == null)
			return false;
		// first finding
		if(oRootElement == null) {
			Element oTempElement = oDocument.getRootElement();
			String strTempElementName = oTempElement.getName();
			if(strTempElementName.equals(strElementName)) {
				oRootElement = oTempElement;
				oCurrentElement = oTempElement;
				oLastElement = oTempElement;
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
				oLastElement = oTempCurrentElement;
				return true;
			}
		}
		return false;
	}
	
	public void resetPosition() {
		nCurrentPosition = 0;
	}
	
	public void setAttribute(String strAttributeName, String strAttributeValue) {
		if(oDocument == null)
			return;
		oLastElement.setAttribute(strAttributeName, strAttributeValue);
	}
	
	public String getAttribute(String strAttributeName) {
		if(oDocument == null)
			return "";
		Attribute oCurrentAttribute = oLastElement.getAttribute(strAttributeName);
		if(oCurrentAttribute == null)
			return "";
		String strCurrentAttribute = oCurrentAttribute.getValue();
		if(strCurrentAttribute != null)
			return strCurrentAttribute;
		return "";
	}
	
	public void intoToElement() {
		if(oDocument == null)
			return;
		nDepth+=1;
		oParentElement = oCurrentElement;
		oCurrentElement = null;
		oLastElement = null; 
		nCurrentPosition = 0;
	}
	
	public boolean outOfElement() {
		if(oDocument == null)
			return false;
		if(nDepth == 0) {
			return false;
		}
		nDepth-=1;
		nCurrentPosition = 0;
		oCurrentElement = oParentElement;
		oLastElement = oParentElement; 
		oParentElement = oCurrentElement.getParentElement();
		return true;
	}
	
	// 현재 가리키는 Element 하위 Node Element를 추가하고 가리킨다.
	public boolean addChildElement(String strElementName) {
		return addChildElement(strElementName, "");
	}
	
	public boolean addChildElement(String strElementName, String strContent) {
		// xml 데이터를 새로 만들 경우 loadXml 혹은 setDoc 은 호출되지 않으므로 document 객체부터 새로 생성하여 root element 를 생성한다.
		if(oDocument == null) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				String strTempXml = String.format("<%s/>", strElementName); 
				DocumentBuilder documentBuilder = factory.newDocumentBuilder();
				org.w3c.dom.Document w3cDocument = documentBuilder.parse(new ByteArrayInputStream(strTempXml.getBytes()));
				oDocument = new DOMBuilder().build(w3cDocument);
				oRootElement = oDocument.getRootElement();
				oCurrentElement = oRootElement;
				oLastElement = oRootElement;
				if(strContent != null && strContent.length() > 0) {
					oRootElement.setText("");
				}
				return true;
			}catch (Exception e) {
				logger.error(e.getMessage());
				return false;
			}
		}
		Element oChildElement = new Element(strElementName);
		if(strContent != null && strContent.length() > 0) {
			oChildElement.setText("");
		}
		oCurrentElement.addContent(oChildElement);
		oLastElement = oChildElement; 
		return true;
	}
	
	public boolean removeCurrentElement() {
		if(oDocument == null)
			return false;
		if(nDepth == 0) {
			return false;
		}
		nDepth-=1;
		Element oParentElement = oCurrentElement.getParentElement();
		oParentElement.removeContent(oCurrentElement);
		oCurrentElement = oParentElement;
		oLastElement = oParentElement; 
		nCurrentPosition = 0;
		return true;
	}
	
	public boolean removeChildElement(String strElementName) {
		if(oDocument == null)
			return false;
		if(oLastElement != null) {
			if(oLastElement.getName().equals(strElementName)) {
				oLastElement = oCurrentElement;
			}
		}
		oCurrentElement.removeChildren(strElementName);
		return true;
	}
	
	public boolean removeChildAllElement() {
		if(oDocument == null)
			return false;
		oLastElement = oCurrentElement;
		List<Element> listChildElement = oCurrentElement.getChildren();
		// 앞에 child 부터 지울 경우 뒤에 있던 데이터가 앞으로 앞당겨지면서 for 문을 정상적으로 수행 할 수 없다. 따라서 뒤에부터 지운다.
		for(int i = listChildElement.size() - 1; i >= 0; i--) {
			Element oChildElement = listChildElement.get(i);
			oCurrentElement.removeContent(oChildElement);
		}
		return true;
	}
	
	public String getContent() {
		if(oDocument == null)
			return "";
		return oLastElement.getText();
	}
	
	public void setContent(String strContent) {
		if(oDocument == null)
			return;
		oLastElement.setText(strContent);
	}
	
	public void setCDATAContent(String strContent) {
		if(oDocument == null)
			return;
		CDATA oTempCDATA = new CDATA(strContent);
		oLastElement.setContent(oTempCDATA);
	}
	
	public String getDoc() {
		if(oDocument == null)
			return "";
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
