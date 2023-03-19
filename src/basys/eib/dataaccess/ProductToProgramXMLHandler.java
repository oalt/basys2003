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

import java.util.Vector;

import org.xml.sax.helpers.*;
import org.xml.sax.*;

/**
 * ProductToProgramXMLHandler.java
 * 
 * @author 	oalt
 * @version	$Id: ProductToProgramXMLHandler.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 */
public class ProductToProgramXMLHandler extends DefaultHandler
{
	
	private String productID;
	private Vector programIDs;
	
	private String programIDtmp="";
	
	private boolean isP2p=false;
	private boolean isProductID=false;
	private boolean isProgramID=false;
	private boolean match=false;
	/**
	 * Constructor for ProductToProgramXMLHandler.
	 */
	public ProductToProgramXMLHandler(String productID, Vector programIDs)
	{
		super();
		this.productID=productID;
		this.programIDs=programIDs;
	}
	
	//===========================================================
    // SAX DocumentHandler methods
    //===========================================================


    public void startDocument() throws SAXException
    {
        //logger.info("Starte Einlesen der Cross Referenz.");
    }

    public void endDocument() throws SAXException
    {
    }

    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException
    {

        String eName = sName; // element name
        if ("".equals(eName))
        {
            eName = qName; // not namespaceAware
        }

        if(eName.equals("product_to_program"))
        {
            this.isP2p=true;
        }
        else if(eName.equals("PRODUCT_ID"))
        {
            if(this.isP2p)
         	{   
            	this.isProductID=true;
         	}
        }
        else if(eName.equals("PROGRAM_ID"))
        {
        	if(this.isP2p)
        	{
            	this.isProgramID=true;
        	}
        }
        else    
        {
            
        }

    }

    public void endElement(String namespaceURI, String sName, String qName) throws SAXException
    {
        String eName = sName; // element name
        if ("".equals(eName))
            eName = qName; // not namespaceAware
        
        if ("".equals(eName))
        {
            eName = qName; // not namespaceAware
        }

        if(eName.equals("product_to_program"))
        {
            this.isP2p=false;
            if(this.match)
            {
            	if(!this.programIDtmp.equals(""))
            	{
            		this.programIDs.addElement(this.programIDtmp);
            	}
            	match=false;	
            }
        }
        else if(eName.equals("PRODUCT_ID"))
        {
            if(this.isP2p)
         	{   
            	this.isProductID=false;
         	}
        }
        else if(eName.equals("PROGRAM_ID"))
        {
        	if(this.isP2p)
        	{
            	this.isProgramID=false;
        	}
        }
        else    
        {
            
        }

    }

    public void characters(char buf[], int offset, int len) throws SAXException
    {
        String s = new String(buf, offset, len);
        if(this.isProductID)
        {
        	if(this.productID.equals(s))
        	{
        		match=true;	
        	}
        }
        if(this.isProgramID)
        {
        	if(this.match)
        	{
        		this.programIDtmp=s;	
        	}	
        }
    }

}
