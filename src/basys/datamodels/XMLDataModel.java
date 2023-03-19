/*
 * BBBBBB     AAAAA     SSSSS  YY      YY   SSSSS    22222    00000    00000   33333
 * BB   BB   AA   AA   SS       YY    YY   SS       22   22  00   00  00   00 33   33
 * BB   BB   AA   AA    SS       YY  YY     SS          22   00   00  00   00      33
 * BBBBBB    AA   AA     SS       YYYY       SS        22    00   00  00   00    333
 * BB   BB   AAAAAAA      SS       YY         SS      22     00   00  00   00      33
 * BB   BB   AA   AA       SS      YY          SS    22      00   00  00   00 33   33
 * BBBBBB    AA   AA   SSSSS       YY      SSSSS     222222   00000    00000   33333
 * 
 * http://www.basys2003.org
 *   
 * Copyright (c) 2003, 2004, BASys 2003 Project, 
 *                           Vienna University of Technology, 
 *                           Department of Automation - Automation Systems group,
 *                           Oliver Alt 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *	- Redistributions of source code must retain the above copyright notice, this list
 *    of conditions and the following disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 
 *  - Neither the name of the BASys 2003 Project nor the names of its contributors 
 *    may be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH 
 * DAMAGE.
 */
package basys.datamodels;

import java.util.Observable;
import java.util.Observer;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.apache.log4j.*;

