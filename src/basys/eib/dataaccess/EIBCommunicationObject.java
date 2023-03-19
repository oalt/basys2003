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
package basys.eib.dataaccess;

/**
 * EIBCommunicationObject.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBCommunicationObject.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 * 
 */
public class EIBCommunicationObject
{
	
	private String name="";
	private String function="";
	private String readenabled="";
	private String writeenabled="";
	private String commenabled="";
	private String type="";
	private String number=""; 
	private String id="";
	
	/**
	 * 
	 */
	public EIBCommunicationObject()
	{
		super();
	}
	
	public void setNmae(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	public void setFunction(String function)
	{
		this.function = function;
	}
	
	public String getFunction()
	{
		return this.function;
	}
	
	public void setReadEnabled(String readenabled)
	{
		this.readenabled = readenabled;
	}
	
	public String getReadEnabled()
	{
		return this.readenabled;
	}
	
	public void setWriteEnabled(String writeenabled)
	{
		this.writeenabled = writeenabled;
	}
	
	public String getWriteEnabled()
	{
		return this.writeenabled;
	}
	
	public void setComEnabled(String commenabled)
	{
		this.commenabled = commenabled;
	}
	
	public String getComEnabled()
	{
		return this.commenabled;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public void setNumber(String number)
	{
		this.number = number;
	}
	
	public String getNumber()
	{
		return this.number;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getId()
	{
		return this.id;
	}
}
