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
package basys.client;

import basys.*;
import basys.XMLSaver;
import basys.client.ui.ProjectviewPane;
import basys.client.ui.dialogs.AboutDialog;
import basys.client.ui.dialogs.EIBApplicationSelectorDialog;
import basys.client.ui.dialogs.EIBProgrammingDialog;
import basys.client.ui.dialogs.NewProjectDialog;
import basys.datamodels.eib.EIBDevicesDataModel;
import basys.eib.EIBConnection;
import basys.eib.EIBConnectionFactory;
import basys.eib.EIBFrameDialog;
import basys.eib.EIBTelegrammtraceframe;
import basys.eib.exceptions.EIBConnectionNotAvailableException;
import basys.eib.exceptions.EIBConnectionNotPossibleException;
import basys.eib.exceptions.EIBGatewayException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.io.*;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import basys.eib.dataaccess.vdconvert.VDConverter;

/**
 * ApplicationEventHandler.java
 * 
 * @author 	oalt
 * @version	$Id: ApplicationEventHandler.java,v 1.2 2006/09/14 13:42:40 fritzchen Exp $
 */
public class ApplicationEventHandler
{
	private static Logger logger=Logger.getLogger(ApplicationEventHandler.class);
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	private Application a;
	
	// HACK: to shutdown eibconnection on exit without using 
	// getEIBConnection(), because connection would be 
	// started anyway otherwise
	private EIBConnection conn;
	
	private EIBTelegrammtraceframe tracer=null;
	private EIBFrameDialog fd=null;
	
	/**
	 * Constructor for ApplicationEventHandler.
	 */
	public ApplicationEventHandler(Application a)
	{
		super();
		this.a=a;
	}
	
	public void handleSave()
	{
		File projectpath=new File(Util.getProjectPathPrefix()+"/"+a.getProject().getProjectDirectoryName());
		if(!projectpath.exists())
		{
			projectpath.mkdirs();
		}
		
		File f1=new File(Util.getProjectPathPrefix()+"/"+a.getProject().getProjectDirectoryName()+"/structure.xml");
		XMLSaver saver=new XMLSaver();
		saver.saveDocument(a.getProject().getArchitecturalDataModel().getDocumnet(), f1);
		
		File f2=new File(Util.getProjectPathPrefix()+"/"+a.getProject().getProjectDirectoryName()+"/installation.xml");		
		saver.saveDocument(a.getProject().getInstallationModel().getDocumnet(), f2);
		a.getProject().setModified(false);
		a.getMainFrame().enableSaveMenu(false);
		a.getMainFrame().setTitleBar("- "+a.getProject().getProjectName());
	}
	
	public void handleNewProject()
	{
		if(closeProject())
		{	
			NewProjectDialog dialog = new NewProjectDialog(a.getMainFrame());
			int val=dialog.showDialog();
			if(val==1)
			{
				Project p=new Project(this.a, dialog.getProjectName());
				p.setPrefferesBusSystem(dialog.getPrefferedBusSystem());
				
				p.setModified(true);
				
				openProjectView(p);
				this.a.getMainFrame().enableSaveMenu(true);
			}
		}
	}
	
	public void handleCloseProject()
	{
		this.closeProject();
	}
	
	/**
	 * 
	 * @return true if project was closed
	 */
	private boolean closeProject()
	{
		if(a.getProject()!=null)
		{
			if(a.getProject().isModified())
			{
				int sel=JOptionPane.showConfirmDialog(null, locale.getString("mess.saveChanges"));
				
				if(sel==0)
				{
					this.handleSave();
				}
				else if(sel==2 || sel==-1)
				{
					return false;
				}
			}
			
			JPanel panel=new JPanel(new BorderLayout());
			panel.setBackground(Color.WHITE);
			panel.add(new JLabel(Util.getIcon("basys.png")), SwingConstants.CENTER);
			this.a.getMainFrame().setApplicationPane(panel);
			this.a.getMainFrame().enableCloseProject(false);
			this.a.getMainFrame().setTitleBar("");
			this.a.getMainFrame().enableSaveMenu(false);
			this.a.getMainFrame().enableProgramEIB(false);
			
			this.a.setProject(null);
			
			return true;
		}
		return true;
	}
	
