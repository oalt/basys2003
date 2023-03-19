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
package basys.eib.dataaccess.vdconvert;

import java.io.*;
import java.util.Vector;

import org.apache.log4j.*;

/**
 * VDConverter.java
 * 
 * Data converter for ETS vd_ files. 
 * This class implements the vd_ to xml converter for the ETS device/product datas.
 * 
 * @author 	oalt
 * @version	$Id: VDConverter.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 */
public class VDConverter
{
	private static Logger logger=Logger.getLogger(VDConverter.class);
	
	private LineNumberReader br;
	
	private String line;
	private String next;
	private String data;
	
	boolean read_caption=false;
	boolean read_record=false;
	boolean read_table=false;
	boolean read_value=false;
	
	String[] atmp;
	String recordname=null;
	
	int val_count=0;
	
	Vector captions=new Vector();
	
	FileWriter out;
	
	/**
	 * Constructor for VDConverter.
	 */
	public VDConverter(String infilename, String outfilename)
	{
		super();
		convertToXML(infilename, outfilename);
	}

	/**
	 * 
	 */
	public void convertToXML(String infilename, String outfilename)
	{
		
		boolean parse=false;
			
		try
		{
			out=new FileWriter(outfilename);
			
			br=new LineNumberReader(new FileReader(infilename));
		
			line = br.readLine();
			
			out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
			out.write("<eib-products>\n");
			if(!line.equals("EX-IM"))
			{
				logger.info("Wrong file format.");	
				System.exit(1);
			}
			
			line=br.readLine();
			
			while(true)
			{
					
				next=br.readLine();
				
				if(next == null)
				{
					if(line!=null)
					{
						parseLine(line);	
					}
					
					break;	
				}
				
				while(next.startsWith("\\\\"))
				{
					line=line + next.substring(2);
					next=br.readLine();	
					if(next==null)
					{
						
						if(line!=null)
						{
							parseLine(line);	
						}
						return;	
						
					}
				}
				parseLine(line);
				line=next;
			}			
			
			out.flush();
			out.close();	
				
			
		
		}
		catch(IOException ioe)
		{
			
		}
		catch(Exception e)
		{
			logger.debug("Error in line: "+br.getLineNumber()+" "+line);
			e.printStackTrace();	
			try
			{
				this.out.flush();
				out.close();
			}
			catch(IOException ioe)
			{
				
			}
		} 	
	}

	
	private void parseLine(String line) throws Exception
	{
		String tmp="";
		
		
		try
		{
		
			char start=' ';
			
			if(line.length()>0)
			{
				start=line.charAt(0);
				if(line.charAt(0)=='T' && line.charAt(1)!=' ')
				{
					start=' ';	
				}
			}
			else
			{
				line="";	
			}
			// System.err.println(br.getLineNumber()+": "+line);
			if(line.startsWith("N "))
			{	// name
				//logger.info("read name. "+next);
			}
			else if(line.startsWith("K "))
			{
				//logger.info("read K.");
			}
			else if(line.startsWith("D ")) //date
			{
				//logger.info("read date.");
			}
			else if(line.startsWith("V "))
			{
				//logger.info("read version.");
			}
			else if(line.startsWith("H "))
			{
				//logger.info("read H.");
			}
			else if(line.startsWith("-"))	// block end
			{
				//logger.info("read block end.");
				this.read_caption=false;
				this.read_record=false;
				this.read_table=false;
				this.read_value=false;	
			}				
			else if(line.startsWith("T "))
			{
				String[] linedata=line.split(" ");
				try
				{
					Integer.parseInt(linedata[1]);
					
			    	// table caption
			    	captions=new Vector();
					//logger.info("read Table.");
					this.read_table=true;
				}
				catch(NumberFormatException nfe)
				{
					// not a number	
					read_value=true;
					read_record=false;
				}
			}
				
			else if(line.startsWith("C"))	// table captions
			{
				if(this.read_table)
				{
					//logger.info("read Caption.");
					//this.read_table=false;
					
					if(!this.read_caption)
					{
						this.read_caption=true;	
					}
				}
			}
			else if(line.startsWith("R "))	// record
			{
				//logger.info("read Record.");
				this.read_value=false;
				this.read_caption=false;
				this.read_record=true;
				this.read_table=false;
			}
			else if(line.startsWith("XXX"))
			{
				//logger.debug("XXX match");
				out.write("\t</"+recordname+">");
				out.write("</eib-products>");
				out.flush();
				return;	
			}
			else
			{
				read_value=true;
				read_record=false;
			}
			
			
			if(read_table)
			{
				//
			}
			if(read_record)
			{
				atmp=line.split(" ");
				val_count=0;
				//System.out.println(atmp[4]+":");	
				if(recordname!=null)
				{
					out.write("\t</"+recordname+">\n");
				}
				recordname=atmp[4];
				out.write("\t<"+recordname+">\n");
			}
			if(read_caption)
			{
				atmp=line.split(" ");
				captions.addElement(atmp[5]);
				//logger.debug("Adding name to vector: "+atmp[5]);
			}
			if(read_value)
			{
				//System.out.println("<"+captions.elementAt(val_count)+">"+line+"<"+captions.elementAt(val_count)+">" );
				if(!line.equals(""))
				{
					line=line.replaceAll("<","&lt;");
					line=line.replaceAll(">","&gt;");
					line=line.replaceAll("&", "&amp;");
					out.write("\t\t<"+captions.elementAt(val_count)+">"+line+"</"+captions.elementAt(val_count)+">\n");
				}
				val_count++;
			}	
			line="";				
		}
		catch(IOException ioe)
		{
			
		}
		catch(Exception e)
		{
			throw e;	
		}
				
	}
	
	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		new VDConverter("/home/oalt/vds/bje.vd_", "/home/oalt/vds/bje.xml");
	}
}
