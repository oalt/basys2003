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

import java.util.Enumeration;
import java.util.Vector;

import org.xml.sax.helpers.*;
import org.xml.sax.*;

import basys.eib.*;

/**
 * ApplicationXMLHandler.java
 * 
 * @author 	oalt
 * @version	$Id: ApplicationXMLHandler.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 */
public class ApplicationXMLHandler extends DefaultHandler
{
	
	private boolean isApp=false;
	private boolean isProgID=false;
	
	private boolean match=false;
	
	private boolean isMaskID=false;
	private boolean isName=false;
	private boolean isVersion=false;
	private boolean isDevType=false;
	private boolean isPEIType=false;
	private boolean isAddTsize=false;
	private boolean isAssTsize=false;
	private boolean isAssTadd=false;
	private boolean isCTsize=false;
	private boolean isCTadd=false;
	private boolean isManID=false;
	private boolean isEEPROM=false;
	private boolean isOrgManID=false;
	
	// get Manufacturer
	private boolean isMan=false;
	private boolean isManCode=false;
	private boolean isManName=false;
	
	private Vector pids;
	private Vector apps;
	private EIBApplication app;
	
	private EIBManufacturer manu;
	
	private String data="";
	
	private Vector manufacturers = new Vector();
	
	/**
	 * Constructor for ApplicationXMLHandler.
	 */
	public ApplicationXMLHandler(Vector pids, Vector apps)
	{
		super();
		this.pids=pids;
		this.apps=apps;
		
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
		data="";
        String eName = sName; // element name
        if ("".equals(eName))
        {
            eName = qName; // not namespaceAware
        }
		
		if(eName.equals("manufacturer"))
		{
			this.isMan=true;
			
			this.manu=new EIBManufacturer();
		}
        else if(eName.equals("MANUFACTURER_NAME"))
		{
			if(this.isMan)
			{
				this.isManName=true;
				this.data="";	
			}
		}
        else if(eName.equals("application_program"))
        {
        	this.isApp=true;
        }
        else if(eName.equals("PROGRAM_ID"))
        {
            if(this.isApp)
         	{   
            	this.isProgID=true;
         	}
        }
        else if(eName.equals("MASK_ID"))
        {
        	if(this.isApp)
        	{
            	this.isMaskID=true;
        	}
        }
        else if(eName.equals("PROGRAM_NAME"))
        {
        	if(this.isApp)
        	{
            	this.isName=true;
        	}
        }
        else if(eName.equals("PROGRAM_VERSION"))
        {
        	if(this.isApp)
        	{
            	this.isVersion=true;
        	}
        }
        else if(eName.equals("DEVICE_TYPE"))
        {
        	if(this.isApp)
        	{
            	this.isDevType=true;
        	}
        }
        else if(eName.equals("PEI_TYPE"))
        {
        	if(this.isApp)
        	{
            	this.isPEIType=true;
        	}
        }
        else if(eName.equals("ADDRESS_TAB_SIZE"))
        {
        	if(this.isApp)
        	{
            	this.isAddTsize=true;
        	}
        }
        else if(eName.equals("ASSOCTAB_ADDRESS"))
        {
        	if(this.isApp)
        	{
            	this.isAssTadd=true;
        	}
        }
        else if(eName.equals("ASSOCTAB_SIZE"))
        {
        	if(this.isApp)
        	{
            	this.isAssTsize=true;
        	}
        }
        else if(eName.equals("COMMSTAB_ADDRESS"))
        {
        	if(this.isApp)
        	{
            	this.isCTadd=true;
        	}
        }
        else if(eName.equals("COMMSTAB_SIZE"))
        {
        	if(this.isApp)
        	{
            	this.isCTsize=true;
        	}
        }
        else if(eName.equals("MANUFACTURER_ID"))
        {
        	this.data="";
        	if(this.isMan)
        	{
        		this.isManCode=true;
        	}
        	else if(this.isApp)
        	{
            	this.isManID=true;
        	}
        }
        else if(eName.equals("EEPROM_DATA"))
        {
        	if(this.isApp)
        	{
            	this.isEEPROM=true;
        	}
        }
        else if(eName.equals("ORIGINAL_MANUFACTURER_ID"))
        {
        	if(this.isApp)
        	{
            	this.isOrgManID=true;
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

		if(eName.equals("manufacturer"))
		{
			this.isMan=false;
			this.manufacturers.add(this.manu);
		}
        else if(eName.equals("MANUFACTURER_NAME"))
		{
			if(this.isMan)
			{
				this.isManName=false;
				this.manu.setName(this.data);
			}
		}
        else if(eName.equals("application_program"))
        {
        	this.isApp=false;
        	if(this.match)
        	{
        		this.apps.addElement(this.app);
        		
        	}
			this.match=false;
        }
        else if(eName.equals("PROGRAM_ID"))
        {
            if(this.isApp)
         	{   
            	this.isProgID=false;
         	}
        }
        else if(eName.equals("MASK_ID"))
        {
        	if(this.isApp)
        	{
            	this.isMaskID=false;
        	}
        }
        else if(eName.equals("PROGRAM_NAME"))
        {
        	if(this.isApp)
        	{
            	this.isName=false;
        	}
        }
        else if(eName.equals("PROGRAM_VERSION"))
        {
        	if(this.isApp)
        	{
            	this.isVersion=false;
        	}
        }
        else if(eName.equals("DEVICE_TYPE"))
        {
        	if(this.isApp)
        	{
            	this.isDevType=false;
        	}
        }
        else if(eName.equals("PEI_TYPE"))
        {
        	if(this.isApp)
        	{
            	this.isPEIType=false;
        	}
        }
        else if(eName.equals("ADDRESS_TAB_SIZE"))
        {
        	if(this.isApp)
        	{
            	this.isAddTsize=false;
        	}
        }
        else if(eName.equals("ASSOCTAB_ADDRESS"))
        {
        	if(this.isApp)
        	{
            	this.isAssTadd=false;
        	}
        }
        else if(eName.equals("ASSOCTAB_SIZE"))
        {
        	if(this.isApp)
        	{
            	this.isAssTsize=false;
        	}
        }
        else if(eName.equals("COMMSTAB_ADDRESS"))
        {
        	if(this.isApp)
        	{
            	this.isCTadd=false;
        	}
        }
        else if(eName.equals("COMMSTAB_SIZE"))
        {
        	if(this.isApp)
        	{
            	this.isCTsize=false;
        	}
        }
        else if(eName.equals("MANUFACTURER_ID"))
        {
        	if(this.isMan)
        	{
        		this.isManCode=false;
        		this.manu.setID(this.data);
        	}
        	else if(this.isApp)
        	{
            	this.isManID=false;
        	}
        }
        else if(eName.equals("EEPROM_DATA"))
        {
        	if(this.isApp)
        	{
            	this.isEEPROM=false;
        	}
        	if(this.match)
        	{
				this.app.setEEPROMData(this.data);
        		this.data="";
        	}
        }
        else if(eName.equals("ORIGINAL_MANUFACTURER_ID"))
        {
        	if(this.isApp)
        	{
            	this.isOrgManID=false;
        	}
        }
        else    
        {
            
        }        

    }

    public void characters(char buf[], int offset, int len) throws SAXException
    {
        String s = new String(buf, offset, len);
        if(this.isProgID)
        {
        	for(Enumeration e=this.pids.elements(); e.hasMoreElements(); )
        	{
	        	if(((String)e.nextElement()).equals(s))
	        	{
	        		match=true;
	        		this.app=new EIBApplication();
	        		this.app.setProgramID(s);	
	        		break;
	        	}
        	}
        }
        if(this.match)
        {
        	if(this.isMaskID)
        	{
        		this.app.setMaskID(s);	
        	} 
        	else if(this.isName)
        	{
        		this.app.setName(s);	
        	}
        	else if(this.isVersion)
        	{
        		this.app.setVersion(s);	
        	}
        	else if(this.isDevType)
        	{
        		this.app.setDeviceType(s);	
        	}
        	else if(this.isPEIType)
        	{
        		this.app.setPEIType(s);	
        	}
        	else if(this.isAddTsize)
        	{
        		this.app.setAddressTableSize(s);	
        	}
        	else if(this.isAssTadd)
        	{
        		this.app.setAssociationTableAddress(s);	
        	}
        	else if(this.isAssTsize)
        	{
        		this.app.setAssociationTableSize(s);	
        	}
        	else if(this.isCTadd)
        	{
        		this.app.setCommTableAddress(s);	
        	}
        	else if(this.isCTsize)
        	{
        		this.app.setCommTableSize(s);	
        	}
        	else if(this.isManID)
        	{
        		this.app.setManufacturerID(s);	
        	}
        	else if(this.isEEPROM)
        	{
        		this.data+=s;
        		//System.err.println(s);
        		//this.app.setEEPROMData(s);	
        	}
        	else if(this.isOrgManID)
        	{
        		this.app.setOriginalManufacturerID(s);
       	 	}
        }
        else if(this.isManCode)
        {
        	this.data+=s;
        }
        else if(this.isManName)
        {
        	this.data+=s;
        }
    }
	
	/**
	 * Returns the EIB Manufacturers (code and name)
	 * @return the EIBManufacturers vector
	 */
	public Vector getManufacturers()
	{
		return this.manufacturers;
	}
	
}
