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

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;



/**
 * LocaleResourceBundle.java
 * 
 * @author 	oalt
 * @version	$Id: LocaleResourceBundle.java,v 1.1 2004/01/14 21:38:39 oalt Exp $
 */
public class LocaleResourceBundle
{
	private static Logger logger=Logger.getLogger(LocaleResourceBundle.class);
	
	private static ResourceBundle locale = null;
	
	/**
	 * Constructor for LocaleResourceBundle.
	 */
	private LocaleResourceBundle()
	{
		super();
		//Locale currentLocale = new Locale("en", "EN");
		Locale currentLocale = new Locale("de", "DE");
		locale = ResourceBundle.getBundle("Locale", currentLocale);
	}

	public static ResourceBundle getLocale()
	{
		if(locale == null)
		{
			new LocaleResourceBundle();	
		}
		
		return locale;	
		
	}
	
	/**
	 * Set cuurent locale.
	 * 
	 * @param l Locate to set.
	 */
	public static void setLocale(Locale l)
	{
		locale = ResourceBundle.getBundle("Locale", l);
	}
	
	public static void setLocale(String l)
	{
		if(l.startsWith("de"))
		{
			locale = ResourceBundle.getBundle("Locale", new Locale("de", "DE"));
		}
		else if(l.startsWith("en"))
		{
			locale = ResourceBundle.getBundle("Locale", new Locale("en", "EN"));
			logger.debug("Set language english");
		}
		
		
	}

}