	public void handleOpenProject()
	{
		if(this.a.getProject()!=null)
		{
			this.closeProject();
		}
		
		JFileChooser fc=new JFileChooser(Util.getProjectPathPrefix());
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int returnVal = fc.showOpenDialog(a.getMainFrame());

		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			File file = fc.getSelectedFile();
			if(file.isDirectory())
			{
				if(isProjectPath(file))
				{
					Project p=new Project(this.a, file);
				
					openProjectView(p);
				}
				else
				{
					JOptionPane.showMessageDialog(null, locale.getString("mess.wrongProjectPath"));
				}
			}
		} 
		else 
		{
			// cancel
		}
	}
	
	private boolean isProjectPath(File selectedPath)
	{
		File insta=new File(selectedPath.getPath()+"/installation.xml");
		File arch=new File(selectedPath.getPath()+"/structure.xml");
		
		//logger.debug(insta.exists()+ " "+ arch.exists());
		
		return insta.exists() && arch.exists();
	}
	
	private void openProjectView(Project p)
	{
		a.setProject(p);
		// open Project view
		ProjectviewPane pvp=new ProjectviewPane(p);
		a.getMainFrame().setApplicationPane(pvp);

		// add observer for title bar
		p.getArchitecturalDataModel().addObserver(a.getMainFrame());
		p.getInstallationModel().addObserver(a.getMainFrame());
		
		a.getMainFrame().setTitleBar("- "+p.getProjectName());
		a.getMainFrame().enableProgramEIB(true);
		a.getMainFrame().enableCloseProject(true);
	}
	
	public void handleExit()
	{
		// close connection
		if (conn != null && conn.isConnected())
			conn.disconnect();
		// save global data
		XMLSaver saver=new XMLSaver();
		saver.saveDocument(a.getBusDeviceDataModel("EIB").getDocumnet(), new File(Util.getDataPathPrefix()+"/global-data/eib/eib-devices.xml"));
		
		if(closeProject())
		{
			System.exit(0);
		}
	}
	
	public void handleMakeEIBData()
	{
		EIBApplicationSelectorDialog asd=new EIBApplicationSelectorDialog(a.getMainFrame(), (EIBDevicesDataModel)a.getBusDeviceDataModel("EIB"));
		asd.setVisible(true);
	}
	
	public void handleConvertVDFile()
	{
		String infile="";
		String outfile;
		JFileChooser fc=new JFileChooser();
		
		fc.setDialogTitle(locale.getString("tit.chooseVDFile"));
		
		fc.setFileFilter(new FileFilter()
		{
			public boolean accept(File f)
			{
				if(f.getName().endsWith(".vd_") || f.isDirectory())
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			public String getDescription()
			{
				return "ETS product data (*.vd_)";
			}
		});
		
		int returnVal = fc.showOpenDialog(a.getMainFrame());

		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			File file = fc.getSelectedFile();
			infile=file.getAbsolutePath();
			logger.debug("vd_ filename: "+infile);
		} 
		else 
		{
			return;
		}
		
		outfile=JOptionPane.showInputDialog(null, locale.getString("mess.inputXMLname"));
		
		if(outfile==null)
		{
			return;
		}
		
		if(!outfile.endsWith(".xml"))
		{
			outfile+=".xml";
		}
		
		outfile=Util.getDataPathPrefix()+"/global-data/eib/devicedata/"+outfile;
		this.a.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		VDConverter converter=new VDConverter(infile, outfile);
		this.a.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void handleOpenTelegramtracer()
	{
		try
		{
			if(this.tracer==null)
			{
				try
				{	
					EIBConnection con=a.getEIBConnection();
					con.connect();
					tracer=new EIBTelegrammtraceframe(con);
				}
				catch(EIBConnectionNotPossibleException npe)
				{
					logger.debug(npe.toString());
					JOptionPane.showMessageDialog(null, locale.getString("mess.noEIBConnection"));
				}
				catch(EIBConnectionNotAvailableException nae)
				{
					logger.debug(nae.toString());
					JOptionPane.showMessageDialog(null, locale.getString("mess.noEIBConnection"));
				}
				
			}
			tracer.setVisible(true);
		}
		catch(EIBGatewayException ge)
		{
			logger.debug(ge);
		}
	}
	
	public void handleOpenEIBFrameDialog()
	{
		if(fd==null)
		{
			try
			{	
				EIBConnection con=a.getEIBConnection();
				con.connect();
				fd=new EIBFrameDialog(a.getEIBConnection());
			}
			catch(EIBConnectionNotPossibleException npe)
			{
				logger.debug(npe.toString());
				JOptionPane.showMessageDialog(null, locale.getString("mess.noEIBConnection"));
			}
			catch(EIBConnectionNotAvailableException nae)
			{
				logger.debug(nae.toString());
				JOptionPane.showMessageDialog(null, locale.getString("mess.noEIBConnection"));
			}
		}
		
		if(fd!=null)
		{
			fd.setVisible(true);
		}
	}
	
	public void handleProgramEIBDevices()
	{
		try
		{
			EIBConnection con=a.getEIBConnection();
			EIBProgrammingDialog dialog=new EIBProgrammingDialog(this.a.getMainFrame(), this.a.getProject(), con);
			dialog.setVisible(true);
		}
		catch(EIBConnectionNotAvailableException nae)
		{
			JOptionPane.showMessageDialog(null, locale.getString("mess.noEIBConnection"));
		}
		catch(EIBConnectionNotPossibleException npe)
		{
			JOptionPane.showMessageDialog(null, locale.getString("mess.noEIBConnection"));
		}
	
	}
	
	public void handleStopEIBGateway()
	{
		try
		{
		
			EIBConnection con=this.a.getEIBConnection();
			con.disconnect();
			// Set Menu entries
			this.a.getMainFrame().enableStopGateway(false);
			this.a.getMainFrame().enableStartGateway(true);
			this.a.getMainFrame().enableGatewaySelectors(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void handleStartEIBGateway()
	{
		EIBConnection con=null;
		try
		{
			con=this.a.getEIBConnection();
			con.connect();
			conn = con;
			this.a.getMainFrame().enableStopGateway(true);
			this.a.getMainFrame().enableStartGateway(false);
			this.a.getMainFrame().enableGatewaySelectors(false);
			
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		
		}
	}
	
	public void handleAbout()
	{
		AboutDialog dialog=new AboutDialog(a.getMainFrame(), locale.getString("mi.about"), true);
		dialog.setVisible(true);
	}

	/**
	 * 
	 */
	public void handleGWDarmstadt()
	{
		this.setEIBGateway("Gateway TU-Darmstadt");
	}

	/**
	 * 
	 */
	public void handleGWLinuxBCU1()
	{
		this.setEIBGateway("Linux BCU1 Connection");		
	}

	/**
	 * switches gateway to KNXnet/IP connection
	 */	
	public void handleGWKNXnetIP()
	{
		this.setEIBGateway("KNXnet/IP Connection");		
	}
	
	private void setEIBGateway(String gwtype)
	{
		EIBConnectionFactory.setConnectorType(gwtype);
		if(this.fd != null && this.fd.isVisible())
		{
			this.fd.setVisible(false);
			this.fd=null;	
		}
		if(this.tracer != null && this.tracer.isVisible())
		{
			this.tracer.setVisible(false);
			this.tracer=null;
		}
		this.a.setEIBConnection(null);
	}
}
