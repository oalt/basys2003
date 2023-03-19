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

import basys.LocaleResourceBundle;
import basys.Util;
import basys.client.ui.dialogs.NewEndDeviceDialog;
import basys.client.ui.event.ShowViewEvent;
import basys.client.ui.event.ShowViewEventListener;

import java.awt.BorderLayout;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;

/**
 * StructuralViewPane.java
 * 
 * @author 	oalt
 * @version	$Id: StructuralViewPane.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class StructuralViewPane extends JPanel 
{
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	private JToolBar toolbar;
	
	public StructuralViewPane()
	{
		initUI();		
	}
	
	private void initUI()
	{
		this.setLayout(new BorderLayout());
	
		this.toolbar=new JToolBar();
		this.toolbar.setLayout(new FlowLayout(5));
		DnDLabel label=new DnDLabel("new-building");
		label.setIcon(Util.getIcon("building.png"));
		label.setText(locale.getString("l.building"));
		this.toolbar.add(label);	
		
		label=new DnDLabel("new-floor");
		label.setIcon(Util.getIcon("floor.png"));
		label.setText(locale.getString("l.floor"));
		this.toolbar.add(label);
		
		label=new DnDLabel("new-room");
		label.setIcon(Util.getIcon("room.png"));
		label.setText(locale.getString("l.room"));
		this.toolbar.add(label);
		
		label=new DnDLabel("new-junctionbox");
		label.setIcon(Util.getIcon("junctionbox.png"));
		label.setText(locale.getString("l.junctionbox"));
		this.toolbar.add(label);
		
		label=new DnDLabel("dev-"+NewEndDeviceDialog.LAMP);
		label.setIcon(Util.getIcon("lamp16x16.png"));
		label.setText(locale.getString("l.lamp"));
		this.toolbar.add(label);
		
		label=new DnDLabel("dev-"+NewEndDeviceDialog.DIMMABLE_LAMP);
		label.setIcon(Util.getIcon("dimminglamp16x16.png"));
		label.setText(locale.getString("l.dimlamp"));
		this.toolbar.add(label);

		label=new DnDLabel("dev-"+NewEndDeviceDialog.VALVE);
		label.setIcon(Util.getIcon("valve16x16.png"));
		label.setText(locale.getString("l.valve"));
		this.toolbar.add(label);
		
		label=new DnDLabel("dev-"+NewEndDeviceDialog.JALOUSIE);
		label.setIcon(Util.getIcon("jal16x16.png"));
		label.setText(locale.getString("l.jal"));
		this.toolbar.add(label);
		
		label=new DnDLabel("sensor");
		label.setIcon(Util.getIcon("sensor16x16.png"));
		label.setText(locale.getString("l.sensor"));
		this.toolbar.add(label);
		
		this.add(this.toolbar, BorderLayout.NORTH);
		
		//RoomPanel p=new RoomPanel(null, "");
		//JPanel p = new JPanel();
		//p.setBackground(Color.WHITE);
		
		//this.add(p, BorderLayout.CENTER);
	}
	
	public void setStructuralViewPanel(JPanel p)
	{
		this.removeAll();
		this.add(this.toolbar, BorderLayout.NORTH);
		this.add(p, BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}


	
}
