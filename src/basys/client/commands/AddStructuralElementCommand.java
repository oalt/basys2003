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
import basys.datamodels.architectural.ArchitecturalDataModel;
import java.awt.Component;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;


/**
 * AddStructuralElementCommand.java
 * 
 * @author 	oalt
 * @version	$Id: AddStructuralElementCommand.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class AddStructuralElementCommand extends Command
{
	private static Logger logger=Logger.getLogger(AddStructuralElementCommand.class);
	
	private ArchitecturalDataModel model;
	private String destID;
	private String elementID;
	private String newElementID;
	
	/**
	 * Constructor for AddStructuralElementCommand.
	 */
	private AddStructuralElementCommand(Component c)
	{
		super(c);
	}
	
	public AddStructuralElementCommand(Component o, ArchitecturalDataModel model, String destID, String elementID)
	{
		super(o);
		this.model=model;
		this.destID=destID;
		this.elementID=elementID;	
	}
	
	/**
	 * @see basys.commands.Command#execute()
	 */
	public boolean execute()
	{
		logger.debug("destID: "+this.destID);
		logger.debug("elementID: "+this.elementID);
		
		if(this.destID.startsWith(ArchitecturalDataModel.PROJECT_PREFIX)
		   && this.elementID.startsWith(ArchitecturalDataModel.BUILDING_PREFIX))
		{
			return this.addElement();				
		}
		else if(this.destID.startsWith(ArchitecturalDataModel.BUILDING_PREFIX)
		   && this.elementID.startsWith(ArchitecturalDataModel.FLOOR_PREFIX))
		{
			return this.addElement();				
		}
		else if(this.destID.startsWith(ArchitecturalDataModel.FLOOR_PREFIX)
		   && this.elementID.startsWith(ArchitecturalDataModel.ROOM_PREFIX))
		{
			return this.addElement();				
		}
		else if(this.destID.startsWith(ArchitecturalDataModel.ROOM_PREFIX)
		   && this.elementID.startsWith(ArchitecturalDataModel.JUNCTION_BOX_PREFIX))
		{
			return this.addElement();				
		}	
		logger.debug("end execute.");	 							
		return false;
	}
	
	/**
	 * 
	 */
	private boolean addElement()
	{
		logger.debug("addElement..");	
		Node n=model.getDataRootNode(destID);
		
		ResourceBundle locale=LocaleResourceBundle.getLocale();
		
		String name=JOptionPane.showInputDialog(null, 
												locale.getString("mess.inputname"),
												locale.getString("tit.input"),
		 										JOptionPane.WARNING_MESSAGE);
	
		if(name == null)
		{
			return false;	
		}
		if(name.equals(""))
		{
			name=locale.getString("noname");	
		}
		
		String eID=model.addChild(this.destID);
		
		model.writeDOMNodeValue(model.getDataRootNode(eID),new StringTokenizer("name","/"), name);
		
		this.newElementID=eID;
		
		return true;		
	}
	
	/**
	 * @see basys.commands.Command#unexecute()
	 */
	public void unexecute()
	{
	}
	
	public Object clone()
	{
		return new AddStructuralElementCommand(o);
	}
	
	public String getNewElementID()
	{
		return this.newElementID;	
	}
}
