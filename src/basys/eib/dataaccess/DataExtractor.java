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
package basys.eib.dataaccess;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.helpers.*;

import basys.eib.*;

import javax.xml.parsers.*;


/**
 * DataExtractor.java
 * 
 * @author 	oalt
 * @version	$Id: DataExtractor.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 */
public class DataExtractor
{
	private String filename;
	
	private Vector manufacturers=null;
	
	/**
	 * Constructor for DataExtractor.
	 */
	public DataExtractor(String filename)
	{
		super();
		this.filename=filename;
	}
	
	/**
	 * Parse XML file and return Vector of EIBProducts
	 */
	public Vector getEIBProducts()
	{
		Vector products=new Vector();
		
    	DefaultHandler handler = new EIBProductXMLHandler(products);

        // Use the default (non-validating) parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try
        {
			File file=new File(filename);
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file, handler);

        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        
        return products;	
	}
	
	public Vector getProgramIDsForProduct(String productID)
	{
		Vector v=new Vector();
		DefaultHandler handler = new ProductToProgramXMLHandler(productID, v);

        // Use the default (non-validating) parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try
        {
			File file=new File(filename);
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file, handler);

        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        
        return v;
	}
	
	public Vector getAplications(Vector pids)
	{
		Vector v=new Vector();
		DefaultHandler handler = new ApplicationXMLHandler(pids, v);

        // Use the default (non-validating) parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try
        {
			File file=new File(filename);
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file, handler);
			this.manufacturers=((ApplicationXMLHandler)handler).getManufacturers();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        
        return v;	
	}
	
	public void setApplicationMaskData(EIBApplication app)
	{
		DefaultHandler handler = new EIBMaskVersionXMLHandler(app);

		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try
		{
			File file=new File(filename);
			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(file, handler);

		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * Parses all com objects for the application with the given id.
	 * 
	 * @param appId the id of the application
	 * @return a Vector with the EIBCommunicationObjects
	 */
	public Vector getComObjectsForApplication(String appId)
	{
		Vector v=new Vector();
		DefaultHandler handler = new EIBCommunicationObjectXMLHandler(appId, v);

		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try
		{
			File file=new File(filename);
			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(file, handler);

		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		return v;
	}
	
	/**
	 * Search matching manufacturer name for given id.
	 * 
	 * @param manID the id to resolve the name for.
	 * @return the manufacturer name or an empty String if not found.
	 */
	public String getManufacturerName(String manID)
	{	
		if(this.manufacturers==null)
		{
			this.getAplications(new Vector());
		}
		
		for(Enumeration e=this.manufacturers.elements(); e.hasMoreElements(); )
		{
			EIBManufacturer m=(EIBManufacturer)e.nextElement();
			if(m.getID().equals(manID))
			{
				return m.getName();
			}
		}
		return "";
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Vector getNormalizedComObjects(Vector comObjects)
	{
		Vector cobjects=new Vector();
		
		Vector tmp=(Vector)comObjects.clone();
		
		for(int i=0; i<this.getComObjectCount(comObjects); i++)
		{
			int index=this.getLowestComObjectIndex(tmp);
			EIBCommunicationObject co=(EIBCommunicationObject)tmp.get(index);	// get CO with lowest number
			
			tmp.remove(index);
			
			EIBCommunicationObject newco=new EIBCommunicationObject();	// create cloned CO
			newco.setNmae(co.getName());
			newco.setType(co.getType());
			newco.setFunction(co.getFunction());
			newco.setNumber(co.getNumber());
			
			while(index<tmp.size())	// search another COs
			{
				EIBCommunicationObject c=(EIBCommunicationObject)tmp.get(index);
				if(c.getNumber().equals(newco.getNumber()))
				{
					newco.setFunction(newco.getFunction()+" * "+c.getFunction());
					tmp.remove(index);	
				}
				else
				{
					index++;
				}
			}
			
			cobjects.add(newco);	// add new CO to Vector
		}
		return cobjects;
	}
	
	/**
	 * Calculates the first index of the CO element with the lowest number
	 * 
	 * @param comObjects
	 * @return
	 */
	private int getLowestComObjectIndex(Vector comObjects)
	{
		Integer nr=null;
		int index=0;
		
		for(int cnt=0; cnt<comObjects.size(); cnt++)
		{
			EIBCommunicationObject co=(EIBCommunicationObject)comObjects.get(cnt);
			int n=Integer.parseInt(co.getNumber());
			if(nr==null)
			{
				nr=new Integer(n);
				index=cnt;		
			}
			else
			{
				if(n<nr.intValue())
				{
					nr=new Integer(n);
					index=cnt;
				}
			}
		}
		
		
		return index;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private int getComObjectCount(Vector comObjects)
	{
		Vector tmp=(Vector)comObjects.clone();
		
		int cocount=0;
		
		String nr="";
		
		while(tmp.size()>0)
		{
			if(nr.equals("")) 
			{
				// remove first element and store number
				EIBCommunicationObject co=(EIBCommunicationObject)tmp.get(0);	
				tmp.remove(0);
				nr=co.getNumber();
				cocount++;
			}
			else
			{
				int cnt=0;
				while(cnt<tmp.size())
				{
					// test rest if equal and remove the equal elements
					EIBCommunicationObject co2=(EIBCommunicationObject)tmp.get(cnt);
					if(co2.getNumber().equals(nr))
					{
						tmp.remove(cnt);
					}
					else
					{
						cnt++;
					}
					
				} // while
				
				nr="";
			
			} // if
		}
		
		return cocount;
		
	}

	public static void main(String[] args)
	{

		
		
	}
}
