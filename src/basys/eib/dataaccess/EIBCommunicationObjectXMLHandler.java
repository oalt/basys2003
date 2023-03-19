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

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * EIBCommunicationObjectXMLHandler.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBCommunicationObjectXMLHandler.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 * 
 */
public class EIBCommunicationObjectXMLHandler extends DefaultHandler
{
	private static Logger logger=Logger.getLogger(EIBCommunicationObjectXMLHandler.class);
	
	private String programId;
	private Vector comObjects;
	private EIBCommunicationObject co;
	
	private String data="";
	
	private boolean match=false;
	
	private boolean isComObject=false;
	private boolean isProgramId=false;
	private boolean isName=false;
	private boolean isFunction=false;
	private boolean isReadEnabled=false;
	private boolean isWriteEnabled=false;
	private boolean isComEnabled=false;
	private boolean isId=false;
	private boolean isType=false;
	private boolean isNumber=false;
	/**
	 * 
	 */
	public EIBCommunicationObjectXMLHandler(String programId, Vector comObjects)
	{
		super();
		this.programId=programId.trim();
		this.comObjects=comObjects;
		logger.debug(programId);
	}
	
	
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

        if(eName.equals("communication_object"))
        {
            this.isComObject=true;
        }
        else if(eName.equals("PROGRAM_ID"))
        {
        	if(this.isComObject)
        	{
            	this.isProgramId=true;
        	}
        }
        else if(eName.equals("OBJECT_NAME"))
        {
        	if(this.match)
        	{
            	this.isName=true;
        	}
        }
        else if(eName.equals("OBJECT_FUNCTION"))
        {
        	if(this.match)
        	{
            	this.isFunction=true;
        	}
        }
        else if(eName.equals("OBJECT_READENABLED"))
        {
        	if(this.match)
        	{
            	this.isReadEnabled=true;
        	}
        }
        else if(eName.equals("OBJECT_WRITEENABLED"))
        {
        	if(this.match)
        	{
            	this.isWriteEnabled=true;
        	}
        }
        else if(eName.equals("OBJECT_COMMENABLED"))
        {
        	if(this.match)
        	{
            	this.isComEnabled=true;
        	}
        }
        else if(eName.equals("OBJECT_ID"))
        {
        	if(this.match)
        	{
            	this.isId=true;
        	}
        }
        else if(eName.equals("OBJECT_NUMBER"))
        {
        	if(this.match)
        	{
            	this.isNumber=true;
        	}
        }
        else if(eName.equals("OBJECT_TYPE"))
        {
        	if(this.match)
        	{
            	this.isType=true;
        	}
        }
        else    
        {
            
        }

    }

    public void endElement(String namespaceURI, String sName, String qName) throws SAXException
    {
        data=data.trim();	// remove leading and trailing whitespace
        
        String eName = sName; // element name
        if ("".equals(eName))
            eName = qName; // not namespaceAware
        
        if ("".equals(eName))
        {
            eName = qName; // not namespaceAware
        }

        if(eName.equals("communication_object"))
        {
            this.isComObject=false;
            
            if(this.match)
            {
            	this.comObjects.add(this.co);
            	this.match=false;
            }
        }
        else if(eName.equals("PROGRAM_ID"))
        {
        	
        	if(this.isComObject)
        	{
            	//logger.debug("match !"+data);
            	this.isProgramId=false;
            	if(this.programId.equals(data))
            	{
            		//logger.debug("match !"+data);
            		this.match=true;
            		this.co=new EIBCommunicationObject();
            		this.co.setId(this.data);
            	}
        	}
        }
        else if(eName.equals("OBJECT_NAME"))
        {
        	if(this.match)
        	{
            	this.isName=false;
            	this.co.setNmae(data);
        	}
        }
        else if(eName.equals("OBJECT_FUNCTION"))
        {
        	if(this.match)
        	{
            	this.isFunction=false;
            	this.co.setFunction(data);
        	}
        }
        else if(eName.equals("OBJECT_READENABLED"))
        {
        	if(this.match)
        	{
            	this.isReadEnabled=false;
            	this.co.setReadEnabled(data);
        	}
        }
        else if(eName.equals("OBJECT_WRITEENABLED"))
        {
        	if(this.match)
        	{
            	this.isWriteEnabled=false;
            	this.co.setWriteEnabled(data);
        	}
        }
        else if(eName.equals("OBJECT_COMMENABLED"))
        {
        	if(this.match)
        	{
            	this.isComEnabled=false;
            	this.co.setComEnabled(data);
        	}
        }
        else if(eName.equals("OBJECT_ID"))
        {
        	if(this.match)
        	{
            	this.isId=false;
            	this.co.setId(data);
        	}
        }
        else if(eName.equals("OBJECT_NUMBER"))
        {
        	if(this.match)
        	{
            	this.isNumber=false;
            	this.co.setNumber(data);
        	}
        }
        else if(eName.equals("OBJECT_TYPE"))
        {
        	if(this.match)
        	{
            	this.isType=false;
            	this.co.setType(data);
        	}
        }
        else    
        {
            
        }
		this.data="";
    }

    public void characters(char buf[], int offset, int len) throws SAXException
    {
        String s = new String(buf, offset, len);
        if(this.isComObject)
        {
        	data+=s;
        }
    }
}