/**
 * XMLDataModel.java
 * 
 * Abstract superclass for XML datamodels with the id/name/comment structure
 * 
 * @author 	oalt
 * @version	$Id: XMLDataModel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public abstract class XMLDataModel extends Observable implements Observer
{
	private static Logger logger=Logger.getLogger(XMLDataModel.class);
	
	private Document doc;
	
//	public abstract StandardDataModel getStandardDataModel(String id);
//	public abstract String getRootID();
//	public abstract Collection getChildIDs(String parentID);
//	public abstract String addChild(String parentID);	
	
	/**
	 * Constructor for XMLDataModel.
	 * Builds an emty DOM document.
	 */
	public XMLDataModel()
	{
		super();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	try 
    	{
      		DocumentBuilder builder = factory.newDocumentBuilder();
      		this.doc = builder.newDocument();
    	}
    	catch(ParserConfigurationException pce) 
    	{
     		// Parser with specified options can't be built
      		pce.printStackTrace();
    	}
	}
	
	/**
	 * Constructor for ArchitecturalDataModel.
	 */
	public XMLDataModel(Document doc)
	{
		super();
		this.doc=doc;
	}
	
	/**
	 * Returns the DOM document
	 */
	public Document getDocumnet()
	{
		return this.doc;
	}
	
	/**
	 * Set the DOM document
	 */
	public void setDocumnet(Document doc)
	{
		this.doc=doc;
	}
	
	/**
	 * @see java.util.Observer#update(Observable, Object)
	 */
	public void update(Observable o, Object arg)
	{
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Remove the data with the given ID.
	 */
	public void removeData(String id)
	{
		Node n=this.getDataRootNode(id);
		Node parent = n.getParentNode();
		parent.removeChild(n);
		this.setChanged();
		this.notifyObservers();	
	}
	
	/**
	 * Get name of the node
	 */
	public String getName(String id)
	{
		Node n=this.getDataRootNode(id);
		return this.readDOMNodeValue((Element)n, new StringTokenizer("name", "/"));
	}
	
	/**
	 * Set name for the given id
	 */
	public void setName(String id, String name)
	{
		Node n=this.getDataRootNode(id);
		this.writeDOMNodeValue(n, new StringTokenizer("name", "/"), name);
	}
	
	/**
	 * Get comment of the node
	 */
	public String getComment(String id)
	{
		Node n=this.getDataRootNode(id);
		return this.readDOMNodeValue((Element)n, new StringTokenizer("comment", "/"));
	}
	
	/**
	 * Set name for the given id
	 */
	public void setComment(String id, String comment)
	{
		Node n=this.getDataRootNode(id);
		this.writeDOMNodeValue(n, new StringTokenizer("comment", "/"), comment);
	}
	
	/**
	 * Return the id list for all child nodes with namen name from the given stat node
	 * 
	 * @param node the start node
	 * @param childname the child node name
	 * @return the id String collection
	 */
	protected Collection getIDList(Node node, String childname)
	{	
		Vector v=new Vector();
		
		NodeList nl=((Element)node).getElementsByTagName(childname);
		for(int i=0; i<nl.getLength(); i++)
		{
			Element e=(Element)nl.item(i);
			if(e.hasAttribute("id"))
			{
				v.add(e.getAttribute("id"));	
			}
		}
		
		return v;
	} 
	
	/**
	 * Create a new standard child data node. This node includes a name with id and a comment tag.
	 * 
	 * @param tagname the child root tag name
	 * @return the DOM Element
	 */
	protected Element createCild(String tagname)
	{
		Element e=doc.createElement(tagname);
		
		String id=tagname+"-"+this.getNewID(tagname); // get id for the child
		e.setAttribute("id", id);
		
		e.appendChild(doc.createElement("name"));
		e.appendChild(doc.createElement("comment"));	
		
		return e;	
	}
	
	/**
	 * @return the child id for the given child node
	 */
	protected String getChildID(Element childnode)
	{
		return childnode.getAttribute("id");
	}
	
	/**
	 * Reurns an ID number for a new child node
	 */
	private int getNewID(String tagname)
	{
		NodeList nl=this.doc.getElementsByTagName(tagname);
				
		// calculate maximum value
		int max=0;
		
		for(int i=0; i<nl.getLength(); i++)
		{
			Element e=(Element)nl.item(i);
			if(e.hasAttribute("id"))
			{
				String idstr=e.getAttribute("id");
				String prefix=tagname+"-";
				String number = idstr.replaceAll(prefix, "");
				if(Integer.parseInt(number)>max)
				{
					max=Integer.parseInt(number);	
				}	
			}	
		}
		
		return max + 1;	// return maximum + 1
	}
	
	/**
	 * Returns the root node with the given id
	 */
	public Node getDataRootNode(String id)
	{
		Node drNode=null;
		
		int end=id.indexOf('-');
		String nodename;
		if(end != -1)
		{
			nodename=id.substring(0, end);
		}
		else
		{
			nodename=id;	
		}	
		
		NodeList nl=this.doc.getElementsByTagName(nodename);
		if(nl!=null)
		{
			for(int i=0; i<nl.getLength(); i++)
			{
				Node n=nl.item(i);
				NamedNodeMap attr=n.getAttributes();
				if(attr!=null)
				{
					Node idNode=attr.getNamedItem("id");
					if(idNode!=null)
					{
						if(idNode.getNodeValue().equals(id))
						{
							drNode=n;
							break;	
						}	
					}		
				}
			} // for	
		} // if(null)
		//logger.info("returning data node, id="+id+" Nodename: "+nodename+" node: "+drNode);
		return drNode;
	}
	
	/**
	 * Writes the node value for the node given in the StringTokenizer path. If no such node exists it will created.
	 * All observers will notified abaut the change of the data model.
	 * @param n path root node
	 * @param path path to the node 
	 * @param value the value that will be written
	 */
	public void writeDOMNodeValue(Node n, StringTokenizer path, String value)
	{
		this.writeNodeValue(n, path, value);
		this.doc.normalize();
		this.setChanged();
		this.notifyObservers();	
	}
	
	/**
	 * Recursively DFS method for writing node values. If no such nod exists the method creates them.
	 */
	private void writeNodeValue(Node n, StringTokenizer path, String value)
	{	
		
		if(path.hasMoreTokens())
		{
			String next = path.nextToken();
            // logger.debug("next " + next);
            if(n.getChildNodes()==null)
            {
            	Node ne=n.appendChild(n.getOwnerDocument().createElement(next)); // build tag			
            	writeNodeValue(ne, path, value); 
            }
            else
            {
            	NodeList nl=n.getChildNodes();
            	boolean found=false;
            	
            	for(int i=0; i<nl.getLength(); i++)
            	{
            		if(nl.item(i).getNodeName().equals(next))
            		{
            			found=true;
            			writeNodeValue(nl.item(i), path, value);
            			break;
            		}	
            	}
            	if(!found)
            	{
            		Node nu=n.appendChild(n.getOwnerDocument().createElement(next));
            		writeNodeValue(nu, path, value);	
            	}
            }			
		}
		else // end of path
		{
			//logger.debug("End of path: "+n.toString());
			if(n.hasChildNodes())
			{
				
				// search text nodes
				if(n.getChildNodes().item(0).getNodeType()==Node.TEXT_NODE)
				{
					n.getChildNodes().item(0).setNodeValue(value);
					//logger.debug("Set text node value");	
					return;
				}
				else
				{
					n.appendChild(n.getOwnerDocument().createTextNode(value));	
					//logger.debug("Append text node value");
					return;
				}	
			}
			else
			{
				n.appendChild(n.getOwnerDocument().createTextNode(value));	
				//logger.debug("Append text node value 2 "+value);
				return;
			}	
		}
		
	}
	
	/**
	 * BFS method for reading node values
	 */
	public String readDOMNodeValue(Node e, StringTokenizer path)
	{
		Node actualVisited=e;
		while(path.hasMoreTokens())
        {
            String next = path.nextToken(); // get child nodes
            //logger.info("next " + next);
            NodeList nl = actualVisited.getChildNodes();
            if(nl.getLength() > 0)
            {

            	for(int cnt=0; cnt < nl.getLength(); cnt++)
            	{
            		if(nl.item(cnt).getNodeName().equals(next))
            		{
            			//logger.debug("found node "+nl.item(cnt).getNodeName());
						actualVisited=(Element)nl.item(cnt);
						break;	
            		}
            	}

            }
            else
            {
            	return "";	
            }
        }
        if(actualVisited.hasChildNodes())
        {
			if(actualVisited.getFirstChild().getNodeType() == Node.TEXT_NODE)
			{
		   		return actualVisited.getFirstChild().getNodeValue();
			}
        }
        else
        {
        	return "";
        }
        return "";
	}
	
	/**
	 * Set the ID.
	 */
	protected void setID(Node xmlNode, String id)
	{
		// set ID
		NamedNodeMap attrs=xmlNode.getAttributes();
		if(attrs!=null)
		{
			Node idNode=attrs.getNamedItem("id");
			if(idNode!=null)
			{
				idNode.setNodeValue((String)id);	
			}		
		}
		else
		{
			xmlNode.appendChild(xmlNode.getOwnerDocument().createAttribute("id"));
			xmlNode.getAttributes().getNamedItem("id").setNodeValue((String)id);	
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	
	
}
