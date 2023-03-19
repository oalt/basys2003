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
package basys.server.servlets;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import basys.LocaleResourceBundle;
import basys.Util;
import basys.XMLLoader;
import basys.datamodels.InstallationDeviceState;
import basys.datamodels.architectural.ArchitecturalDataModel;
import basys.datamodels.installation.InstallationModel;
import basys.eib.EIBAddress;
import basys.eib.EIBConnection;

import basys.eib.EIBConnectionFactory;
import basys.eib.EIBFrame;
import basys.eib.EIBFrameListener;
import basys.eib.EIBGrpaddress;
import basys.eib.EIBPhaddress;
import basys.eib.event.EIBFrameEvent;
import basys.eib.exceptions.EIBAddressFormatException;
import basys.eib.exceptions.EIBConnectionNotAvailableException;
import basys.eib.exceptions.EIBConnectionNotPossibleException;


/**
 * InstallationDeviceControlServlet.java
 * 
 * 
 * @author	oalt
 * @version $Id: InstallationDeviceControlServlet.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 * 
 */
public class InstallationDeviceControlServlet extends HttpServlet implements EIBFrameListener
{
	
	private static final String SERVLETNAME="Control";
	
	public static final int LAMP 			= 1;
	public static final int DIMMABLE_LAMP 	= 2;
	public static final int VALVE			= 3;
	public static final int JALOUSIE		= 4;
	
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	private String project="";
	private ArchitecturalDataModel archmodel=null;
	private InstallationModel imodel=null;
	
	private Vector devstates=new Vector();
	private boolean updateView=true;
	private EIBConnection con=null;
	
	/**
	 * 
	 */
	public InstallationDeviceControlServlet()
	{
		super();
	}
	
	/**
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init()
	{
		
	}
	
	/**
	 * 
	 * @see javax.servlet.Servlet#destroy()
	 */
	public void destroy()
	{
		if(con!=null)
		{
			this.con.disconnect();
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		
		ServletOutputStream out=res.getOutputStream();
		
		if(con==null)
		{
			
			try
			{
				this.con=EIBConnectionFactory.getConnection();
			}
			catch(EIBConnectionNotAvailableException nae)
			{
				//out.print(nae);
				con=null;
			}
		}
		
		con.addEIBFrameListener(this);
		
		try
		{
			con.connect();
		}	
		catch(EIBConnectionNotPossibleException npe)
		{
			// out.print(npe);
		}
		
		String project=req.getParameter("projectfilename");
		
		//out.println(project);
		
		// init datamodels
		if(this.project.equals(project))
		{
			if(imodel==null)
			{
				initModels(project);
			}
		}
		else
		{
			if(project!=null)
			{
				this.project=project;
			}
			initModels(project);
		}
		
		
		String id=req.getParameter("id");
		
		String caption=archmodel.getName(id);
		
		if(id.startsWith("room"))
		{
			res.setContentType("text/html");
		}
		else
		{
			res.setContentType("text/html");
		}
		
		if(req.getParameter("eib")!=null)
		{
			String eib=req.getParameter("eib");
			//out.println(eib);
			String value=req.getParameter("value");
			if(con!=null)
			{
				int[] data=new int[1];
				data[0]=Integer.parseInt(value);
				
				EIBGrpaddress desta=null;
				try
				{
					desta=new EIBGrpaddress(eib);
					EIBPhaddress pa=new EIBPhaddress(1,1,200);
				
	    			EIBFrame ef = new EIBFrame(false, 3, pa, desta, 6, 0, 0x80, data);
					
					con.sendEIBFrame(ef);
					Thread.sleep(250);
				}
				catch(EIBAddressFormatException afe)
				{
				}
				catch(Exception e)
				{
				}
				
				
			}
		}
		
		if(id.startsWith("room"))
		{
			this.printHead(out);
			this.printContent(out, id);
			this.printFooter(out);
			
			/*
			MultipartResponse multi = new MultipartResponse(res);
			this.updateView=true;
			while(true)
			{
				if(this.updateView)
				{
					multi.startResponse("text/html");
					this.printHead(out);
					this.printContent(out, id);
					this.printFooter(out);
					multi.endResponse();
					
					this.updateView=false;
				}	
					
				try
				{
					Thread.sleep(2000);
				}
				catch(InterruptedException ie)
				{
					
				}
			}
			*/
			
		}
		else
		{	
			this.printHead(out);
			this.printContent(out, id);
			this.printFooter(out);
		}
		
	}
	
	private void initModels(String project)
	{
		XMLLoader loader=new XMLLoader(new File(Util.getProjectPathPrefix()+"/"+project+"/installation.xml"));
		this.imodel=new InstallationModel(loader.getDocument());
		
		loader=new XMLLoader(new File(Util.getProjectPathPrefix()+"/"+project+"/structure.xml"));
		this.archmodel=new ArchitecturalDataModel(loader.getDocument());
	}
	
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		 
	}
	
