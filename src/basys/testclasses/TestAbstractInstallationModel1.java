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
package basys.testclasses;

import java.io.File;

import basys.XMLSaver;
import basys.datamodels.installation.InstallationModel;

import org.apache.log4j.*;

/**
 * TestAbstractInstallationModel1.java
 * 
 * @author 	oalt
 * @version	$Id: TestAbstractInstallationModel1.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 */
public class TestAbstractInstallationModel1
{
	private static Logger logger=Logger.getLogger(InstallationModel.class);
	
	/**
	 * Constructor for TestAbstractInstallationModel1.
	 */
	public TestAbstractInstallationModel1()
	{
		/*
		super();
		InstallationModel model = new InstallationModel();
		
		XMLSaver saver=new XMLSaver();
		
		String sid=model.addSensor("neuer Sensor");
		String aid=model.addActor("neuer Aktor");
		String pid=model.addPassiveComponent("neue passive Komponente");
		model.addSensor("neuer Sensor");
		model.setProperty(sid,"test","hallo 233");
		
		model.setProperty(sid,"test", "1234");
		model.setProperty(sid,"test2","hallo 233");
		
		logger.debug("Property test: "+model.getPropertyByName(sid, "test"));
		logger.debug("Property unknown: "+model.getPropertyByName(sid, "unknown"));
		
		logger.debug("Property test: "+model.getPropertyByName(aid, "test"));
		model.setProperty(aid, "test", "test");
		logger.debug("Property test: "+model.getPropertyByName(aid, "test"));
		
		model.addConnection(sid, aid);
		model.addConnection(sid, aid);
		
		model.removeConnection(sid, aid);
		
		model.addConnection(sid, aid);
		model.addConnection(sid, pid);
		//model.removeAllConnections(sid);
				
		saver.saveDocument(model.getDocumnet(), new File("/homes/oalt/datatest.xml"));
		*/	
	}

	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		new TestAbstractInstallationModel1();
	}
}
