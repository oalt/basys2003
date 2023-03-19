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
package basys.datamodels.architectural;

import basys.client.ListEntry;
import basys.datamodels.*;

import java.awt.Point;
import java.util.*;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

/**
 * ArchitecturalDataModel.java
 * 
 * @author 	oalt
 * @version	$Id: ArchitecturalDataModel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class ArchitecturalDataModel extends XMLDataModel
{
	private static Logger logger=Logger.getLogger(ArchitecturalDataModel.class);
	
	private static final String ROOT_ID="project-1";
	public static final String PROJECT_PREFIX="project";
	public static final String BUILDING_PREFIX="building";
	public static final String FLOOR_PREFIX="floor";
	public static final String ROOM_PREFIX="room";
	public static final String JUNCTION_BOX_PREFIX="junctionbox";
	
	/**
	 * Default Constructor.
	 */
	public ArchitecturalDataModel(String projectname)
	{
		super();
		Document document=this.getDocumnet();
		Node archNode=document.appendChild(document.createElement("architectural-data"));
		Node pNode=archNode.appendChild(document.createElement("project"));
		
		((Element)pNode).setAttribute("id", this.ROOT_ID);
		pNode.appendChild(document.createElement("name")).appendChild(document.createTextNode(projectname));
		pNode.appendChild(document.createElement("comment"));
		pNode.appendChild(document.createElement("directoryname")).appendChild(document.createTextNode(this.makeProjectDirectoryName()));
		pNode.appendChild(document.createElement("preffered-bus-system"));
	}
	
	public ArchitecturalDataModel(Document doc)
	{
		super(doc);	
	}

	/**
	 * Returns the data model root node ID.
	 */	
	public String getRootID()
	{
		return this.ROOT_ID;	
	}
	
	public Collection getChildIDs(String parentID)
	{
		if(parentID.startsWith(this.PROJECT_PREFIX))
		{
			return this.getIDList(this.getDataRootNode(parentID), this.BUILDING_PREFIX);	
		}
		else if(parentID.startsWith(this.BUILDING_PREFIX))
		{
			return this.getIDList(this.getDataRootNode(parentID), this.FLOOR_PREFIX);
		}
		else if(parentID.startsWith(this.FLOOR_PREFIX))
		{
			return this.getIDList(this.getDataRootNode(parentID), this.ROOM_PREFIX);
		}
		else if(parentID.startsWith(this.BUILDING_PREFIX))
		{
			return this.getIDList(this.getDataRootNode(parentID), this.FLOOR_PREFIX);
		}
		else if(parentID.startsWith(this.FLOOR_PREFIX))
		{
			return this.getIDList(this.getDataRootNode(parentID), this.ROOM_PREFIX);
		}
		else if(parentID.startsWith(this.ROOM_PREFIX))
		{
			return this.getIDList(this.getDataRootNode(parentID), this.JUNCTION_BOX_PREFIX);
		}	
		else
		{
			return null;	
		}
	}
	
	public String getParentID(String childID)
	{
		if(!childID.startsWith(PROJECT_PREFIX))
		{
			Element parent=(Element)this.getDataRootNode(childID).getParentNode();
			return parent.getAttribute("id");
		}
		return "";
	}
	
	/**
	 * 
	 */
	public String addChild(String parentID)
	{
		Node parent = this.getDataRootNode(parentID);
		if(parentID.startsWith(this.PROJECT_PREFIX))
		{
			Element e=this.createCild(this.BUILDING_PREFIX);
			parent.appendChild(e);
			this.setChanged();
			this.notifyObservers("new-"+parentID);
			return this.getChildID(e);		
		}
		else if(parentID.startsWith(this.BUILDING_PREFIX))
		{
			Element e=this.createCild(this.FLOOR_PREFIX);
			parent.appendChild(e);
			this.setChanged();
			this.notifyObservers("new-"+parentID);
			return this.getChildID(e);		
		}
		if(parentID.startsWith(this.FLOOR_PREFIX))
		{
			Element e=this.createCild(this.ROOM_PREFIX);
			parent.appendChild(e);
			this.setChanged();
			this.notifyObservers("new-"+parentID);
			return this.getChildID(e);		
		}
		if(parentID.startsWith(this.ROOM_PREFIX))
		{
			Element e=this.createCild(this.JUNCTION_BOX_PREFIX);
			parent.appendChild(e);
			this.setChanged();
			this.notifyObservers("new-"+parentID);
			return this.getChildID(e);		
		}
		else
		{
			return null;	
		}		
	}	
	
	public void addEndDevice( String roomId, String enddevId, Point p, int devType)
	{
		Node n=this.getDataRootNode(roomId);
		
		Document doc=n.getOwnerDocument();
		
		Element ed=n.getOwnerDocument().createElement("enddevice");
		
		ed.setAttribute("id", enddevId);
		
		//this.writeDOMNodeValue(ed, new StringTokenizer("location/point/x","/"), ""+p.getX());
		//this.writeDOMNodeValue(ed, new StringTokenizer("location/point/y","/"), ""+p.getY());
		this.writeDOMNodeValue(ed, new StringTokenizer("type", "/"), devType+"");
		
		n.appendChild(ed);
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public String getEndDeviceID(Node endDeviceNode)
	{
		return ((Element)endDeviceNode).getAttribute("id");
	}
	
	/**
	 * Add bus device link to installation location (Room or Junction Box).
	 * @param installationLocationId room or junction box id.
	 * @param devId device id in installation.xml
	 */
	public void addBusDevice(String installationLocationId, String devId)
	{
		Node n=this.getDataRootNode(installationLocationId);
		
		Document doc=n.getOwnerDocument();
		
		Element ed=n.getOwnerDocument().createElement("busdevice");
		
		ed.appendChild(doc.createTextNode(devId));
		
		n.appendChild(ed);
		
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Returns ids of all installed bus devices in the given installation location.
	 *  
	 * @param installationLocationID id of the installation location to search for devices
	 * @return ids of matching bus devices.
	 */
	public Collection getBusDeviceIDs(String installationLocationID)
	{
		logger.debug("installationLocationID: "+installationLocationID);
		
		Element e=(Element)this.getDataRootNode(installationLocationID);
		NodeList nl=e.getElementsByTagName("busdevice");
		
		Vector v=new Vector();
		for(int cnt=0; cnt<nl.getLength(); cnt++)
		{
			Element node=(Element)nl.item(cnt);
			v.addElement(node.getFirstChild().getNodeValue());
			logger.debug("Found id: "+node.getFirstChild().getNodeValue());
		}
		return v;
	}
	
	public Vector getRoomGeometry(String roomID)
	{
		Vector g=new Vector();
		
		
		
		return g;
	}
	
	public String getInstallationLocationName(String busdeviceID)
	{
		String name="";
		NodeList nl=this.getDocumnet().getElementsByTagName("busdevice");
		Node n=null;
		
		for(int cnt=0; cnt < nl.getLength(); cnt++)
		{
			String con=null;
			try
			{
				con=nl.item(cnt).getFirstChild().getNodeValue();
				//logger.debug("Connection: "+con);
				if(con.equals(busdeviceID))
				{
					n=nl.item(cnt).getParentNode();
					break;
				}
			}
			catch(NullPointerException npe)
			{
				npe.printStackTrace();
			}
			
		
		}
		
		if(n!=null)
		{
		
			name=this.readDOMNodeValue(n, new StringTokenizer("name", "/"));
		}
		
		return name;
	}
	
	public String getProjectName()
	{
		return this.getName(ROOT_ID);
	}
	
	public void setProjectName(String name)
	{
		this.setName(ROOT_ID, name);
	}
	
	public String getPrefferedBusSystem()
	{
		Node n=this.getDataRootNode(ROOT_ID);
		return this.readDOMNodeValue(n, new StringTokenizer("preffered-bus-system","/"));
	}
	
	public void setPrefferedBusSystem(String pbs)
	{
		Node n=this.getDataRootNode(ROOT_ID);
		this.writeDOMNodeValue(n, new StringTokenizer("preffered-bus-system", "/"), pbs);
	}
	
	public Vector getJunctionBoxList()
	{
		Vector ids=(Vector)this.getIDList(this.getDataRootNode(ROOT_ID), JUNCTION_BOX_PREFIX);
		
		Vector list=new Vector();
		
		for(Enumeration e=ids.elements(); e.hasMoreElements(); )
		{
			String id=(String)e.nextElement();
			
			ListEntry le=new ListEntry(this.getName(id), id);
			list.addElement(le);
		}
		
		return list;
	}
	
	public String getProjectDirectoryName()
	{
		Node n=this.getDataRootNode(ROOT_ID);
		return this.readDOMNodeValue(n, new StringTokenizer("directoryname","/"));
	}
	
	private String makeProjectDirectoryName()
	{
		String name=this.getProjectName();
		name=name.replaceAll(" ", "_");
		name=name.replaceAll("ä","ae");
		name=name.replaceAll("ö", "oe");
		name=name.replaceAll("ü","ue");
	
		return name;
	}
	
}
