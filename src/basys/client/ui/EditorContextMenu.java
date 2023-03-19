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
package basys.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import basys.LocaleResourceBundle;
import basys.Util;
import basys.client.Project;
import basys.client.commands.AddDeviceToRoomCommand;
import basys.client.commands.AddSensorCommand;
import basys.client.commands.AddStructuralElementCommand;
import basys.client.ui.dialogs.NewEndDeviceDialog;

/**
 * EditorContextMenu.java
 * 
 * 
 * @author	olli
 * @version $Id: EditorContextMenu.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EditorContextMenu extends JPopupMenu implements ActionListener
{
	
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	private Project p;
	private String id;
	
	/**
	 * 
	 */
	public EditorContextMenu(Project p, String id)
	{
		super();
		this.p=p;
		this.id=id;
		initUI();
	}
	
	private void initUI()
	{
		JMenuItem mitem;
		
		if(this.id.startsWith("project"))
		{
			mitem=new JMenuItem(locale.getString("km.newbuilding"));
			mitem.setIcon(Util.getIcon("building.png"));
			mitem.setActionCommand("new-building");
			mitem.addActionListener(this);
			this.add(mitem);
		}
		else if(this.id.startsWith("building"))
		{
			mitem=new JMenuItem(locale.getString("km.newfloor"));
			mitem.setActionCommand("new-floor");
			mitem.setIcon(Util.getIcon("floor.png"));
			mitem.addActionListener(this);
			this.add(mitem);
		}
		else if(this.id.startsWith("floor"))
		{
			mitem=new JMenuItem(locale.getString("km.newroom"));
			mitem.setIcon(Util.getIcon("room.png"));
			mitem.setActionCommand("new-room");
			mitem.addActionListener(this);
			this.add(mitem);
		}
		else if(this.id.startsWith("room"))
		{
			mitem=new JMenuItem(locale.getString("km.newjunctionbox"));
			mitem.setIcon(Util.getIcon("junctionbox.png"));
			mitem.setActionCommand("new-junctionbox");
			mitem.addActionListener(this);
			this.add(mitem);
			
			this.addSeparator();
			
			mitem=new JMenuItem(locale.getString("km.newlamp"));
			mitem.setIcon(Util.getIcon("lamp16x16.png"));
			mitem.setActionCommand("dev-"+NewEndDeviceDialog.LAMP);
			mitem.addActionListener(this);
			this.add(mitem);
			
			mitem=new JMenuItem(locale.getString("km.newdimlamp"));
			mitem.setIcon(Util.getIcon("dimminglamp16x16.png"));
			mitem.setActionCommand("dev-"+NewEndDeviceDialog.DIMMABLE_LAMP);
			mitem.addActionListener(this);
			this.add(mitem);
			
			mitem=new JMenuItem(locale.getString("km.newvalve"));
			mitem.setIcon(Util.getIcon("valve16x16.png"));
			mitem.setActionCommand("dev-"+NewEndDeviceDialog.VALVE);
			mitem.addActionListener(this);
			this.add(mitem);
			
			mitem=new JMenuItem(locale.getString("km.newjal"));
			mitem.setIcon(Util.getIcon("jal16x16.png"));
			mitem.setActionCommand("dev-"+NewEndDeviceDialog.JALOUSIE);
			mitem.addActionListener(this);
			this.add(mitem);
			
			this.addSeparator();
			
			mitem=new JMenuItem(locale.getString("km.newsensor"));
			mitem.setIcon(Util.getIcon("sensor16x16.png"));
			mitem.setActionCommand("sensor");
			mitem.addActionListener(this);
			this.add(mitem);
		}
		
		
		if(!this.id.startsWith("junctionbox") && !this.id.startsWith("project"))
		{
			this.addSeparator();
		}
		
		if(!this.id.startsWith("project"))
		{
			mitem=new JMenuItem(locale.getString("mi.cut"));
			mitem.setIcon(Util.getIcon("Cut.gif"));
			mitem.setActionCommand("cut");
			mitem.addActionListener(this);
			this.add(mitem);
			
			mitem=new JMenuItem(locale.getString("mi.copy"));
			mitem.setIcon(Util.getIcon("Copy.gif"));
			mitem.setActionCommand("copy");
			mitem.addActionListener(this);
			this.add(mitem);
			
			mitem=new JMenuItem(locale.getString("mi.paste"));
			mitem.setIcon(Util.getIcon("Paste.gif"));
			mitem.setActionCommand("paste");
			mitem.addActionListener(this);
			mitem.setEnabled(!this.p.getApplication().isBufferEmpty());
			this.add(mitem);
			
			mitem=new JMenuItem(locale.getString("mi.delete"));
			mitem.setIcon(Util.getIcon("Delete.gif"));
			mitem.setActionCommand("delete");
			mitem.addActionListener(this);
			this.add(mitem);
		}
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd=e.getActionCommand();
		
		if(cmd.startsWith("new-"))
		{
			AddStructuralElementCommand command=new AddStructuralElementCommand(null, 
																				p.getArchitecturalDataModel(),
																				this.id,
																				cmd.replaceAll("new-", ""));
			if(command.execute())
			{
				// TODO command to command list 
			}
		}
		else if(cmd.startsWith("dev-"))
		{
			AddDeviceToRoomCommand command = new AddDeviceToRoomCommand(null,
																		this.p,
																		this.id,
																		cmd.replaceAll("dev-", ""),
																		null);
			if(command.execute())
			{
				// TODO command to command list 
			}
		}
		else if(cmd.equals("sensor"))
		{
			AddSensorCommand command = new AddSensorCommand(null, 
															this.p,
															this.id, 
															null);
			
			if(command.execute())
			{
				// TODO command to command list 
			}
		}
	}
	
}
