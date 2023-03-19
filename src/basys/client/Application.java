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
package basys.client;

import java.io.File;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import basys.*;
import basys.XMLLoader;
import basys.client.ui.*;
import basys.client.ui.dialogs.LanguageSelectionDialog;
import basys.datamodels.XMLDataModel;
import basys.datamodels.eib.EIBDevicesDataModel;
import basys.eib.EIBConnection;
import basys.eib.EIBConnectionFactory;
import basys.eib.exceptions.EIBConnectionNotAvailableException;
import basys.eib.exceptions.EIBConnectionNotPossibleException;

/**
 * Application.java
 * 
 * @author 	oalt
 * @version	$Id: Application.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class Application
{
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	private static Logger logger=Logger.getLogger(Application.class);
	
	private Mainframe mainframe;
	private ApplicationEventHandler handler;
	private Project p=null;
	
	// global data models
	private EIBDevicesDataModel eibmodel=null;
	
	private EIBConnection eibconnection=null;
	
	private boolean bufferEmpty=true;
	
	public static Properties appconfig;
	
	/**
	 * Constructor for Application.
	 */
	public Application()
	{
		super();
		
		LocalConfigurator configurator=new LocalConfigurator();
		
		if(!configurator.isValidConfig())
		{
			// show language selector
			LanguageSelectionDialog lsdia=new LanguageSelectionDialog();
			lsdia.setVisible(true);

			configurator.createConfig(lsdia.getLocaleString());
			JOptionPane.showMessageDialog(null, locale.getString("mess.welcome")+configurator.getPathInfo());	
		}
		
		appconfig=configurator.loadConfig();
		
		String lang=appconfig.getProperty("language");
		logger.info("Language="+lang);
		
		// set language
		LocaleResourceBundle.setLocale(lang);
		
		logger.info("os.name="+System.getProperty("os.name"));
		logger.info("os.arch="+System.getProperty("os.arch"));
		logger.info("os.version="+System.getProperty("os.version"));
		logger.info("user.home="+System.getProperty("user.home"));
		
		this.handler=new ApplicationEventHandler(this);
		this.mainframe = new Mainframe(handler);
	}
	
	
	
	/**
	 * 
	 */
	public Mainframe getMainFrame()
	{
		return this.mainframe;	
	}
	
	public void setProject(Project p)
	{
		this.p = p;
	}
	
	/**
	 * Returns actual opened project.
	 * 
	 * @return actual open project
	 */
	public Project getProject()
	{
		return this.p;
	}
	
	/**
	 * 
	 * @param bussystem
	 * @return
	 */
	public XMLDataModel getBusDeviceDataModel(String bussystem)
	{
		if(bussystem.equals("EIB"))
		{
			return this.getEIBDeviceDateModel();
		}
		
		return null;
	}
	
	private EIBDevicesDataModel getEIBDeviceDateModel()
	{
		if(this.eibmodel==null)	// model not yet initialized
		{
			File eibdev=new File(Util.getDataPathPrefix()+"/global-data/eib/eib-devices.xml");
			if(!eibdev.exists())	// in case of no file, create new model
			{
				this.eibmodel=new EIBDevicesDataModel();
			}
			else	// load existing file
			{
				XMLLoader loader=new XMLLoader(eibdev);
				this.eibmodel=new EIBDevicesDataModel(loader.getDocument());
			}
		}
		return this.eibmodel;
	}
	
	/**
	 * Returns the EIB device data model.
	 * 
	 * @param eibmodel EIB device data model
	 */
	public void setEIBDeviceDataModel(EIBDevicesDataModel eibmodel)
	{
		this.eibmodel = eibmodel;
	}
	
	public EIBConnection getEIBConnection() throws EIBConnectionNotAvailableException, EIBConnectionNotPossibleException
	{
		//EIBConnectionFactory.setConnectorType("Linux BCU1 Connection");
		if(this.eibconnection==null)
		{
			try
			{
				this.eibconnection=EIBConnectionFactory.getConnection();
			}
			catch(EIBConnectionNotAvailableException nae)
			{
				//nae.printStackTrace();
				throw new EIBConnectionNotAvailableException();
			}
			
			
		}
		
		try
		{
			this.eibconnection.connect();
		}
		catch(EIBConnectionNotPossibleException npe)
		{
			throw npe;
		}
		
		return this.eibconnection;
	}
	
	public void setEIBConnection(EIBConnection con)
	{
		this.eibconnection=con;
	}
	
	public boolean isBufferEmpty()
	{
		return this.bufferEmpty;
	}
	
	/**
	 * Main method for the BASys application.
	 * 
	 * @param args unused.
	 */
	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		try
        {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
		Application app=new Application();
		
		//Thread t=new Thread(new StartupScreen(app.getMainFrame()));
        //t.run();
		app.getMainFrame().setVisible(true);
	}
}
