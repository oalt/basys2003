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
package basys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * LocalConfigurator.java
 * 
 * 
 * @author	olli
 * @version $Id: LocalConfigurator.java,v 1.1 2004/01/14 21:38:39 oalt Exp $
 * 
 */
public class LocalConfigurator
{
	
	private static final String DATADIRNAME = "basys";
	private static final String CONFIGFILENAME = "basys.properties";
	private static final String PROJECTDIR = "projects";
	private static final String GLOBALDIR  = "global-data";
	private static final String EIBDIR = "eib";
	private static final String DEVICEDATA = "devicedata";
	private static final String HELPDIR = "help";
	 
	/**
	 * 
	 */
	public LocalConfigurator()
	{
		super();
	}
	
	
	public boolean isValidConfig()
	{
		String home=System.getProperty("user.home");
		
		File configfile=new File(home+"/"+DATADIRNAME+"/"+CONFIGFILENAME);
		if(configfile.exists())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public void createConfig(String language)
	{
		File configdir=new File(System.getProperty("user.home")+"/"+DATADIRNAME);
		if(!configdir.exists())
		{
			configdir.mkdir();
			File projectdir=new File(System.getProperty("user.home")+"/"+DATADIRNAME+"/"+PROJECTDIR);
			projectdir.mkdir();
			File globaldir=new File(System.getProperty("user.home")+"/"+DATADIRNAME+"/"+GLOBALDIR);
			globaldir.mkdir();
			File eibdir=new File(System.getProperty("user.home")+"/"+DATADIRNAME+"/"+GLOBALDIR+"/"+EIBDIR);
			eibdir.mkdir();
			File eibdevicedir=new File(System.getProperty("user.home")+"/"+DATADIRNAME+"/"+GLOBALDIR+"/"+EIBDIR+"/"+DEVICEDATA);
			eibdevicedir.mkdir();
			File helpdir=new File(System.getProperty("user.home")+"/"+DATADIRNAME+"/"+GLOBALDIR+"/"+HELPDIR);
			helpdir.mkdir();
		}
		Properties p=new Properties();
		p.put("language", language);
		p.put("basysdir", DATADIRNAME);
		p.put("projectdir", PROJECTDIR);
		p.put("globaldir", GLOBALDIR);
		p.put("eibdir", GLOBALDIR+"/"+EIBDIR);
		p.put("eibdevices", GLOBALDIR+"/"+EIBDIR+"/"+DEVICEDATA);
		p.put("helpdir", GLOBALDIR+"/"+HELPDIR);
		
		File propfile=new File(System.getProperty("user.home") +"/"+DATADIRNAME+"/"+CONFIGFILENAME);
		
		try
		{
			FileOutputStream out=new FileOutputStream(propfile);
			p.save(out, "BASys 2003 Configuration");
			out.close();
		}
		catch(IOException ioe)
		{
			
		}
		
	}
	
	public Properties loadConfig()
	{
		Properties p = new Properties();
		try
    	{	
			FileInputStream propFile = new FileInputStream(System.getProperty("user.home") +"/"+DATADIRNAME+"/"+CONFIGFILENAME);
		
			p.load(propFile);
    	}
    	catch(IOException ioe)
    	{
    		
    	}
    	return p;
	}
	
	public String getPathInfo()
	{
		return System.getProperty("user.home") +"/"+DATADIRNAME;
	}
}