	/**
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void printHead(ServletOutputStream out) throws IOException
	{
		out.println("<html>");
		out.println("<body bgcolor=\"white\">");
		
		out.println("<center><h1>Projekt "+archmodel.getProjectName()+"</h1></center>");
	}
	
	/**
	 * 
	 * @param out
	 * @param id
	 * @throws IOException
	 */
	private void printContent(ServletOutputStream out, String id) throws IOException
	{
		
		out.println("<center>");
		
		
		String descr="";
		if(id.startsWith("project"))
		{
			descr="Gebäude: ";
		}
		else if(id.startsWith("building"))
		{
			descr="Stockwerke: ";
		}
		else if(id.startsWith("floor"))
		{
			descr="Räume: ";
		}
				
		
		out.println("<h2>"+descr+archmodel.getName(id)+"</h2></center>");
		
		if(!con.isConnected())
		{
			out.println("<center><font color=\"red\"><h3>"+locale.getString("mess.noEIBConnection")+"</h3></font></center>");
		}
		
		if(!id.startsWith("project"))
		{
			
			out.println("<a href=\""+SERVLETNAME+"?projectfilename="+this.project+
						"&id="+archmodel.getParentID(id)+"\">Eine Ebene höher</a>");
		}
		
		out.println("<p />");
		
		out.println("<table border=\"1\" width=\"100%\">");
		if(!id.startsWith("room"))
		{
			out.println("<tr><th>Name</th></tr>");
			
			Vector ids=(Vector)archmodel.getChildIDs(id);
			
			for(Enumeration e=ids.elements(); e.hasMoreElements(); )
			{
				String childID=(String)e.nextElement();
					
				out.print("<tr><td><a href=\""+SERVLETNAME+"?projectfilename="+this.project+"&id="+childID+"\">");
				out.print(archmodel.getName(childID)+"</a></td>");
			}
		}
		else
		{
			out.println("<tr><th>Gerätename</th><th>Gerätetyp</th><th>Status</th><th>Funktion</th></tr>");
			this.printRoomDeviceTable(out, id);
		}
		
		out.println("</table>");
	}
	
