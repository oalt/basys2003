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
 *                           
 *               2006, 		 Added KNXnet/IP (EIBnet/IP) functionality
 *               			 B. Malinowsky
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
import basys.client.ApplicationEventHandler;

import java.awt.*;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;


/**
 * Mainframe.java
 * 
 * Main frame of BASys application, including menu and tool bar.
 * 
 * @author 	oalt
 * @version	$Id: Mainframe.java,v 1.2 2006/09/14 13:42:41 fritzchen Exp $
 */
public class Mainframe extends JFrame implements ActionListener, Observer
{	
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();

	private ApplicationEventHandler handler;
		
	// Menu Bar	
	private JMenuBar menubar;	
	
	private JMenu fileMenu;
	private JMenuItem newProjectMenuItem;
	private JMenuItem openProjectMenuItem;
	private JMenuItem closeProjectMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem printMenuItem;
	private JMenuItem exitMenuItem;
	
	private JMenu editMenu;
	private JMenuItem undoMenuItem;
	private JMenuItem redoMenuItem;
	private JMenuItem cutMenuItem;
	private JMenuItem copyMenuItem;
	private JMenuItem pasteMenuItem;
	private JMenuItem deleteMenuItem;
	
	private JMenu planingMenu;
	private JMenuItem editArchitecturalDataMenuItem;
	
	private JMenu commisioningMenu;
	private JMenuItem programEIBdevicesMenuItem;
	
	private JMenu testMenu;
	
	private JMenu diagnosticMenu;
	private JMenuItem openTelegramtracerMenuItem;
	private JMenuItem showEIBTelegramDialogMenuItem;
	private JRadioButtonMenuItem gatewayEIBDarmstadtMenuItem;
	private JRadioButtonMenuItem gatewayLinuxBCU1MenuItem;
	private JRadioButtonMenuItem gatewayKNXnetIPMenuItem;	
	private JMenuItem stopEIBGateway;
	private JMenuItem startEIBGateway;
	
	private JMenu dataMenu;
	private JMenuItem convertVDFileMenuItem;
	private JMenuItem makeEIBDataMenuItem;
	
	private JMenu tableausMenu;
	
	
	private JMenu helpMenu;
	private JMenuItem aboutMenuItem;
	
	// Tool Bar
	private JToolBar toolBar;
	
	
	
	// Status Bar
	private JLabel statusBar = new JLabel("Status: "+locale.getString("stat.ready"));  
		
