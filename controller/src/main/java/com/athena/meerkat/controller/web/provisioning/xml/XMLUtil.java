/**
 * 
 */
package com.athena.meerkat.controller.web.provisioning.xml;

import java.io.File;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.athena.meerkat.controller.MeerkatConstants;

/**
 * xml utils
 * 
 * @author BongJin Kwon
 * 
 */
public abstract class XMLUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLUtil.class);
	private static final String UTF8_BOM = "\uFEFF";

	/**
	 * return attribute value
	 * 
	 * @param node
	 * @param attrName
	 * @return
	 */
	public static String getAttribute(Node node, String attrName) {
		NamedNodeMap nodeMap = node.getAttributes();

		if (nodeMap != null) {

			Node attrNode = nodeMap.getNamedItem(attrName);

			if (attrNode == null) {
				throw new XMLUtilException(attrName + " attribute is not found in "+ nodeToString(node));
			}

			return attrNode.getNodeValue();
		}

		throw new XMLUtilException("attribute map is not found in "+ nodeToString(node));
	}

	/**
	 * return attribute values
	 * 
	 * @param node
	 * @param attrNames
	 * @return
	 */
	public static String[] getAttributes(Node node, String... attrNames) {

		
		String[] attrs = new String[attrNames.length];

		NamedNodeMap nodeMap = node.getAttributes();

		if (nodeMap != null) {

			for (int i = 0; i < attrNames.length; i++) {

				Node attrNode = nodeMap.getNamedItem(attrNames[i]);

				if (attrNode == null) {
					throw new XMLUtilException(attrNames[i] + " attribute is not found in " + nodeToString(node));
				}

				attrs[i] = attrNode.getNodeValue();
			}

			return attrs;
		}

		throw new XMLUtilException("attributes map is not found in " + nodeToString(node));
	}

	/**
	 * return Node by xpath expression
	 * 
	 * @param item
	 *            The starting context (a node, for example).
	 * @param expression
	 *            The XPath expression.
	 * @return
	 */
	public static Node getNode(Object item, String expression) {

		Node node = null;
		XPath xPath = XPathFactory.newInstance().newXPath();

		try {
			node = (Node) xPath.compile(expression).evaluate(item, XPathConstants.NODE);

			if (node == null) {
				throw new XMLUtilException("node not found. xpath : " + expression);
			}

			return node;
		} catch (XPathExpressionException e) {
			throw new XMLUtilException(e);
		}
	}

	/**
	 * return NodeList by xpath expression
	 * @param item
	 *            The starting context (a node, for example).
	 * @param expression
	 *            The XPath expression.
	 * @return
	 */
	public static NodeList getNodeList(Object item, String expression) {

		NodeList nodeList = null;
		XPath xPath = XPathFactory.newInstance().newXPath();

		try {
			nodeList = (NodeList) xPath.compile(expression).evaluate(item, XPathConstants.NODESET);

			if (nodeList == null) {
				throw new XMLUtilException("node list not found. xpath : " + expression);
			}

			return nodeList;
		} catch (XPathExpressionException e) {
			throw new XMLUtilException(e);
		}
	}

	/**
	 * return string value by xpath expression.
	 * 
	 * @param item
	 *            The starting context (a node, for example).
	 * @param expression
	 *            The XPath expression.
	 * @return
	 */
	public static String getStringValue(Object item, String expression) {

		XPath xPath = XPathFactory.newInstance().newXPath();

		try {
			String value = xPath.compile(expression).evaluate(item);

			if (value == null) {
				throw new XMLUtilException("string value not found. xpath : " + expression);
			}

			return value;
		} catch (XPathExpressionException e) {
			throw new XMLUtilException(e);
		}
	}

	/**
	 * convert node to string.
	 * @param node
	 * @return
	 */
	public static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			//t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException e) {
			throw new XMLUtilException(e);
		}
		return sw.toString();
	}
	
	public static void writeToFile(Document doc, File file) {
		try {
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			
			/*
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSSerializer writer = impl.createLSSerializer();
            
            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", Boolean.TRUE); // Set this to true if the declaration is needed to be outputted.

            FileUtils.writeStringToFile(file, writer.writeToString(doc), MeerkatConstants.UTF8);
            */
		} catch (Exception e) {
			throw new XMLUtilException(e);
		}
	}
	
	/**
	 * check UTF BOM byte of string
	 * @param s
	 * @return
	 */
	public static boolean hasUTF8BOM(String s){
		return s.startsWith(UTF8_BOM);
	}
	
	/**
	 * remove UTF BOM byte.
	 * @param s
	 * @return
	 */
	public static String removeUTF8BOM(String s) {
		
        if (hasUTF8BOM(s)) {
            s = s.substring(1);
            System.out.println("bommmmmm");
        }
        return s;
    }

}
// end of XMLUtil.java