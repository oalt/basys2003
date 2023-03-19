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
package basys.client.eib;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import basys.client.ListEntry;
import basys.client.Project;
import basys.client.ui.dialogs.NewEndDeviceDialog;
import basys.datamodels.installation.InstallationModel;
import basys.datamodels.architectural.ArchitecturalDataModel;
import basys.datamodels.eib.EIBDevicesDataModel;

/**
 * EIBActorConfigurator.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBDeviceConfigurator.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EIBDeviceConfigurator
{
	private int deviceType;
	private Project p;
	private String installationLocationID;
	private String location;
	
	/**
	 * 
	 */
	public EIBDeviceConfigurator(int deviceType, String installationLocationID, String location, Project p)
	{
		super();
		this.deviceType=deviceType;
		this.installationLocationID=installationLocationID;
		this.location=location;
		this.p=p;
		
	}
	
	/**
	 * Search for already installed actuators with matching functions.
	 * 
	 * @return id list of found actuators.
	 */
	public Vector findActuatorsInProject()
	{
		Vector actuators=new Vector();
		
		InstallationModel imodel=this.p.getInstallationModel();
		ArchitecturalDataModel amodel=this.p.getArchitecturalDataModel();
		
		actuators=imodel.findMatchingDevices(this.location,
											 this.getRequiredDeviceFunctions(this.deviceType),
											 "actuator",
											 (Vector)amodel.getBusDeviceIDs(this.installationLocationID));
			
		return actuators;		
	}
	
	/**
	 * Search for new matching actuators
	 * 
	 * @return id list of found actuators
	 */
	public Vector findNewActuators()
	{
		Vector actuators=new Vector();
		
		EIBDevicesDataModel model=(EIBDevicesDataModel)this.p.getApplication().getBusDeviceDataModel("EIB");
		
		actuators=model.findMatchingDevices(this.location, 
											this.getRequiredDeviceFunctions(this.deviceType),
											"actuator");
		
		return actuators;
	}
	
	/**
	 * 
	 * @param requiredFunctions
	 * @return
	 */
	public Vector findSensorsInProject(String[] requiredFunctions)
	{
		Vector sensors=new Vector();
		
		InstallationModel imodel=this.p.getInstallationModel();
		ArchitecturalDataModel amodel=this.p.getArchitecturalDataModel();
		
		sensors=imodel.findMatchingDevices(this.location,
											requiredFunctions,
											 "sensor",
											 (Vector)amodel.getBusDeviceIDs(this.installationLocationID));
			
		return sensors;
	}
	
	/**
	 * 
	 * @param requiredFunctions
	 * @return
	 */
	public Vector findNewSensors(String enddeviceID)
	{
		
		String requiredFunctions[]=this.getRequiredFinctionsForSensor(enddeviceID);
		
		Vector sensors=new Vector();
		
		EIBDevicesDataModel model=(EIBDevicesDataModel)this.p.getApplication().getBusDeviceDataModel("EIB");
		
		sensors=model.findMatchingDevices(this.location, 
										  requiredFunctions,
										  "sensor");
		
		return sensors;
	}
	
	private String[] getRequiredFinctionsForSensor(String enddeviceID)
	{
		InstallationModel imodel=this.p.getInstallationModel();
		
		return imodel.getFunctionsForEnddevice(enddeviceID);
	}
	
	public Vector getFunctionGroupList(String busDevID)
	{
		Vector list=new Vector();
		InstallationModel imodel=this.p.getInstallationModel();
		
		Vector ids=this.getFunctionGroupIDs(busDevID);
		for(Enumeration e=ids.elements(); e.hasMoreElements(); )
		{
			String id=(String)e.nextElement();
			String name=imodel.readDOMNodeValue(imodel.getDataRootNode(id), new StringTokenizer("function/name", "/"));
			ListEntry le=new ListEntry(name, id);
			list.addElement(le);
		}
		return list;
		
	}
	
	public void	setEIBGroupAddresses(String funcGroupID)
	{
		InstallationModel imodel=this.p.getInstallationModel();
		
		imodel.setGroupAddresses(this.getRequiredDeviceFunctions(this.deviceType), funcGroupID);
	}
	
	
	
	public Vector getFunctionGroupIDs(String busDevID)
	{
		Vector actuators=new Vector();
		
		InstallationModel imodel=this.p.getInstallationModel();
		
		actuators=imodel.getMatchingFunctionGroups(busDevID, this.getRequiredDeviceFunctions(this.deviceType));
		
		return actuators;
	}
	
	public String getInstallationLocationID()
	{
		return this.installationLocationID;
	}
	
	/**
	 * Returns all required EIB device functions for control the device (lamp, valve,...) with the given id.
	 * @param deviceType the type of device to control (lamp, valve, ...)
	 * @return array of required functions
	 */
	private String[] getRequiredDeviceFunctions(int deviceType)
	{
		String[] type=new String[1];
		type[0]="";
		
		switch(deviceType)
		{
			case NewEndDeviceDialog.LAMP:
				type=new String[1];
				type[0]="switching";
			break;
			case NewEndDeviceDialog.DIMMABLE_LAMP:
				type=new String[3];
				type[0]="switching";
				type[1]="";
				type[2]="";
			break;
			case NewEndDeviceDialog.VALVE:
				type=new String[1];
				type[0]="value";
			break;
			
		}
		
		return type;
	}
}
