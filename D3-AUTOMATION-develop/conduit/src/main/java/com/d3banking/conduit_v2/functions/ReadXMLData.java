package com.d3banking.conduit_v2.functions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXMLData extends ConstantFields{
	
	/**
	 * Get test data from XML file
	 * @throws ParserConfigurationException 
	 * @throws IOException
	 * @throws SAXException
	 */
	public List<String> getTestDataFor(
			String subElementTagName) throws ParserConfigurationException, SAXException, IOException
			{
				//String _filePath = getClass().getResource("/TestData.xml").toString().replace("file:/", "");
		        String _filePath = conduitDirPath + "TestData.xml";
				File fXmlFile = new File(_filePath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("testData");
				List<String> dataInXML = new ArrayList<String>();
				for (int n = 0; n < nList.getLength(); n++) {
					Node nNode = nList.item(n);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						dataInXML.add(eElement.getElementsByTagName(subElementTagName).item(0).getChildNodes().item(0).getNodeValue());
					}
				}
				return dataInXML;
			}
	
	/**
	 * Read data in XML file
	 * @param _fileName
	 * @param _parentElementTagName
	 * @param _subElementTagName
	 * @param _attTagName
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public List<String> getElementValueInConduitXMLFile(
			String fileName, 
			String parentElementTagName, 
			String subElementTagName, 
			String attTagName) throws ParserConfigurationException, SAXException, IOException 
			{
				String filePath = getClass().getResource("/" + conduitDir).toString().replace("file:/", "")+ "/" + fileName;
				File fXmlFile = new File(filePath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName(parentElementTagName);
				List<String> dataInXML = new ArrayList<String>();
				if (nList.getLength()==0){dataInXML.add("elementDoesNotExist");}
				for (int n = 0; n < nList.getLength(); n++) {
					   Node nNode = nList.item(n);
					   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					      Element eElement = (Element) nNode;
					      if (!attTagName.equals(""))
					      {  	  
					    	  if (eElement.getAttribute(attTagName).equals(""))
					    	  {
					    		  dataInXML.add("elementDoesNotExist");
					    	  }else{
					    		  dataInXML.add(eElement.getAttribute(attTagName).trim());
					    	  }
					      }
					      if (!subElementTagName.equals(""))
					      {
					  		try {
					  			int nodeList = eElement.getElementsByTagName(subElementTagName).getLength();
					  			for (int l=0; l<nodeList; l++)
					  			{
					  				eElement.getElementsByTagName(subElementTagName).item(l).getChildNodes().item(0);
							  		try {
							  			dataInXML.add(eElement.getElementsByTagName(subElementTagName).item(l).getChildNodes().item(0).getNodeValue().trim());
									} catch (NullPointerException e1) {
										dataInXML.add("EmptyValue");
								    }	
					  			}		
					  			if (nodeList==0)
					  			{
					  				dataInXML.add("elementDoesNotExist");
					  			}
							} catch (NullPointerException e1) {
								dataInXML.add("elementDoesNotExist");
						    }						
						  }
					   }
				}
				return dataInXML;
			}
	
	/**
	 * Read data in XML file
	 * @param _fileName
	 * @param _parentElementTagName
	 * @param _subElementTagName
	 * @param _attTagName
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public List<String> getElementValueInConduitXMLFile(
			String fileName, 
			String parentElementTagName, 
			String subElementTagName1, 
			String subElementTagName2, 
			String subElementTagName3, 
			String attTagName) throws ParserConfigurationException, SAXException, IOException
			{
				String filePath = getClass().getResource("/" + conduitDir).toString().replace("file:/", "")+ "/" + fileName;
				File fXmlFile = new File(filePath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName(parentElementTagName);
				List<String> dataInXML = new ArrayList<String>();
				if (nList.getLength()==0){dataInXML.add("elementDoesNotExist");}
				for (int n = 0; n < nList.getLength(); n++) {
					   Node nNode = nList.item(n);
					   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					      Element eElement = (Element) nNode;
					      NodeList nList2 = eElement.getElementsByTagName(subElementTagName1);
					      if (nList2.getLength()==0){dataInXML.add("elementDoesNotExist");}
					      for (int m = 0; m < nList2.getLength(); m++) {
					    	   Node nNode2 = nList2.item(m);
							   if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
							      Element eElement2 = (Element) nNode2;
							      NodeList nList3 = eElement2.getElementsByTagName(subElementTagName2);
							      if (nList3.getLength()==0)
						  			{
						  				dataInXML.add("elementDoesNotExist");
						  			}
							      for (int l = 0; l < nList3.getLength(); l++) {
							    	   Node nNode3 = nList3.item(l);
									   if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
									      Element eElement3 = (Element) nNode3;
									      if (!attTagName.equals(""))
									      {  	  
									    	  if (eElement3.getAttribute(attTagName).equals(""))
									    	  {
									    		  dataInXML.add("elementDoesNotExist");
									    	  }else{
									    		  dataInXML.add(eElement3.getAttribute(attTagName));
									    	  }
									      }
									      if (!subElementTagName3.equals(""))
									      {
									    	  
									    	  try {
										  			int nodeList = eElement.getElementsByTagName(subElementTagName3).getLength();
										  			for (int li=0; li<nodeList; li++)
										  			{
										  				eElement.getElementsByTagName(subElementTagName3).item(li).getChildNodes().item(0);
												  		try {
												  			dataInXML.add(eElement.getElementsByTagName(subElementTagName3).item(li).getChildNodes().item(0).getNodeValue().trim());
														} catch (NullPointerException e1) {
															dataInXML.add("EmptyValue");
													    }	
										  			}
										  			if (nodeList==0)
										  			{
										  				dataInXML.add("elementDoesNotExist");
										  			}
												} catch (NullPointerException e1) {
													dataInXML.add("elementDoesNotExist");
											    }				
										  }
									   }
							      }
							   }
					      }
					   }
				}
				return dataInXML;
			}
	
	/**
	 * Update element value in Conduit file
	 * @param fileName
	 * @param parentElementTagName
	 * @param subElementTagName
	 * @param attTagName
	 * @param value
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public void setElementValueInConduitXMLFile(String fileName, String parentElementTagName, String subElementTagName, String attTagName,
			  String value) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException
	  {
		    String filePath = getClass().getResource("/" + conduitDir).toString().replace("file:/", "")+ "/" + fileName;
	        File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName(parentElementTagName);
			for (int n = 0; n < nList.getLength(); n++) {
				   Node nNode = nList.item(n);
				   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				      Element eElement = (Element) nNode;
				      if (!attTagName.equals(""))
				      {
				    	  eElement.setAttribute(attTagName, value + (n+1));
				      }
				      if (!subElementTagName.equals(""))
				      {
				  		try {
				  			eElement.getElementsByTagName(subElementTagName).item(0).getChildNodes().item(0).setNodeValue(value +(n+1));
						} catch (NullPointerException e) {
						}
				      }
				   }
			}
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			Result dest = new StreamResult(new FileWriter (filePath));
			transformer.transform(source, dest);
	  }
	
	/**
	 * Update element value in Conduit file
	 * @param fileName
	 * @param parentElementTagName
	 * @param subElementTagName
	 * @param attTagName
	 * @param value
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public void setElementValueInConduitXMLFile(String fileName, String parentElementTagName, String subElementTagName1, String subElementTagName2, String attTagName,
			  String value) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException
	  {
		    String filePath = getClass().getResource("/" + conduitDir).toString().replace("file:/", "")+ "/" + fileName;
	        File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName(parentElementTagName);
			for (int n = 0; n < nList.getLength(); n++) {
				   Node nNode = nList.item(n);
				   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				      Element eElement = (Element) nNode; 
				      NodeList nList2 = eElement.getElementsByTagName(subElementTagName1);
					  for (int l = 0; l < nList2.getLength(); l++) {
						   Node nNode2 = nList2.item(l);
						   if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
						      Element eElement2 = (Element) nNode2;
						      if (!attTagName.equals(""))
						      {
						    	  eElement2.setAttribute(attTagName, value + (l+1));
						      }
						      if (!subElementTagName2.equals(""))
						      {
						  		try {
						  			eElement2.getElementsByTagName(subElementTagName2).item(0).getChildNodes().item(0).setNodeValue(value +(l+1));
								} catch (NullPointerException e) {
								}
						      }
						   }
					  }
				   }
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			Result dest = new StreamResult(new FileWriter (filePath));
			transformer.transform(source, dest);
	  }
	
	/**
	 * Update element value in Conduit file
	 * @param fileName
	 * @param parentElementTagName
	 * @param subElementTagName
	 * @param attTagName
	 * @param value
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public void setElementValueInConduitXMLFile(String fileName, String parentElementTagName, String subElementTagName1, String subElementTagName2, String attTagName,
			  String value, int index) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException
	  {
		    String filePath = getClass().getResource("/" + conduitDir).toString().replace("file:/", "")+ "/" + fileName;
	        File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName(parentElementTagName);
			for (int n = 0; n < nList.getLength(); n++) {
				   Node nNode = nList.item(n);
				   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				      Element eElement = (Element) nNode; 
					  NodeList nList2 = eElement.getElementsByTagName(subElementTagName1);
					  for (int l = 0; l < nList2.getLength(); l++) {
						   Node nNode2 = nList2.item(l);
						   if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
						      Element eElement2 = (Element) nNode2;
						      if (!attTagName.equals(""))
						      {
						    	  eElement2.setAttribute(attTagName, value + index);
						      }
						      if (!subElementTagName2.equals(""))
						      {
						  		try {
						  			eElement2.getElementsByTagName(subElementTagName2).item(0).getChildNodes().item(0).setNodeValue(value + index);
								} catch (NullPointerException e) {
								}
						      }
						   }
					  }
				   }
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			Result dest = new StreamResult(new FileWriter (filePath));
			transformer.transform(source, dest);
	  }
	
	/**
	 * Update element value in Conduit file
	 * @param fileName
	 * @param parentElementTagName
	 * @param subElementTagName
	 * @param attTagName
	 * @param value
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public void setElementValueInConduitXMLFile(String fileName, String parentElementTagName, String subElementTagName1, String subElementTagName2, String attTagName,
			  String value, int index, int parentElementTagNameIndex, int subElementTagName1Index, int subElementTagName2Index) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException
	  {
		    String filePath = getClass().getResource("/" + conduitDir).toString().replace("file:/", "")+ "/" + fileName;
	        File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName(parentElementTagName);

				   Node nNode = nList.item(parentElementTagNameIndex);
				   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				      Element eElement = (Element) nNode; 
					  NodeList nList2 = eElement.getElementsByTagName(subElementTagName1);
					  Node nNode2 = nList2.item(subElementTagName1Index);
					  if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
					      Element eElement2 = (Element) nNode2;
					      if (!attTagName.equals(""))
					      {
					    	  eElement2.setAttribute(attTagName, value + index);
					      }
					      if (!subElementTagName2.equals(""))
					      {
					  		try {
					  			eElement2.getElementsByTagName(subElementTagName2).item(subElementTagName2Index).getChildNodes().item(0).setNodeValue(value + index);
							} catch (NullPointerException e) {
							}
					      }
					  }
				   }

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			Result dest = new StreamResult(new FileWriter (filePath));
			transformer.transform(source, dest);
	  }
}