	private void printRoomDeviceTable(ServletOutputStream out, String roomID) throws IOException
	{
		Node n=archmodel.getDataRootNode(roomID);
		NodeList nl=((Element)n).getElementsByTagName("enddevice");
		
		
		
		String enddeviceid="";
		for(int cnt=0; cnt<nl.getLength(); cnt++)
		{
			
			String type=archmodel.readDOMNodeValue(nl.item(cnt), new StringTokenizer("type", "/"));
			int t=Integer.parseInt(type);
			
			if(t<100)
			{
				
				enddeviceid=archmodel.getEndDeviceID(nl.item(cnt));
				out.print("<tr height=\"50\"");
				
				if(cnt%2==0)
				{
					out.print(" bgcolor=\"#BBBBBB\"");
				}
				else
				{
					out.print(" bgcolor=\"#FFFFFF\"");
				}
								out.println(">");
			
				// Gerätename
				out.println("<td>"+imodel.getName(enddeviceid)+"</td>");
				
				
				
				
				String o="";
				
				
				switch(t)
				{
					case LAMP:
						o=locale.getString("l.lamp");
					break;
					case DIMMABLE_LAMP:
						o=locale.getString("l.dimlamp");
					break;
					case VALVE:
						o=locale.getString("l.valve");
					break;
					case JALOUSIE:
						o=locale.getString("l.jal");
					break;
				}
				out.println("<td><center>"+o+"</center></td>");
			
				/************************/
				
				Vector conns=(Vector)imodel.getConnections(enddeviceid);
				
				o="unknown";
				
				for(Enumeration e=conns.elements(); e.hasMoreElements(); )
				{
					String con=(String)e.nextElement();
					
					String bussystem=imodel.getPropertyByName(enddeviceid, "bussystem");
					if(bussystem != null)
					{
						if(con.startsWith("functiongroup") && bussystem.equals("EIB"))
						{
							String eibid=imodel.getEIBDeviceID(con);
							String eibstate=imodel.getPropertyByName(eibid, "device-state");
							if(eibstate.equals("ready"))
							{
								Vector addrs=(Vector)imodel.getGroupAddresses(con);
								EIBGrpaddress grpaddr=(EIBGrpaddress)addrs.firstElement();
								
								for(Enumeration e2=this.devstates.elements(); e2.hasMoreElements(); )
								{
									InstallationDeviceState state=(InstallationDeviceState)e2.nextElement();
									if(state.getGrpAddr().equals(grpaddr))
									{
										o=state.getState();
										break;
									}
								}
							}
							break;
						}
					}
				}
				
				
				out.println("<td><center><img src=\""+o+".png\" />&nbsp;</center></td>");
					
				if(t==LAMP)
				{	
					
					for(Enumeration e=conns.elements(); e.hasMoreElements(); )
					{
						String con=(String)e.nextElement();
						
						String bussystem=imodel.getPropertyByName(enddeviceid, "bussystem");
						if(bussystem != null)
						{
							if(con.startsWith("functiongroup") && 
							   bussystem.equals("EIB"))
							{
								String eibid=imodel.getEIBDeviceID(con);
								String eibstate=imodel.getPropertyByName(eibid, "device-state");
								if(eibstate.equals("ready") && this.con.isConnected())
								{
									Vector addrs=(Vector)imodel.getGroupAddresses(con);
									
									o="<center>";
									o+="<a href=\""+SERVLETNAME+"?projectfilename="+this.project+"&id="+roomID+
										"&eib="+addrs.firstElement()+"&value=1\">" +										"<img border=\"0\" src=\"on.png\" /> ein</a>";	
								
									o+=" &nbsp;<a href=\""+SERVLETNAME+"?projectfilename="+this.project+"&id="+roomID+
										"&eib="+addrs.firstElement()+"&value=0\">"+
										"<img border=\"0\" src=\"off.png\" /> aus</a>";
									
									o+="</center>";
								}
								else
								{
									o="<center><i>nicht Betriebsbereit</i></center>";
								}
								
								break;
							}
						}
					}
					
					out.println("<td>"+o+"</td>");
				}
				else
				{
				
					out.println("<td></td>");
				}
			
				out.println("</tr>");
			}
		}
	}
	
	private void printFooter(ServletOutputStream out) throws IOException
	{
		out.println("<p />");
		out.println("&copy; 2003 Oliver Alt");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * @see basys.eib.EIBFrameListener#frameReceived(basys.eib.event.EIBFrameEvent)
	 */
	public void frameReceived(EIBFrameEvent efe)
	{
		int apdata[];
    	EIBFrame ef = efe.getEIBFrame();
		
		EIBAddress destaddr=ef.getDestAddress();
		
		if(destaddr instanceof EIBGrpaddress)
		{
	      	if(ef.getAPCI()==0x080)
	      	{
	      		if(ef.getAck()==0xCC)
	        	{
	          		apdata=ef.getApdata();
	          		if(apdata[0]==0)
	          		{
	            		this.updateState((EIBGrpaddress)destaddr, "off");
	          		}
	          		else
	          		{
	            		this.updateState((EIBGrpaddress)destaddr, "on");
	          		}
	        	}
	      	}
	    }
		
	}
	
	private void updateState(EIBGrpaddress grpaddr, String state)
	{	
		for(int cnt=0; cnt < this.devstates.size(); cnt++)
		{
			InstallationDeviceState s=(InstallationDeviceState)this.devstates.get(cnt);
			if(s.getGrpAddr().equals(grpaddr))
			{
				s.setState(state);
				this.updateView=true;
				return;
			}
		}
		
		InstallationDeviceState s2=new InstallationDeviceState();
		s2.setGrpAddr(grpaddr);
		s2.setState(state);
		this.devstates.addElement(s2);
		this.updateView=true;
	}
	
}
