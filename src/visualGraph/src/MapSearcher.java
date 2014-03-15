package visualGraph.src;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Abstrakt superklass f�r alla s�kklasser. 
 * 
 * @author billing
 * @author denniso
 */
public abstract class MapSearcher {
	
	/**
	 * Laddar en XML-fil och returnerar ett DOM-dokument. 
	 * Se http://www.jdom.org/ f�r mer info. 
	 * @param srcFile XML-fil med kartan.
	 * @return DOM Document
	 * @throws IOException
	 * @throws JDOMException
	 * 
	 */
	public static Document loadXmlMap(File srcFile) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(srcFile);
		return doc;
	}
	
	/**
	 * Specifiserar kartan. 
	 * 
	 * Denna metod anropas alltid f�re s�kning och lagrar kartan som ett privat attribut. 
	 * Hur kartan representeras �r upp till er att specificera.
	 * @param map XML-fil med kartan.
	 *  
	 */
	abstract public void setMap(File map);
	
	/**
	 * Utf�r s�kning med bredden f�rst. 
	 * @param from Id f�r startnod
	 * @param to Id f�r m�lnod
	 * @return Str�ng som representerar v�gen fr�n start till m�l. 
	 *  
	 */
	abstract public String breadthFirst(String from,String to);
	
	/**
	 * Utf�r s�kning med djupet f�rst. 
	 * @param from Id f�r startnod
	 * @param to Id f�r m�lnod
	 * @return Str�ng som representerar v�gen fr�n start till m�l. 
	 *  
	 */
	abstract public String depthFirst(String from,String to);
	
	/**
	 * Utf�r s�kning med A*. 
	 * @param from Id f�r startnod
	 * @param to Id f�r m�lnod
	 * @param fastest Anger om "snabbaste" eller (om falsk) kortaste v�gen s�ks
	 * @return Str�ng som representerar v�gen fr�n start till m�l. 
	 *  
	 */
	abstract public String aStar(String from,String to,boolean fastest);
	
	/**
	 * Utf�r s�kning med Greedy Search. 
	 * @param from Id f�r startnod
	 * @param to Id f�r m�lnod
	 * @return Str�ng som representerar v�gen fr�n start till m�l. 
	 *  
	 */
	abstract public String greedySearch(String from,String to);
}