	/**
	 * Constructor for Mainframe.
	 * @throws HeadlessException
	 */
	public Mainframe(ApplicationEventHandler handler)
	{
		
		super("BASys 2003");
		
		this.setIconImage(Util.getImage("mainicon16x16.png"));
		
		this.handler=handler;
				
		initMenuBar();
		
		initToolBar();
		
		JPanel startupPanel=new JPanel(new BorderLayout());
		startupPanel.add(new JLabel(Util.getIcon("basys.png")), SwingConstants.CENTER);
		startupPanel.setBackground(Color.WHITE);
		
		this.getContentPane().setLayout(new BorderLayout());
		
		this.setApplicationPane(startupPanel);
		
		// Center frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.setSize(new Dimension(screenSize.width-50, screenSize.height-100));

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

	/**
	 * Init menu bar for the frame
	 */
	private void initMenuBar()
	{
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent we) 
			{
				handler.handleExit();
			}
		});
		
		this.menubar=new JMenuBar();
		
		// **************************** File menu ******************************************
		
		this.fileMenu=new JMenu(locale.getString("m.file"));
		
		this.newProjectMenuItem=new JMenuItem(locale.getString("mi.newproject"));
		this.newProjectMenuItem.setActionCommand("new project");
		this.newProjectMenuItem.addActionListener(this);
		this.fileMenu.add(this.newProjectMenuItem);
		
		this.fileMenu.addSeparator();
				
		this.openProjectMenuItem=new JMenuItem(locale.getString("mi.openproject"));
		this.openProjectMenuItem.setActionCommand("open project");
		this.openProjectMenuItem.addActionListener(this);
		this.fileMenu.add(this.openProjectMenuItem);
		
		this.closeProjectMenuItem=new JMenuItem(locale.getString("mi.closeproject"));
		this.closeProjectMenuItem.setActionCommand("close project");
		this.closeProjectMenuItem.setEnabled(false);
		this.closeProjectMenuItem.addActionListener(this);
		this.fileMenu.add(this.closeProjectMenuItem);
		
		this.fileMenu.addSeparator();
		
		this.saveMenuItem=new JMenuItem(locale.getString("mi.save"));
		this.saveMenuItem.setActionCommand("save");
		this.saveMenuItem.setEnabled(false);
		this.saveMenuItem.addActionListener(this);
		this.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		this.fileMenu.add(this.saveMenuItem);
		
		this.saveAsMenuItem=new JMenuItem(locale.getString("mi.saveas"));
		this.saveAsMenuItem.setActionCommand("save as");
		this.saveAsMenuItem.setEnabled(false);
		this.saveAsMenuItem.addActionListener(this);
		this.fileMenu.add(this.saveAsMenuItem);
		
		this.fileMenu.addSeparator();
		
		this.printMenuItem=new JMenuItem(locale.getString("mi.print"));
		this.printMenuItem.setActionCommand("print");
		this.printMenuItem.addActionListener(this);
		this.printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		this.printMenuItem.setEnabled(false);
		this.fileMenu.add(this.printMenuItem);
		
		this.fileMenu.addSeparator();
		
		this.exitMenuItem=new JMenuItem(locale.getString("mi.exit"));
		this.exitMenuItem.setActionCommand("exit");
		this.exitMenuItem.addActionListener(this);
		this.exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		this.fileMenu.add(this.exitMenuItem);
		
		//********************************* Edit Menu *****************************************
		
		this.editMenu=new JMenu(locale.getString("m.edit"));
		//this.editMenu.setEnabled(false);
		
		this.undoMenuItem=new JMenuItem(locale.getString("mi.undo"));
		this.undoMenuItem.setActionCommand("undo");
		this.undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		this.undoMenuItem.setEnabled(false);
		this.undoMenuItem.addActionListener(this);
		this.editMenu.add(this.undoMenuItem);
			
		this.redoMenuItem=new JMenuItem(locale.getString("mi.redo"));
		this.redoMenuItem.setActionCommand("redo");
		this.redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		this.redoMenuItem.setEnabled(false);
		this.redoMenuItem.addActionListener(this);
		this.editMenu.add(this.redoMenuItem);
		
		this.editMenu.addSeparator();
		
		this.cutMenuItem=new JMenuItem(locale.getString("mi.cut"));
		this.cutMenuItem.setActionCommand("cut");
		this.cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		this.cutMenuItem.setEnabled(false);
		this.cutMenuItem.addActionListener(this);
		this.editMenu.add(this.cutMenuItem);
		
		this.copyMenuItem=new JMenuItem(locale.getString("mi.copy"));
		this.copyMenuItem.setActionCommand("copy");
		this.copyMenuItem.setEnabled(false);
		this.copyMenuItem.addActionListener(this);
		this.copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		this.editMenu.add(this.copyMenuItem);
		
		this.pasteMenuItem=new JMenuItem(locale.getString("mi.paste"));
		this.pasteMenuItem.setActionCommand("paste");
		this.pasteMenuItem.setEnabled(false);
		this.pasteMenuItem.addActionListener(this);
		this.pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		this.editMenu.add(this.pasteMenuItem);
		
		this.deleteMenuItem=new JMenuItem(locale.getString("mi.delete"));
		this.deleteMenuItem.setActionCommand("delete");
		this.deleteMenuItem.setEnabled(false);
		this.deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		this.deleteMenuItem.addActionListener(this);
		this.editMenu.add(this.deleteMenuItem);
		
		// *************************** planing menu ********************************************
		
		this.planingMenu=new JMenu(locale.getString("m.planing"));
		
		// *************************** commisioning menu ***************************************
		
		this.commisioningMenu=new JMenu(locale.getString("m.commissioning"));
		
		this.programEIBdevicesMenuItem=new JMenuItem(locale.getString("mi.programEIB"));
		this.programEIBdevicesMenuItem.setActionCommand("program EIB");
		this.programEIBdevicesMenuItem.addActionListener(this);
		this.programEIBdevicesMenuItem.setEnabled(false);
		this.commisioningMenu.add(this.programEIBdevicesMenuItem);
		
		// ************************* diagnostic menu *******************************************
		this.diagnosticMenu=new JMenu(locale.getString("m.diagnostic"));
		
		this.openTelegramtracerMenuItem=new JMenuItem(locale.getString("mi.openTelegramtracer"));
		this.openTelegramtracerMenuItem.setActionCommand("open telegramtracer");
		this.openTelegramtracerMenuItem.addActionListener(this);
		this.diagnosticMenu.add(this.openTelegramtracerMenuItem);
		
		this.showEIBTelegramDialogMenuItem=new JMenuItem(locale.getString("mi.openEIBTelegramDialog"));
		this.showEIBTelegramDialogMenuItem.setActionCommand("show EIB frame dialog");
		this.showEIBTelegramDialogMenuItem.addActionListener(this);
		this.diagnosticMenu.add(this.showEIBTelegramDialogMenuItem);
		
		this.diagnosticMenu.addSeparator();
		this.diagnosticMenu.add(new JLabel(" "+locale.getString("mi.gatewaytypeEIB")));
		ButtonGroup bg=new ButtonGroup();
		
		// KNXnet/IP connection item
		this.gatewayKNXnetIPMenuItem=new JRadioButtonMenuItem(
				locale.getString("mi.gatewayKNXnetIP"), true);
		this.gatewayKNXnetIPMenuItem.setActionCommand("gateway KNXnet/IP");
		this.gatewayKNXnetIPMenuItem.addActionListener(this);
		this.diagnosticMenu.add(this.gatewayKNXnetIPMenuItem);
		
		this.gatewayEIBDarmstadtMenuItem=new JRadioButtonMenuItem(
								locale.getString("mi.gatewayEIBDarmstadt"),false);
		this.gatewayEIBDarmstadtMenuItem.setActionCommand("gateway TU-Darmstadt");
		this.gatewayEIBDarmstadtMenuItem.addActionListener(this);
		this.diagnosticMenu.add(this.gatewayEIBDarmstadtMenuItem);
		
		this.gatewayLinuxBCU1MenuItem=new JRadioButtonMenuItem(
								locale.getString("mi.gatewayEIBLinuxBCU1"), false);
		this.gatewayLinuxBCU1MenuItem.setActionCommand("gateway Linux BCU1");
		this.gatewayLinuxBCU1MenuItem.addActionListener(this);
		this.diagnosticMenu.add(this.gatewayLinuxBCU1MenuItem);
		
		
		bg.add(this.gatewayKNXnetIPMenuItem);
		bg.add(this.gatewayEIBDarmstadtMenuItem);
		bg.add(this.gatewayLinuxBCU1MenuItem);
		
		this.diagnosticMenu.addSeparator();
		
		this.stopEIBGateway=new JMenuItem(locale.getString("mi.stopEIBGateway"));
		this.stopEIBGateway.setActionCommand("stop eib gateway");
		this.stopEIBGateway.setEnabled(false);		
		this.stopEIBGateway.addActionListener(this);
		this.diagnosticMenu.add(this.stopEIBGateway);
		
		this.startEIBGateway=new JMenuItem(locale.getString("mi.startEIBGateway"));
		this.startEIBGateway.setActionCommand("start eib gateway");
		this.startEIBGateway.setEnabled(true);
		this.startEIBGateway.addActionListener(this);
		this.diagnosticMenu.add(this.startEIBGateway);
		
		this.enableGatewaySelectors(true);
		// ************************** test menu ************************************************
		
		this.testMenu=new JMenu(locale.getString("m.test"));
		this.testMenu.setEnabled(false);
		
		// ************************** data menu ************************************************
		
		this.dataMenu=new JMenu(locale.getString("m.data"));
		
		this.convertVDFileMenuItem=new JMenuItem(locale.getString("mi.convertvd"));
		this.convertVDFileMenuItem.setActionCommand("convert vd file");
		this.convertVDFileMenuItem.addActionListener(this);
		this.dataMenu.add(this.convertVDFileMenuItem);
		
		this.makeEIBDataMenuItem=new JMenuItem(locale.getString("mi.makeeibdata"));
		this.makeEIBDataMenuItem.setActionCommand("make eib data");
		this.makeEIBDataMenuItem.addActionListener(this);
		this.dataMenu.add(this.makeEIBDataMenuItem);
		
		// ************************** tableau menu *********************************************
		
		this.tableausMenu=new JMenu(locale.getString("m.tableaus"));
		
		// ****************************** Help Menu ********************************************
		
		this.helpMenu=new JMenu(locale.getString("m.help"));
		
		this.aboutMenuItem=new JMenuItem(locale.getString("mi.about"));
		this.aboutMenuItem.setActionCommand("about");
		this.aboutMenuItem.addActionListener(this);
		this.helpMenu.add(this.aboutMenuItem);
		
		
		// ******************** add menus to menu bar ******************************************
		
		this.menubar.add(this.fileMenu);
		this.menubar.add(this.editMenu);
		//this.menubar.add(this.planingMenu);
		this.menubar.add(this.commisioningMenu);
		this.menubar.add(this.diagnosticMenu);
		this.menubar.add(this.testMenu);
		this.menubar.add(this.dataMenu);
		//this.menubar.add(this.tableausMenu);
		this.menubar.add(this.helpMenu);
		
		this.setJMenuBar(this.menubar);		
	}
	
	/**
	 * 
	 *
	 */
	private void initToolBar()
	{
		this.toolBar=new JToolBar();
		
		this.toolBar.add(this.getToolBarButton("New.gif", "new project", "new", true, locale.getString("mi.newproject")));
		
		this.toolBar.addSeparator();
		
		this.toolBar.add(this.getToolBarButton("Open.gif", "open project", "open", true, locale.getString("mi.openproject")));
		
		this.toolBar.add(this.getToolBarButton("close.gif", "close project", "close", false, locale.getString("mi.closeproject")));
		
		this.toolBar.addSeparator();
		
		this.toolBar.add(this.getToolBarButton("Save.gif", "save", "save", false, locale.getString("mi.save")));
		
		this.toolBar.add(this.getToolBarButton("Saveto.gif", "save as", "saveas", false, locale.getString("mi.saveas")));
		
		this.toolBar.addSeparator();
		
		this.toolBar.add(this.getToolBarButton("Undo.gif", "undo", "undo", false, locale.getString("mi.undo")));
		
		this.toolBar.add(this.getToolBarButton("Redo.gif", "redo", "redo", false, locale.getString("mi.redo")));
		
		this.toolBar.addSeparator();
		
		this.toolBar.add(this.getToolBarButton("Cut.gif", "cut", "cut", false, locale.getString("mi.cut")));
		
		this.toolBar.add(this.getToolBarButton("Copy.gif", "copy", "copy", false, locale.getString("mi.copy")));
		
		this.toolBar.add(this.getToolBarButton("Paste.gif", "paste", "paste", false, locale.getString("mi.paste")));
		
		this.toolBar.add(this.getToolBarButton("Delete.gif", "delete", "delete", false, locale.getString("mi.delete")));
		
		this.toolBar.add(this.getToolBarButton("Print.gif", "print", "print", false, locale.getString("mi.print")));
		
		this.toolBar.addSeparator();
		
		this.toolBar.add(this.getToolBarButton("exit.gif", "exit", "exit", true, locale.getString("mi.exit")));
		
		this.getContentPane().add(this.toolBar, BorderLayout.NORTH);
	}
	
	private JButton getToolBarButton(String iconfilename, String actionCommand, String name, boolean enabled, String tooltip)
	{
		JButton button=new JButton(Util.getIcon(iconfilename));
		button.setFocusPainted(false);
		button.setBorder(null);
		button.setActionCommand(actionCommand);
		button.setEnabled(enabled);
		button.setName(name);
		button.setToolTipText(tooltip);		
		button.addActionListener(this);
		
		return button;
	}
	
	private void enableToolBarButton(String name, boolean b)
	{
		Component[] comps=this.toolBar.getComponents();
		
		for(int i=0; i<comps.length; i++)
		{
			Component c=comps[i];
			if(c instanceof JButton)
			{
				JButton but=(JButton)c;
				if(but.getName().equals(name))
				{
					but.setEnabled(b);
				}
			}
		}
		
	}
	
	public void enableSaveMenu(boolean b)
	{
		this.saveMenuItem.setEnabled(b);
		this.enableToolBarButton("save", b);
	}

	/**
	 * Set the main application component 
	 */
	public void setApplicationPane(Component c)
	{
		this.getContentPane().removeAll();
		this.getContentPane().add(this.statusBar, BorderLayout.SOUTH);
		this.getContentPane().add(c, BorderLayout.CENTER);	
		this.getContentPane().add(this.toolBar, BorderLayout.NORTH);	
		this.getContentPane().validate();
		this.getContentPane().repaint();
		this.validate();
	}
	
	/**
	 * Set text of status bar
	 */
	public void setStatusBar(String text)
	{
		this.statusBar.setText(text);
	}
	
	/**
	 * Set title
	 * @param text
	 */
	public void setTitleBar(String text)
	{
		this.setTitle("BASys 2003 "+ text);
	}
	
	/**
	 * Handle events
	 */	
	public void actionPerformed(ActionEvent e)
	{
		String cmd=e.getActionCommand();
		if(cmd.equals("save"))
		{
			handler.handleSave();
		}
		else if(cmd.equals("new project"))
		{
			handler.handleNewProject();
		}
		else if(cmd.equals("open project"))
		{
			handler.handleOpenProject();
		}
		else if(cmd.equals("exit"))
		{
			handler.handleExit();	
		}
		else if(cmd.equals("make eib data"))
		{
			handler.handleMakeEIBData();
		}
		else if(cmd.equals("convert vd file"))
		{
			handler.handleConvertVDFile();
		}
		else if(cmd.equals("open telegramtracer"))
		{
			handler.handleOpenTelegramtracer();
		}
		else if(cmd.equals("show EIB frame dialog"))
		{
			handler.handleOpenEIBFrameDialog();
		}
		else if(cmd.equals("program EIB"))
		{
			handler.handleProgramEIBDevices();
		}
		else if(cmd.equals("stop eib gateway"))
		{
			handler.handleStopEIBGateway();
		}
		else if(cmd.equals("start eib gateway"))
		{
			handler.handleStartEIBGateway();
		}
		else if(cmd.equals("close project"))
		{
			handler.handleCloseProject();
		}
		else if(cmd.equals("about"))
		{
			handler.handleAbout();
		}
		else if(cmd.equals("gateway TU-Darmstadt"))
		{
			handler.handleGWDarmstadt();
		}
		else if(cmd.equals("gateway Linux BCU1"))
		{
			handler.handleGWLinuxBCU1();
		}
		else if(cmd.equals("gateway KNXnet/IP"))
		{
			handler.handleGWKNXnetIP();
		}		
	}

	/**
	 * @see java.util.Observer#update(Observable, Object)
	 */
	public void update(Observable o, Object arg)
	{
		if(!this.getTitle().endsWith("*"))
		{
			this.setTitle(this.getTitle()+" *");
		}
		this.enableSaveMenu(true);
	}
	
	/**
     * Überschrieben, so dass eine Beendigung beim Schließen des Fensters möglich ist.
     */
    protected void processWindowEvent(WindowEvent e)
    {
        //super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
            this.handler.handleExit();
        }
    }
    
    public void enableProgramEIB(boolean b)
	{
		this.programEIBdevicesMenuItem.setEnabled(b);
	}

	public void enableStartGateway(boolean b)
	{
		this.startEIBGateway.setEnabled(b);
	}
	
	public void enableStopGateway(boolean b)
	{
		this.stopEIBGateway.setEnabled(b);
	}
	
	public void enableCloseProject(boolean b)
	{
		this.closeProjectMenuItem.setEnabled(b);
		this.enableToolBarButton("close", b);
	}
	
	public void enableGatewaySelectors(boolean b)
	{
		this.gatewayEIBDarmstadtMenuItem.setEnabled(b);
		// Note: BCU1 connection:
		// implementation not functional yet
		this.gatewayLinuxBCU1MenuItem.setEnabled(false);
		this.gatewayKNXnetIPMenuItem.setEnabled(b);		
	}
}
