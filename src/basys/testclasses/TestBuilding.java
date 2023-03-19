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

import basys.*;
import basys.datamodels.*;
import basys.datamodels.architectural.*;
import java.util.*;
import java.io.*;

import org.apache.log4j.*;

/**
 * TestBuilding.java
 * 
 * @author 	oalt
 * @version	$Id: TestBuilding.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 */
public class TestBuilding implements Observer
{
	private static Logger logger=Logger.getLogger(TestBuilding.class);
	//Building b;
	
	/**
	 * Constructor for TestBuilding.
	 */
	public TestBuilding()
	{
		super();
		logger.info("Start Test.");
		File f=new File("/homes/oalt/work/oalt/workspace/basys/testdata/arch.xml");
		XMLLoader xl=new XMLLoader(f);			
		
		ArchitecturalDataModel model = new ArchitecturalDataModel(xl.getDocument());
		
		model.addObserver(this);

/*		
		try
		{
			this.b=model.getBuilding("b-01");
			logger.info(b.toString());
			b.setName("neuer Name");
			b.setAddress("Neue Adresse");
		}
		catch(DataObjectNotFoundException wxde)
		{
			wxde.printStackTrace();	
		}
		
		Vector v=(Vector) model.getAllBuildings();
		for(Enumeration e=v.elements(); e.hasMoreElements();)
		{
			logger.info(((Building) e.nextElement()).toString());	
		}
*/
		
		File outfile=new File("/homes/oalt/work/oalt/workspace/basys/testdata/out.xml");
		
		XMLSaver save=new XMLSaver();
		save.saveDocument(model.getDocumnet(), outfile);
		//JunctionBox = new JunctionBox();
		
		model.deleteObserver(this);
		
		ArchitecturalDataModel model2=new ArchitecturalDataModel("MyProject");
		
		model2.addObserver(this);
		
		/*
		Project p=(Project) model2.getStandardDataModel(model.getRootID());
		String childID=model2.addChild(p.getId());
		
		Building building=(Building)model2.getStandardDataModel(childID);
		building.setName("Mein Gebaeude");
		building.setComment("Kein Kommentar !!");
		building.setAddress("Lacheweg 21");
		
		String floorID=model2.addChild(building.getId());
		String floor2ID=model2.addChild(building.getId());
		
		String building2=model2.addChild(p.getId());
		Building b2=(Building)model2.getStandardDataModel(building2);
		
		
		b2.setAddress("Treitelstrasse 3, 1010 Wien");
		model2.addChild(building2);
		
		save.saveDocument(model2.getDocumnet(), new File("/homes/oalt/work/oalt/workspace/basys/testdata/out2.xml"));
		
		model2.removeData("floor-2");
		
		save.saveDocument(model2.getDocumnet(), new File("/homes/oalt/work/oalt/workspace/basys/testdata/out3.xml"));
		*/		
	}

	/**
	 * @see java.util.Observer#update(Observable, Object)
	 */
	public void update(Observable o, Object arg)
	{
		logger.info("update called.\n");
	}
	
	public static void main(String[] args)
	{
		// Set up a simple configuration that logs on the console.
     	BasicConfigurator.configure();	
     	new TestBuilding();
	}
}
