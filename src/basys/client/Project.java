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
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import basys.XMLLoader;
import basys.client.commands.Command;
import basys.datamodels.installation.InstallationModel;
import basys.datamodels.architectural.ArchitecturalDataModel;

/**
 * Project.java
 * 
 * Main data class for holding project data at runtime.
 * 
 * @author	oalt
 * @version $Id: Project.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class Project implements Observer
{	
	
	private boolean modified=false;
	
	private Application parent;
	private ArchitecturalDataModel archmodel=null;
	private InstallationModel instamodell=null;
	
	private Vector commandList=new Vector();
	
	public Project(Application parent)
	{
		
		this(parent, "Neues Projekt"); 
	}
	 
	public Project(Application parent, String projectname)
	{
		this.parent=parent;
		
		archmodel=new ArchitecturalDataModel(projectname);
		instamodell=new InstallationModel();
		
		archmodel.addObserver(this);
		instamodell.addObserver(this);
	}
	
	public Project(Application parent, File projectpath)
	{
		this.parent=parent;
		
		XMLLoader load1=new XMLLoader(new File(projectpath.getPath()+"/structure.xml"));
		archmodel=new ArchitecturalDataModel(load1.getDocument());
		
		XMLLoader load2=new XMLLoader(new File(projectpath.getPath()+"/installation.xml"));
		instamodell=new InstallationModel(load2.getDocument());
		
		archmodel.addObserver(this);
		instamodell.addObserver(this);
	}
	
	public void setApplication(Application parent)
	{
		this.parent = parent;
	}
	
	public Application getApplication()
	{
		return this.parent;
	}
	
	public void setProjectName(String projectName)
	{
		this.archmodel.setProjectName(projectName);
	}
	
	public String getProjectName()
	{
		return this.archmodel.getProjectName();
	}
	
	public String getProjectDirectoryName()
	{
		return this.archmodel.getProjectDirectoryName();
	}
	
	public void setPrefferesBusSystem(String prefferedBusSystem)
	{
		this.archmodel.setPrefferedBusSystem(prefferedBusSystem);
	}
	
	public String getPrefferedBusSystem()
	{
		return this.archmodel.getPrefferedBusSystem();
	}
	
	public ArchitecturalDataModel getArchitecturalDataModel()
	{
		return this.archmodel;
	}
	
	public InstallationModel getInstallationModel()
	{
		return this.instamodell;
	}

	public boolean isModified()
	{
		return this.modified;
	}
	
	public void setModified(boolean b)
	{
		this.modified=b;
	}
	
	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg)
	{
		this.setModified(true);
	}
	
	public void addCommand(Command c)
	{
		this.commandList.addElement(c);
	}
}

