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
package basys.datamodels.eib;

import java.util.Collection;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import basys.datamodels.XMLDataModel;
import basys.eib.EIBApplication;
import basys.eib.EIBDeviceFunction;

import org.w3c.dom.Document;
import org.w3c.dom.*;

/**
 * EIBDevicesDataModel.java
 * 
 * XML model for eib device descriptions
 * 
 * @author	oalt
 * @version $Id: EIBDevicesDataModel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EIBDevicesDataModel extends XMLDataModel
{
	
	private Node devicesNode=null;
	
	/**
	 * 
	 */
	public EIBDevicesDataModel()
	{
		super();
		Document document=this.getDocumnet();
		this.devicesNode=document.appendChild(document.createElement("eib-devices"));
		
	}

	/**
	 * @param doc DOM model of this datamodel
	 */
	public EIBDevicesDataModel(Document doc)
	{
		super(doc);
		this.devicesNode=(Node)doc.getElementsByTagName("eib-devices").item(0);
	}
	
	/**
	 * Add new empty device node
	 * @param name
	 * @return id of the new device node
	 */
	public String addDevice(String name)
	{
		Element e=this.createCild("eibdevice");
		Node device=this.devicesNode.appendChild(e);
		this.setName(this.getChildID(e), name);
		
		device.appendChild(device.getOwnerDocument().createElement("functions"));
		
		this.setChanged();
		this.notifyObservers();
		return this.getChildID(e);
	}
	
	/**
	 * Updates the application node in the model
	 * @param deviceID
	 * @param app
	 */
	public void setApplication(String deviceID, EIBApplication app)
	{
		Node n=this.getDataRootNode(deviceID);
		NodeList nl=((Element)n).getElementsByTagName("application");
		if(nl.getLength()!=0)
		{
			n.removeChild(nl.item(0));
		}
		n.appendChild(app.getDOMElement(n.getOwnerDocument()));
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Returns the application for the by id given device
	 * @param deviceId the device id
	 * @return the application 
	 */
	public EIBApplication getApplication(String deviceId)
	{
		EIBApplication app=new EIBApplication();
		
		Node n=this.getDataRootNode(deviceId);
		
		app.setMaskVersion(this.readDOMNodeValue(n, new StringTokenizer("application/mask-version","/")));
		app.setMaskVersionName(this.readDOMNodeValue(n, new StringTokenizer("application/mask-version-name","/")));
		app.setName(this.readDOMNodeValue(n, new StringTokenizer("application/name","/")));
		app.setVersion(this.readDOMNodeValue(n, new StringTokenizer("application/version","/")));
		app.setDeviceType(this.readDOMNodeValue(n, new StringTokenizer("application/device-type","/")));
		app.setPEIType(this.readDOMNodeValue(n, new StringTokenizer("application/pei-type","/")));
		app.setAddressTableAddress(this.readDOMNodeValue(n, new StringTokenizer("application/addr-table-address","/")));
		app.setAddressTableSize(this.readDOMNodeValue(n, new StringTokenizer("application/addr-table-size","/")));
		app.setAssociationTableAddress(this.readDOMNodeValue(n, new StringTokenizer("application/association-table-address","/")));
		app.setAssociationTableSize(this.readDOMNodeValue(n, new StringTokenizer("application/association-table-size","/")));
		app.setAssociationTabPointer(this.readDOMNodeValue(n, new StringTokenizer("application/association-table-pointer","/")));
		app.setCommTableAddress(this.readDOMNodeValue(n, new StringTokenizer("application/communication-table-address","/")));
		app.setCommTableSize(this.readDOMNodeValue(n, new StringTokenizer("application/communication-table-size","/")));
		app.setComTabPointer(this.readDOMNodeValue(n, new StringTokenizer("application/communication-table-pointer","/")));
		app.setManufacturerID(this.readDOMNodeValue(n, new StringTokenizer("application/manufacturer","/")));
		app.setOriginalManufacturerID(this.readDOMNodeValue(n, new StringTokenizer("application/original-manufacturer","/")));
		app.setEEPROMData(this.readDOMNodeValue(n, new StringTokenizer("application/eeprom-data","/")));
		app.setUserEEPROMStart(this.readDOMNodeValue(n, new StringTokenizer("application/eeprom-start","/")));
		app.setUserEEPROMEnd(this.readDOMNodeValue(n, new StringTokenizer("application/eeprom-end","/")));
		app.setUserRAMStart(this.readDOMNodeValue(n, new StringTokenizer("application/uram-start","/")));
		app.setUserRAMEnd(this.readDOMNodeValue(n, new StringTokenizer("application/uram-end","/")));
		app.setRunErrorAddress(this.readDOMNodeValue(n, new StringTokenizer("application/run-error-address","/")));
		app.setManufacturerDataAddress(this.readDOMNodeValue(n, new StringTokenizer("application/manufacturer-data-address","/")));
		app.setManufacturerDataSize(this.readDOMNodeValue(n, new StringTokenizer("application/manufacturer-data-size","/")));
		app.setManufacturerIdAddress(this.readDOMNodeValue(n, new StringTokenizer("application/manufacturer-id-address","/")));
		app.setManIDProtected(this.readDOMNodeValue(n, new StringTokenizer("application/manufacturer-id-protected","/")));
		app.setRouteConterAddress(this.readDOMNodeValue(n, new StringTokenizer("application/route-counter-address","/")));
		
		return app;
	}
	
	
	/**
	 * Set the device type (sensor, actuator, passive)
	 * @param id eib-device id
	 * @param value the type value
	 */
	public void setDeviceType(String id, String value)
	{
		Node n=this.getDataRootNode(id);
		this.writeDOMNodeValue(n, new StringTokenizer("type", "/"), value);
	}	
	
	/**
	 * Returns the device type
	 * @param id
	 * @return the device type value
	 */
	public String getDeviceType(String id)
	{
		Node n=this.getDataRootNode(id);
		return this.readDOMNodeValue((Element)n, new StringTokenizer("type", "/"));
	} 
	
	/**
	 * Set the installation type (REG, UP, KE)
	 * @param id eib-device id
	 * @param value the type value
	 */
	public void setInstallationType(String id, String value)
	{
		Node n=this.getDataRootNode(id);
		this.writeDOMNodeValue(n, new StringTokenizer("installation-type", "/"), value);
	}	

	/**
	 * Returns the installation type.
	 * 
	 * @param id
	 * @return the device type value
	 */
	public String getInstallationType(String id)
	{
		Node n=this.getDataRootNode(id);
		return this.readDOMNodeValue((Element)n, new StringTokenizer("installation-type", "/"));
	}
	
	/**
	 * Add device function entry to eib debice with the given id.
	 * 
	 * @param funcgrpID the id of the eib device 
	 * @param func the device function to add
	 */
	public void addFunction(String funcgrpID, EIBDeviceFunction func)
	{
		Element e=this.createCild("function");
		
		Node n=this.getDataRootNode(funcgrpID);
		n.appendChild(e);
		
		String id=this.getChildID(e);
		
		this.setName(id, func.getName());

		this.writeDOMNodeValue(e, new StringTokenizer("type","/"), func.getTyoe());
		this.writeDOMNodeValue(e, new StringTokenizer("type","/"), func.getTyoe());
		this.writeDOMNodeValue(e, new StringTokenizer("com-object","/"), ""+func.getComObject());
		this.writeDOMNodeValue(e, new StringTokenizer("function-name","/"), ""+func.getFunctionName());
		this.writeDOMNodeValue(e, new StringTokenizer("eis-type","/"), ""+func.getEisType());
		
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Returns id list of device functions, included in the function group with the given id.
	 * 
	 * @param functionGroupID the function group id.
	 * @return the function IDs.
	 */
	public Collection getFunctionIDs(String functionGroupID)
	{
		return this.getIDList(this.getDataRootNode(functionGroupID), "function");
	}
	
	/**
	 * Add new function group to the device with the given id.
	 * 
	 * @param deviceID the device id.
	 * @return the id of the new function group.
	 */
	public String addFunctionGroup(String deviceID)
	{
		Element e=this.createCild("functiongroup");
		
		Node n=this.getDataRootNode(deviceID);
		NodeList functions=((Element)n).getElementsByTagName("functions");
		functions.item(0).appendChild(e);
	
		this.setChanged();
		this.notifyObservers();
		
		return this.getChildID(e);
				
	}
	
	/**
	 * Returns id list of the function groups for the given device.
	 *  
	 * @param deviceID the device id.
	 * @return the function group id list.
	 */
	public Collection getFunctionGroupIDs(String deviceID)
	{
		return this.getIDList(this.getDataRootNode(deviceID), "functiongroup");
	}
	
	/**
	 * Set the manufacturer name
	 * @param deviceID the id of the eib device
	 * @param name the manufacturer name
	 */
	public void setManufacturer(String deviceID, String name)
	{
		Node n=this.getDataRootNode(deviceID);
		this.writeDOMNodeValue(n, new StringTokenizer("manufacturer", "/"), name);
	}
	
	/**
	 * Get the manufacturer name for the device with the given id.
	 * @param deviceID the eib device id
	 * @return the manufacturer name
	 */
	public String getManufacturerName(String deviceID)
	{
		Node n=this.getDataRootNode(deviceID);
		String s=this.readDOMNodeValue((Element)n, new StringTokenizer("manufacturer", "/"));
		//System.err.println(s);
		return s;
	}
	
	
	
	/**
	 * Search for devices with the given parameters
	 * 
	 * @param installationType the installation type (REG, UP,...)
	 * @param functions required device functions
	 * @param deviceType sensor or actuator
	 * @return id list of matching devices or empty Vector if no match
	 */
	public Vector findMatchingDevices(String installationType, String[] functions, String deviceType)
	{
		Vector devices=new Vector();
		
		Vector deviceIDs=(Vector)this.getDeviceIDs();
		
		for(Enumeration e=deviceIDs.elements(); e.hasMoreElements(); )
		{
			String id=(String)e.nextElement();
			if(this.getInstallationType(id).equals(installationType))
			{
				if(this.getDeviceType(id).equals(deviceType))
				{
					Vector functiongroups=(Vector)this.getFunctionGroupIDs(id);
					while(!functiongroups.isEmpty())
					{
						String groupid=(String)functiongroups.remove(0);
						Vector funcids=(Vector)this.getFunctionIDs(groupid);
						
						boolean isIncluded=true;
						
						for(int cnt=0; cnt<functions.length; cnt++)
						{
							boolean match=false;
							for(int i=0; i<funcids.size(); i++)
							{
								Node n=(Node)this.getDataRootNode((String)funcids.elementAt(i));
								String ftype=this.readDOMNodeValue(n, new StringTokenizer("type", "/"));
								if(ftype.equals(functions[cnt]))
								{
									match=true;
									break;
								}
							}
							
							isIncluded &= match;
							
							if(!match)
							{
								break;
							}
						} // for(cnt)
						
						if(isIncluded)
						{
							devices.add(id);
							break;
						}
					} // while
		
				} // if
		
			} //if
			
		} // for(devices)
		
		return devices;
	}
	
	/**
	 * Returns the size of devices in the data model.
	 * 
	 * @return the device size.
	 */
	public int getDeviceCount()
	{
		return (this.getIDList(this.getDocumnet().getDocumentElement(), "eibdevice")).size();
	}
	
	/**
	 * Returns the ids of all included devices.
	 * 
	 * @return the device ids.
	 */
	public Collection getDeviceIDs()
	{
		return this.getIDList(this.getDocumnet().getDocumentElement(), "eibdevice");
	}
	
}