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
package basys.client.commands;

import basys.LocaleResourceBundle;
import basys.client.ListEntry;
import basys.client.Project;
import basys.client.eib.EIBDeviceConfigurator;
import basys.client.ui.dialogs.ListSelectorDialog;
import basys.client.ui.dialogs.NewEndDeviceDialog;
import basys.datamodels.installation.InstallationModel;
import basys.datamodels.architectural.ArchitecturalDataModel;
import basys.datamodels.eib.EIBDevicesDataModel;
import basys.eib.EIBPhaddress;
import basys.eib.exceptions.EIBAddressFormatException;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

/**
 * AddDeviceToRoomCommand.java
 * 
 * 
 * @author	oalt
 * @version $Id: AddDeviceToRoomCommand.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class AddDeviceToRoomCommand extends Command
{
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	private static Logger logger=Logger.getLogger(AddDeviceToRoomCommand.class);
	
	private static final boolean DEBUG=true;
	
	private Project project;
	private String roomID;
	private int devType;
	private Point location;
	
	
	/**
	 * @param c
	 */
	public AddDeviceToRoomCommand(Component c)
	{
		super(c);
	}

	public AddDeviceToRoomCommand(Component c, Project project, String roomID, String devType,  Point location)
	{
		this(c);
		this.project=project;
		this.roomID=roomID;
		this.devType=Integer.parseInt(devType);
		this.location=location;
		
	}
	
	/**
	 * @see basys.client.commands.Command#execute()
	 */
	public boolean execute()
	{	
		NewEndDeviceDialog dialog=new NewEndDeviceDialog(new Frame(), project.getPrefferedBusSystem(), this.devType);
		int exitValue=dialog.showDialog();
		if(exitValue==0)
		{
			return false;
		}
		else
		{
			String bussystem=project.getPrefferedBusSystem();
			if(bussystem.equals("none"))
			{
				bussystem=dialog.getBussystem();
			}
			if(bussystem.equals("EIB"))
			{
				
				EIBDeviceConfigurator finder;
				
				EIBDevicesDataModel devmodel=(EIBDevicesDataModel)this.project.getApplication().getBusDeviceDataModel(this.project.getPrefferedBusSystem());
				
				ArchitecturalDataModel amodel=this.project.getArchitecturalDataModel();
				
				String installationLocationID=this.roomID;
				
				if(dialog.getInstallationLocation().equals("REG"))
				{
					// Let user choose junction box
					Vector jboxids=amodel.getJunctionBoxList();
					
					if(jboxids.isEmpty())
					{
						JOptionPane.showMessageDialog(null, locale.getString("mess.noJunctionBoxesFonund"));
						return false;
					}
					else
					{
						if(jboxids.size()>1 && jboxids.size() !=0)
						{
							ListSelectorDialog lsd=new ListSelectorDialog(new Frame(), locale.getString("tit.selection"), 
																		  jboxids);
							lsd.setVisible(true);
							if(lsd.getSelection()==null)
							{
								return false;
							}
							else 
							{		
								installationLocationID=((ListEntry)lsd.getSelection()).getValue();
							}
						}
						else
						{
							installationLocationID=((ListEntry)jboxids.firstElement()).getValue();
						}
					}
					
				}
				
				
				
				finder=new EIBDeviceConfigurator(this.devType, installationLocationID, 
											 dialog.getInstallationLocation(), 
											 this.project);
				
				InstallationModel imodel=this.project.getInstallationModel();
				
				// add device to data model
				String devID=imodel.addPassiveComponent();
				
				imodel.setName(devID, dialog.getName());
				//imodel.setProperty(devID, "actor-type", "");
				imodel.setProperty(devID, "bussystem", dialog.getBussystem());
				imodel.setProperty(devID, "actor-installation-location", dialog.getInstallationLocation());
				imodel.setProperty(devID, "type", dialog.getDeviceType()+"");
				
				
				boolean takeNew=true;
				
				// Search for already installed actuators
				Vector alreadyActors=finder.findActuatorsInProject();
				if(!alreadyActors.isEmpty())
				{
					int option=JOptionPane.showConfirmDialog(null, 
															 locale.getString("mess.devInProjectFound"), 
															 "",
												  			 JOptionPane.YES_NO_OPTION);
					
					// Take installed actuator ?
					if(option==JOptionPane.YES_OPTION)
					{
						takeNew=false;
					}
					else
					{
						takeNew=true;
					}
				}
				
				String actuatorID="";
				// Take new actuator
				if(takeNew)
				{
					actuatorID=this.addNewDevice(finder, dialog);
				}
				else
				{ // Take already installed
					actuatorID=this.selectAlreadyInstalledActuator(alreadyActors);
				}
				
				if(actuatorID==null)
				{
					return false;
				}
				
				// Select function group
				Vector functionList=(Vector)finder.getFunctionGroupList(actuatorID);
				
				ListEntry leFuncGroup;
				
				if(functionList.size()>1 && functionList.size()!=0)
				{
				
					ListSelectorDialog lsd3=new ListSelectorDialog(new Frame(), locale.getString("tit.selDevice"),
																   functionList );
					lsd3.setVisible(true);
					if(lsd3.getSelection()==null) // cancel selected
					{
						return false;
					}
					
					leFuncGroup=(ListEntry)lsd3.getSelection();
				}
				else
				{
					leFuncGroup=(ListEntry)functionList.firstElement();
				}
				
				
				
				
				// Set function group name
				imodel.setName(leFuncGroup.getValue(), leFuncGroup.getDisplayedName());
				
				// calculate group address
				finder.setEIBGroupAddresses(leFuncGroup.getValue());
				
				// virtualy connect installation device with bus device
				imodel.addConnection(devID, leFuncGroup.getValue());
				
				// add bus device reference in architectural model		
				amodel.addEndDevice(this.roomID, devID, this.location, dialog.getDeviceType());
				
				String state=imodel.getPropertyByName(actuatorID, "device-state");
				
				if(state.equals("ready"))
				{
					imodel.setProperty(actuatorID, "device-state", "addressed");
				}
				
				return true;
				
			}
			else
			{
				JOptionPane.showMessageDialog(null, locale.getString("mess.busNotSupported"));
				return false;				
			}
				
		}
	}



	/**
	 * 
	 * @param actuatorIDs
	 * @return
	 */
	private String selectAlreadyInstalledActuator(Vector actuatorIDs)
	{
		InstallationModel imodel=this.project.getInstallationModel();
		
		Vector actuatorlist=new Vector();
		for(Enumeration e=actuatorIDs.elements(); e.hasMoreElements(); )
		{
			String id=(String)e.nextElement();
			
			if(DEBUG)
			{		
				//logger.debug(devmodel.getName(id));
			}
			
			String text=imodel.getName(id)+" - "+imodel.getPropertyByName(id, "device-name");
			ListEntry le=new ListEntry(text, id);
			actuatorlist.addElement(le);	
		}
		
		if(actuatorlist.size()>1 && actuatorlist.size()!=0)
		{
		
			// create list selection for matching actuators
			ListSelectorDialog lsd2=new ListSelectorDialog(new Frame(), locale.getString("tit.selDevice"),
														   actuatorlist );
			lsd2.setVisible(true);
			
			if(lsd2.getSelection()==null) // cancel selected
			{
				return null;
			}
			else
			{
				return ((ListEntry)lsd2.getSelection()).getValue();
			}
		}
		else
		{
			return ((ListEntry)actuatorlist.firstElement()).getValue();
		}
		
	}
	
	/**
	 * Add new device to installation model.
	 * @param finder
	 * @param dialog
	 * @return the actuator id in the installation model for the new actuator.
	 */
	private String addNewDevice(EIBDeviceConfigurator finder, NewEndDeviceDialog dialog)
	{
		
		EIBDevicesDataModel devmodel=(EIBDevicesDataModel)this.project.getApplication().getBusDeviceDataModel("EIB");
		InstallationModel imodel=this.project.getInstallationModel();
		
		Vector actuators=new Vector();
		
		// Add new actuator	
		actuators=finder.findNewActuators();
		
		if(actuators.isEmpty()) // no matching device found
		{
			JOptionPane.showMessageDialog(null, locale.getString("mess.noDeviceFound"));
			return null;
		}
		Vector actuatorlist=new Vector();
		
		for(Enumeration e=actuators.elements(); e.hasMoreElements(); )
		{
			String id=(String)e.nextElement();
			
			if(DEBUG)
			{		
				logger.debug(devmodel.getName(id));
			}
			
			String text=devmodel.getManufacturerName(id)+" - "+devmodel.getName(id);
			ListEntry le=new ListEntry(text, id);
			actuatorlist.addElement(le);	
		}
		
		// create list selection for matching actuators
		ListSelectorDialog lsd2=new ListSelectorDialog(new Frame(), locale.getString("tit.selDevice"),
													   actuatorlist );
		lsd2.setVisible(true);
		if(lsd2.getSelection()==null) // cancel selected
		{
			return null;
		}
		
		// ask for actuator name
		String name=JOptionPane.showInputDialog(null, 
												locale.getString("mess.inputname"),
												locale.getString("tit.input"),
		 										JOptionPane.WARNING_MESSAGE);
		 										
		if(name == null)
		{
			return null;	
		}
		if(name.equals(""))
		{
			name=locale.getString("noname");	
		}
		
		// set physical address
		String pastring="";
		EIBPhaddress pad;
		while(true)
		{
			pastring=JOptionPane.showInputDialog(null, locale.getString("mess.inputPhAddr"));
			if(pastring==null)
			{
				return null;
			}
		
			try
			{
				pad=new EIBPhaddress(pastring);
				if(imodel.isEIBPhAddressInUse(pad))
				{
					JOptionPane.showMessageDialog(null, locale.getString("mess.phAddrInUse"));
				}
				else
				{
					break;
				}
				
			}
			catch(EIBAddressFormatException afe)
			{
				JOptionPane.showMessageDialog(null, locale.getString("mess.wrongPhAddrFormat"));	
			}
			
		}
		
		String dID=((ListEntry)lsd2.getSelection()).getValue(); // selected EIB device ID
		
		// Add new actuator
		String actuatorID=imodel.addActuator(name);
		
		imodel.setProperty(actuatorID, "device-name", devmodel.getName(dID));
		imodel.setProperty(actuatorID, "device-id", dID);
		imodel.setProperty(actuatorID, "device-state", "unprogrammed");
		imodel.setProperty(actuatorID, "manufacturer", devmodel.getManufacturerName(dID));
		imodel.setProperty(actuatorID, "bussystem", "EIB");
		imodel.setProperty(actuatorID, "installation-location", dialog.getInstallationLocation());
		imodel.setProperty(actuatorID, "eib-physical-address", pad.toString());
		
		
		Vector fgroups=(Vector)devmodel.getFunctionGroupIDs(dID);
		for(Enumeration e=fgroups.elements(); e.hasMoreElements(); )
		{
			String devfuncgroupID=(String)e.nextElement();
			
			String fgid=imodel.addFunctionGroup(actuatorID); // Add function group
			
			Vector funcs=(Vector)devmodel.getFunctionIDs(devfuncgroupID);
			for(Enumeration f=funcs.elements(); f.hasMoreElements(); )
			{
				// add functions
				
				String devfuncID=(String)f.nextElement();
				
				String fid=imodel.addFunction(fgid);
				imodel.setName(fid, devmodel.getName(devfuncID));
				
				Node source=devmodel.getDataRootNode(devfuncID);
				Node dest  =imodel.getDataRootNode(fid);
				imodel.writeDOMNodeValue(dest, new StringTokenizer("type", "/"), 
										 devmodel.readDOMNodeValue(source, new StringTokenizer("type", "/")));
				
				imodel.writeDOMNodeValue(dest, new StringTokenizer("eis-type", "/"), 
										 devmodel.readDOMNodeValue(source, new StringTokenizer("eis-type", "/")));
				
				imodel.writeDOMNodeValue(dest, new StringTokenizer("com-object", "/"), 
										 devmodel.readDOMNodeValue(source, new StringTokenizer("com-object", "/")));
				
				imodel.writeDOMNodeValue(dest, new StringTokenizer("devmodelid", "/"), devfuncID);
				
				imodel.writeDOMNodeValue(dest, new StringTokenizer("state", "/"), "unused");
				
				
			}
		}
		
		ArchitecturalDataModel amodel=this.project.getArchitecturalDataModel();
		
		amodel.addBusDevice(finder.getInstallationLocationID(), actuatorID);
		
		return actuatorID;
	}
	

	/**
	 * @see basys.client.commands.Command#unexecute()
	 */
	public void unexecute()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
