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
package basys.client.ui.dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import basys.LocaleResourceBundle;
import basys.Util;
import basys.client.ui.VerticalFlowLayout;

import javax.swing.JDialog;
import javax.swing.*;
import javax.swing.border.TitledBorder;



/**
 * NewEndDeviceDialog.java
 * 
 * 
 * @author	oalt
 * @version $Id: NewEndDeviceDialog.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class NewEndDeviceDialog extends JDialog implements ActionListener
{
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	public static final int LAMP 			= 1;
	public static final int DIMMABLE_LAMP 	= 2;
	public static final int VALVE			= 3;
	public static final int JALOUSIE		= 4;
	
	public static final int SENSOR			= 100;
	
	private int deviceType;
	
	private int exitState=0;
	
	private JTextField nameTextField=new JTextField();
	private String bussystem;
	private String installationLocation="REG";
	
	private String displayedName=null;
	
	public NewEndDeviceDialog(Frame parent, String prefferedBusSystem, int devType)
	{
		super(parent);
		
		this.bussystem=prefferedBusSystem;	
		this.deviceType=devType;
		
		this.setModal(true);

		this.setDefaultCloseOperation(
			JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				exitState=0;
				setVisible(false);
			}
		});
		
		initUI();	
		
		this.pack();
		this.setResizable(false);
		
		// Center frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		Dimension frameSize = this.getSize();
		
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
	
	public void setName(String name)
	{
		this.nameTextField.setText(name);
	}
	
	public String getInstallationLocation()
	{
		return this.installationLocation;
	}
	
	public String getBussystem()
	{
		return this.bussystem;
	}
	
	public int getDeviceType()
	{
		return this.deviceType;
	}
	
	public String getName()
	{
		return this.nameTextField.getText();
	}
	
	/**
	 * Show the dialog.
	 * @return 0 if cancled and 1 if new project is selected.
	 */
	public int showDialog()
	{
		this.setVisible(true);
		while(this.isVisible())
		{
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException ie)
			{
			
			}
		
		}
		return exitState;
	}
	
	
	public void initUI()
	{
		// set dialog title
		String title="";
		Icon icon=null;
		switch(this.deviceType)
		{
			case LAMP:
				title=locale.getString("tit.newLamp");
				icon=Util.getIcon("lamp16x16.png");
			break;
			case DIMMABLE_LAMP:
				title=locale.getString("tit.newDimmableLamp");
				icon=Util.getIcon("dimminglamp16x16.png");
			break;
			case VALVE:
				title=locale.getString("tit.newValve");
				icon=Util.getIcon("valve16x16.png");
			break;
			case JALOUSIE:
				title=locale.getString("tit.newJal");
				icon=Util.getIcon("jal16x16.png");
			break;
			case SENSOR:
				title=locale.getString("tit.newSensor");
				icon=Util.getIcon("sensor16x16.png");
			break;
		}
		
		this.setTitle(title);
		
		JPanel mainp = new JPanel(new VerticalFlowLayout(5));
		JPanel maincpanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		JPanel cpanel=new JPanel(new VerticalFlowLayout(5));
		
		maincpanel.setBorder(new TitledBorder(title));
		
		maincpanel.add(new JLabel(icon));
		
		this.nameTextField.setName(locale.getString("noname"));
		this.nameTextField.setCaretPosition(0);
		this.nameTextField.setSelectionStart(0);
		this.nameTextField.setSelectionEnd(nameTextField.getText().length());
		
		
		cpanel.add(new JLabel(locale.getString("mess.inputname")));
		cpanel.add(nameTextField);
		
		cpanel.add(new JLabel(" "+locale.getString("mess.selectInstallLocation")));
		
		// Radio buttons for selecting installation location
		JPanel bgpanel=new JPanel(new FlowLayout());
		
		ButtonGroup bg=new ButtonGroup();
		
		JRadioButton rbutton=new JRadioButton(locale.getString("l.ilREG"), true);
		rbutton.setActionCommand("REG");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);

		rbutton=new JRadioButton(locale.getString("l.ilUP"));
		rbutton.setEnabled(true);
		rbutton.setActionCommand("UP");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		rbutton=new JRadioButton(locale.getString("l.ilKI"));
		rbutton.setEnabled(true);
		rbutton.setActionCommand("KI");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		rbutton=new JRadioButton(locale.getString("l.ilByDevice"));
		rbutton.setEnabled(true);
		rbutton.setActionCommand("ByDevice");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		cpanel.add(bgpanel);
		
		
		
		if(this.bussystem.equals("none"))
		{
			cpanel.add(new JLabel(" "+locale.getString("l.chooseBusSystem")));
			
			JPanel bgpanel1=new JPanel();
			
			ButtonGroup bg2=new ButtonGroup();
			
			rbutton=new JRadioButton("EIB", true);
		   	rbutton.setActionCommand("EIB");
		   	rbutton.addActionListener(this);
		   	bg2.add(rbutton);
		   	bgpanel1.add(rbutton);
			this.bussystem="EIB";
			
			
		   	rbutton=new JRadioButton("LON");
		   	rbutton.setEnabled(false);
		   	rbutton.setActionCommand("LON");
		   	rbutton.addActionListener(this);
		   	bg2.add(rbutton);
		   	bgpanel1.add(rbutton);
		
		   	rbutton=new JRadioButton("LCN");
		   	rbutton.setEnabled(false);
		   	rbutton.setActionCommand("LCN");
		   	rbutton.addActionListener(this);
		   	bg2.add(rbutton);
		   	bgpanel1.add(rbutton);
			
			cpanel.add(bgpanel1);
		}
		
		maincpanel.add(cpanel);
		
		mainp.add(maincpanel);
		
		JPanel buttonp=new JPanel(new FlowLayout());
		
		JButton button = new JButton(locale.getString("blabel.continue"));
		button.setActionCommand("continue");
		button.addActionListener(this);
		buttonp.add(button);
		
		button = new JButton(locale.getString("blabel.cancel"));
		button.setActionCommand("cancel");
		button.addActionListener(this);
		buttonp.add(button);
		
		mainp.add(buttonp);
		
		this.getContentPane().add(mainp);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd=e.getActionCommand();
		
		if(cmd.equals("cancel"))
		{
			this.exitState=0;
			this.setVisible(false);
		}
		else if(cmd.equals("EIB"))
		{
			this.bussystem="EIB";
		}
		else if(cmd.equals("LON"))
		{
			this.bussystem="LON";
		}
		else if(cmd.equals("LCN"))
		{
			this.bussystem="LCN";
		}
		else if(cmd.equals("REG"))
		{
			this.installationLocation="REG";
		}
		else if(cmd.equals("UP"))
		{
			this.installationLocation="UP";
		}
		else if(cmd.equals("KI"))
		{
			this.installationLocation="CI";
		}
		else if(cmd.equals("ByDevice"))
		{
			this.installationLocation="DEVICE";
		}
		else if(cmd.equals("continue"))
		{
			this.exitState=1;
			this.setVisible(false);
		}
		
	}
}
