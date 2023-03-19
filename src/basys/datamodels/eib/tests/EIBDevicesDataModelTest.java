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
package basys.datamodels.eib.tests;

import java.io.File;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import basys.XMLSaver;
import basys.datamodels.eib.EIBDevicesDataModel;
import basys.eib.EIBApplication;
import basys.eib.EIBDeviceFunction;

/**
 * TestEIBDeviceDataModel.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBDevicesDataModelTest.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EIBDevicesDataModelTest extends TestCase
{
	private static Logger logger=Logger.getLogger(EIBDevicesDataModelTest.class);

	/**
	 * 
	 */
	public EIBDevicesDataModelTest()
	{
		super();
	}

	public void testEIBDevicesDataModel()
	{
		logger.info("Testing Data model functions.");
		
		XMLSaver saver=new XMLSaver();
		
		EIBDevicesDataModel model = new EIBDevicesDataModel();
		
		String id1=model.addDevice("4-fach Schaltaktor");
		String id2=model.addDevice("2-fach Schaltaktor");
		
		
		
		model.setApplication(id1, new EIBApplication());
		
		model.setManufacturer(id1, "ABB");
		model.setInstallationType(id1, "REG");
		
		String manu=model.getManufacturerName(id1);
		
		String fgid1=model.addFunctionGroup(id1);
		String fgid2=model.addFunctionGroup(id2);
		
		
		model.addFunction(fgid2, new EIBDeviceFunction("Ausgang 1", "switching", 0, 0));
		
		model.addFunction(fgid1, new EIBDeviceFunction("Ausgang 2", "switching", 1, 2));
		model.addFunction(fgid1, new EIBDeviceFunction("Ausgang 3", "switching", 1, 2));
		
		String es=model.readDOMNodeValue((Element)model.getDataRootNode(id1), new StringTokenizer("application/eeprom-start","/"));
		
		logger.debug("eeprom-start: "+es);
		
		saver.saveDocument(model.getDocumnet(), new File("/homes/oalt/eib-devices.xml"));
		
		assertTrue(manu.equals("ABB"));
		
	
	}

}
