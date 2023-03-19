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
package basys.datamodels.installation;

import java.util.*;


import org.apache.log4j.Logger;
import org.w3c.dom.*;

import basys.client.ListEntry;
import basys.datamodels.XMLDataModel;
import basys.eib.EIBDeviceFunction;
import basys.eib.EIBGrpaddress;
import basys.eib.EIBPhaddress;


import org.w3c.dom.Document;

/**
 * InstallationModel.java
 * 
 * @author 	oalt
 * @version	$Id: InstallationModel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class InstallationModel extends XMLDataModel
{
	private static Logger logger=Logger.getLogger(InstallationModel.class);
		
	private Node sensorsNode = null;
	private Node actuatorsNode  = null;
	private Node passiveNode = null;
	
	/**
	 * Constructor for InstallationModel.
	 */
	public InstallationModel()
	{
		super();
		Document document=this.getDocumnet();
		
		// create empty new document
		Node aNode=document.appendChild(document.createElement("installation-components"));
		Node activeNode=aNode.appendChild(document.createElement("active-components"));
		this.sensorsNode=activeNode.appendChild(document.createElement("sensors"));
		this.actuatorsNode=activeNode.appendChild(document.createElement("actuators"));
		this.passiveNode=aNode.appendChild(document.createElement("passive-components"));
		
	}

	/**
	 * Constructor for InstallationModel.
	 * @param doc
	 */
	public InstallationModel(Document doc)
	{
		super(doc);
		this.actuatorsNode=doc.getElementsByTagName("actuators").item(0);
		this.sensorsNode=doc.getElementsByTagName("sensors").item(0);
		this.passiveNode=doc.getElementsByTagName("passive-components").item(0);
	}
	
	/**
	 * Add new Sensor
	 */
	public String addSensor()
	{
		Element e=this.createCild("sensor");
		e.appendChild(this.getDocumnet().createElement("sensor-type"));
		e.appendChild(this.getDocumnet().createElement("properties"));
		e.appendChild(this.getDocumnet().createElement("connections"));
		this.sensorsNode.appendChild(e);
		return this.getChildID(e);
	}
	
	/**
	 * 
	 */
	public String addSensor(String name)
	{
		String id=this.addSensor();
		this.setName(id, name);	
		return id;
	}
	
	/**
	 * Add new Actuator
	 */
	public String addActuator()
	{
		Element e=this.createCild("actuator");
		//e.appendChild(this.getDocumnet().createElement("actuator-type"));
		e.appendChild(this.getDocumnet().createElement("properties"));
		e.appendChild(this.getDocumnet().createElement("connections"));
		this.actuatorsNode.appendChild(e);
		return this.getChildID(e);
	}
	
	/**
	 * Add new actuator.
	 * @param name Name of the actor
	 */
	public String addActuator(String name)
	{
		String id=this.addActuator();
		this.setName(id, name);	
		return id;
	}
	
	public String addFunctionGroup(String parentID)
	{
		Element e=this.createCild("functiongroup");
		this.getDataRootNode(parentID).appendChild(e);
		return this.getChildID(e);
	}
	
	public String addFunction(String functionGroupID)
	{
		Element e=this.createCild("function");
		this.getDataRootNode(functionGroupID).appendChild(e);
		return this.getChildID(e);
	}
	
	
	
	/**
	 * Add new passive component
	 */
	public String addPassiveComponent()
	{
		Element e=this.createCild("passive");
		e.appendChild(this.getDocumnet().createElement("passive-type"));
		e.appendChild(this.getDocumnet().createElement("properties"));
		e.appendChild(this.getDocumnet().createElement("connections"));
		this.passiveNode.appendChild(e);
		return this.getChildID(e);
	}
	
	/**
	 * Add passive omponent
	 * @param name Passive component name
	 */
	public String addPassiveComponent(String name)
	{
		String id=this.addPassiveComponent();
		this.setName(id, name);
		return id;
	}
	
	public Collection getInstallationDeviceIDs()
	{
		
		Vector list=(Vector)this.getIDList(this.passiveNode, "passive");
	
		Vector cleanedList=new Vector();
		for(int cnt=0; cnt < list.size(); cnt++)
		{
			String id=(String)list.get(cnt);
			int type=Integer.parseInt( this.getPropertyByName(id, "type"));
			if(type<100)
			{
				cleanedList.add(id);
			}
		}
		
		return cleanedList;
	}
	
	
	/**
	 * Set component property
	 */
	public void setProperty(String id, String propertyName, String value)
	{
		Element e=(Element)this.getDataRootNode(id);
		NodeList plist=e.getElementsByTagName("properties");
		Document doc=e.getOwnerDocument();
		
		NodeList properties=((Element)plist.item(0)).getElementsByTagName("property");
		for(int i=0; i<properties.getLength(); i++)
		{
			Element prop=(Element)properties.item(i);
			//logger.debug(prop.hasChildNodes()+" "+prop.getChildNodes().getLength());
			NodeList namenodes=prop.getElementsByTagName("name");
			Element namenode=(Element)namenodes.item(0);	
			
			if(namenode.getFirstChild().getNodeValue().equals(propertyName)) // Property exists ?
			{
				// set new value
				if(prop.getElementsByTagName("value").item(0).hasChildNodes())
				{
					prop.getElementsByTagName("value").item(0).getFirstChild().setNodeValue(value);
				}
				else
				{
					prop.getElementsByTagName("value").item(0).appendChild(doc.createTextNode(value));
				}
				this.setChanged();
				this.notifyObservers();
				return;
			}
		}
	
		// no propery found with given name, add new property node	
		Element newProp=doc.createElement("property");
		
		newProp.appendChild(doc.createElement("name")).appendChild(doc.createTextNode(propertyName));
		newProp.appendChild(doc.createElement("value")).appendChild(doc.createTextNode(value));			
		plist.item(0).appendChild(newProp);
		//logger.debug(e.toString());
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Get properties
	 * @return Vector with the property objects, an empty vector if no properties exist.
	 */
	public Vector getProperties(String id)
	{
		Vector v=new Vector();
		
		Element e=(Element)this.getDataRootNode(id);
		NodeList nl=e.getElementsByTagName("property");
		
		for(int i=0; i<nl.getLength(); i++)
		{
			Property p=new Property();
			p.setName(this.readDOMNodeValue((Element)nl.item(i), new StringTokenizer("name","/")));
			p.setValue(this.readDOMNodeValue((Element)nl.item(i), new StringTokenizer("value","/")));
			
			v.add(p);
		}
		
		return v;
	}
	
	
	/**
	 * Get property by name
	 * @param name property name
	 * @return the propery value or null if not found
	 */
	public String getPropertyByName(String id, String propertyName)
	{
		Vector v=this.getProperties(id);
		for(Enumeration e=v.elements(); e.hasMoreElements(); )
		{
			Property p=(Property)e.nextElement();
			if(p.getName().equals(propertyName))
			{
				return p.getValue();	
			}	
		}
		return null;
	}
	
	
	/**
	 * Add new connection
	 */
	public void addConnection(String sourceID, String destID)
	{
		Element e=(Element)this.getDataRootNode(sourceID);
		
		Document doc=e.getOwnerDocument();
		
		NodeList conn=e.getElementsByTagName("connections");
		if(conn.getLength()==0)
		{
			e.appendChild(doc.createElement("connections"));
			conn=e.getElementsByTagName("connections");
		}
		NodeList connections=((Element)conn.item(0)).getElementsByTagName("connection");
		for(int i=0; i<connections.getLength(); i++)
		{
			Node connectionNode=connections.item(i);
			if(connectionNode.hasChildNodes())
			{
				//logger.debug("existing connection found.");
				if((connectionNode.getFirstChild().getNodeValue()).equals(destID))
				{
					return;	// connection still exists
				}	
			}
		}
		Node newConnection=doc.createElement("connection");
		newConnection.appendChild(doc.createTextNode(destID));
		conn.item(0).appendChild(newConnection);
	}
	
	public Collection getConnections(String id)
	{
		Vector conns=new Vector();
		
		Element e=(Element)this.getDataRootNode(id);
		
		Document doc=e.getOwnerDocument();
		
		NodeList conn=e.getElementsByTagName("connections");
		NodeList connections=((Element)conn.item(0)).getElementsByTagName("connection");
		
		for(int i=0; i<connections.getLength(); i++)
		{
			Node connectionNode=connections.item(i);
			if(connectionNode.hasChildNodes())
			{
				//logger.debug("existing connection found.");
				conns.add(connectionNode.getFirstChild().getNodeValue());	
			}
		}
		return conns;
	}
	
	/**
	 * Remove connection. If connection does not exist, nothing will be done.
	 * @param sourceID connection source ID
	 * @param destID connection destination ID
	 */
	public void removeConnection(String sourceID, String destID)
	{
		Element e=(Element)this.getDataRootNode(sourceID);
		
		Document doc=e.getOwnerDocument();
		
		NodeList conn=e.getElementsByTagName("connections");
		NodeList connections=((Element)conn.item(0)).getElementsByTagName("connection");
		for(int i=0; i<connections.getLength(); i++)
		{
			Node connectionNode=connections.item(i);
			if(connectionNode.hasChildNodes())
			{
				if((connectionNode.getFirstChild().getNodeValue()).equals(destID))
				{
					conn.item(0).removeChild(connectionNode);
					return;
				}	
			}
		}
	} 
	
	/**
	 * Remove all connections
	 */
	public void removeAllConnections(String sourceID)
	{
		Element e=(Element)this.getDataRootNode(sourceID);
		
		Document doc=e.getOwnerDocument();
		
		NodeList conn=e.getElementsByTagName("connections");
		e.removeChild(conn.item(0));
		e.appendChild(doc.createElement("connections"));	
	}

	/**
	 * Search for unused matching function groups in project database.
	 * 
	 * @param installationType device installation type (sensor, actuator, ...)
	 * @param functions array of needed busdevice functions
	 * @param deviceType device type (sensor, actuator)
	 * @param deviceIDs ids of devices, installed in this room or junction box
	 * @return device ids of bus devices with free matching function groups.
	 */
	public Vector findMatchingDevices(String installationType, String[] functions, String deviceType, Vector deviceIDs)
	{
		Vector devices=new Vector();
		
		//Vector deviceIDs=(Vector)this.getDeviceIDs();
		
		for(Enumeration e=deviceIDs.elements(); e.hasMoreElements(); )
		{
			String id=(String)e.nextElement();
			
			logger.debug("Id: "+id);
			
			if(this.getPropertyByName(id, "installation-location").equals(installationType))
			{
				if(id.startsWith(deviceType))
				{
					Vector functiongroups=(Vector)this.getIDList(this.getDataRootNode(id), "functiongroup");
					while(!functiongroups.isEmpty())
					{
						String groupid=(String)functiongroups.remove(0);
						Vector funcids=(Vector)this.getIDList(this.getDataRootNode(groupid), "function");
						
						boolean isIncluded=true;
						
						for(int cnt=0; cnt<functions.length; cnt++)
						{
							boolean match=false;
							for(int i=0; i<funcids.size(); i++)
							{
								Node n=(Node)this.getDataRootNode((String)funcids.elementAt(i));
								String ftype=this.readDOMNodeValue(n, new StringTokenizer("type", "/"));
								
								boolean unused=this.readDOMNodeValue(n, new StringTokenizer("state", "/")).equals("unused");
								
								if(ftype.equals(functions[cnt]) && unused)
								{
									match=true;
									break;
								}
							}
							
							isIncluded &= match;
							
							if(!match)
							{
								break;
							}
						} // for(cnt)
						
						if(isIncluded)
						{
							devices.add(id);
							break;
						}
					} // while
		
				} // if
		
			} //if
			
		} // for(devices)
		
		return devices;
	}
	
	public String[] getFunctionsForEnddevice(String passiveID)
	{
		Vector cons=(Vector)this.getConnections(passiveID);
		
		Vector functypes=new Vector();
		
		if(cons.size()!=0)
		{
			Node n=this.getDataRootNode((String)cons.firstElement());
			Vector funcids=(Vector)this.getIDList(n, "function");
			
			for(int i=0; i<funcids.size(); i++)
			{
				String fid=(String)funcids.get(i);
				Node fnode=this.getDataRootNode(fid);
				
				String type=this.readDOMNodeValue(fnode, new StringTokenizer("type", "/"));
				if(!type.equals("unused"))
				{
					functypes.addElement(type);
				}
			}
			
			String[] ret=new String[functypes.size()];
			for(int j=0; j<functypes.size(); j++)
			{
				ret[j]=(String)functypes.get(j);
			}
			return ret;
		}
		
		return new String[0];
		
	}
	
	
	public String getEIBDeviceID(String functionGroupID)
	{
		Node n=this.getDataRootNode(functionGroupID);
		Element parent=(Element)n.getParentNode();
		return parent.getAttribute("id");
	}
	
	/**
	 * Returns all device ids of EIB devices who are not in state "ready"
	 * @return
	 */
	public Collection getUnprogrammedEIBDeviceIDs()
	{
		Vector devs=new Vector();
		
		// search actuators
		Vector aids=(Vector)this.getIDList(this.actuatorsNode, "actuator");
		
		for(Enumeration e=aids.elements(); e.hasMoreElements(); )
		{
			String id=(String)e.nextElement();
			Node n=this.getDataRootNode(id);
			
			if(this.getPropertyByName(id, "bussystem").equals("EIB") && 
			   !this.getPropertyByName(id, "device-state").equals("ready")
			   )
			{
				devs.add(id);
			}
		}
		
		// search sensors
		Vector sids=(Vector)this.getIDList(this.sensorsNode, "sensor");
		
		for(Enumeration e=sids.elements(); e.hasMoreElements(); )
		{
			String id=(String)e.nextElement();
			Node n=this.getDataRootNode(id);
			
			if(this.getPropertyByName(id, "bussystem").equals("EIB") && 
			   !this.getPropertyByName(id, "device-state").equals("ready")
			   )
			{
				devs.add(id);
			}
		}
		
		return devs;
	}
	
	
	public int findFreeEIBGroupAddress()
	{
		int address=0;
		
		Element root=this.getDocumnet().getDocumentElement();
		
		NodeList nl=root.getElementsByTagName("function");
		for(int cnt=0; cnt<nl.getLength(); cnt++)
		{
			Element e=(Element)nl.item(cnt);
							
			String value=this.readDOMNodeValue(nl.item(cnt), new StringTokenizer("eib-group-address-count", "/"));
			
			try
			{
				int tmp=Integer.parseInt(value);
				if(tmp>=address)
				{
					address=tmp;
				}
			}
			catch(NumberFormatException nfe)
			{
				// logger.debug(nfe);
			}
			
		}
		
		address=address+1;
		
		return address;
	}
	
	public void setGroupAddresses(String[] functions, String functionGroupID)
	{
		
		Vector funcids=(Vector)this.getIDList(this.getDataRootNode(functionGroupID), "function");
			
		boolean isIncluded=true;
		
		for(int cnt=0; cnt<functions.length; cnt++)
		{
			boolean match=false;
			for(int i=0; i<funcids.size(); i++)
			{
				Node n=(Node)this.getDataRootNode((String)funcids.elementAt(i));
				String ftype=this.readDOMNodeValue(n, new StringTokenizer("type", "/"));
				
				if(ftype.equals(functions[cnt]))
				{
					this.writeDOMNodeValue(n, new StringTokenizer("eib-group-address-count", "/"), 
										   this.findFreeEIBGroupAddress()+"");
					
					this.writeDOMNodeValue(n, new StringTokenizer("state", "/"), 
										   "addressed");
					break;
				}
			}
		} // for(cnt)
	} 
	
	public void setSensorGroupAddresses(String actFuncGrpID, String sensFuncGrpID)
	{
		Vector afuncids=(Vector)this.getIDList(this.getDataRootNode(actFuncGrpID), "function");
		Vector sfuncids=(Vector)this.getIDList(this.getDataRootNode(sensFuncGrpID), "function");
		
		
		boolean isIncluded=true;
		
		for(int cnt=0; cnt<afuncids.size(); cnt++)
		{
			Node an=(Node)this.getDataRootNode((String)afuncids.elementAt(cnt));
			String aftype=this.readDOMNodeValue(an, new StringTokenizer("type", "/"));
			
			for(int i=0; i<sfuncids.size(); i++)
			{
				Node sn=(Node)this.getDataRootNode((String)sfuncids.elementAt(i));
				
				String sftype=this.readDOMNodeValue(sn, new StringTokenizer("type", "/"));
				
				if(aftype.equals(sftype) && !aftype.equals("unused"))
				{
					String gacnt=this.readDOMNodeValue(an, new StringTokenizer("eib-group-address-count", "/"));
					
					this.writeDOMNodeValue(sn, new StringTokenizer("eib-group-address-count", "/"), 
										   gacnt);
					
					this.writeDOMNodeValue(sn, new StringTokenizer("state", "/"), 
										   "addressed");
					break;
				}
			}
		} // for(cnt)
		
		
	}
	
	
	public Collection getFunctionGroupList(String busdevID)
	{
		Vector list=new Vector();
		
		Vector fgids=(Vector)(Vector)this.getIDList(this.getDataRootNode(busdevID), "functiongroup");
			
		for(int cnt=0; cnt<fgids.size(); cnt++)
		{
			String id=(String)fgids.get(cnt);
			
			Vector funcids=(Vector)this.getIDList(this.getDataRootNode(id), "function");
			
			String fid=(String)funcids.firstElement();
			String name=this.getName(fid);
			
			logger.debug("Name: "+name+" ID: "+id);
			
			ListEntry le=new ListEntry(name, id);
			
			String state=this.readDOMNodeValue(this.getDataRootNode(fid), new StringTokenizer("state", "/"));
			
			if(state.equals("unused"))
			{		
				list.addElement(le);
			}
		} // for(cnt)
		
		return list; 
	}
	
	public Collection getGroupAddresses(String functionGroupID)
	{
		
		Vector addresses=new Vector();
		
		Vector funcids=(Vector)this.getIDList(this.getDataRootNode(functionGroupID), "function");
		
		for(int i=0; i<funcids.size(); i++)
		{
			Node n=(Node)this.getDataRootNode((String)funcids.elementAt(i));
			String ftype=this.readDOMNodeValue(n, new StringTokenizer("type", "/"));
			
			
			String addrcnt=this.readDOMNodeValue(n, new StringTokenizer("eib-group-address-count", "/"));
				
			String state=this.readDOMNodeValue(n, new StringTokenizer("state", "/"));
			
			if(state.equals("addressed"))
			{
				EIBGrpaddress ga=new EIBGrpaddress(Integer.parseInt(addrcnt));
				addresses.addElement(ga);
			}
			
		}
		return addresses;
		
	}
	
	/**
	 * Returns ids of all matching function groups for the bus device with the given id.
	 * 
	 * @param busdevID bus device id
	 * @param functions array of required functions
	 * @return function group ids
	 */
	public Vector getMatchingFunctionGroups(String busdevID, String[] functions)
	{
		Vector ids=new Vector();
		
		Vector functiongroups=(Vector)this.getIDList(this.getDataRootNode(busdevID), "functiongroup");
		while(!functiongroups.isEmpty())
		{
			String groupid=(String)functiongroups.remove(0);
			Vector funcids=(Vector)this.getIDList(this.getDataRootNode(groupid), "function");
			
			boolean isIncluded=true;
			
			for(int cnt=0; cnt<functions.length; cnt++)
			{
				boolean match=false;
				for(int i=0; i<funcids.size(); i++)
				{
					Node n=(Node)this.getDataRootNode((String)funcids.elementAt(i));
					String ftype=this.readDOMNodeValue(n, new StringTokenizer("type", "/"));
					
					boolean unused=this.readDOMNodeValue(n, new StringTokenizer("state", "/")).equals("unused");
					
					if(ftype.equals(functions[cnt]) && unused)
					{
						match=true;
						break;
					}
				}
				
				isIncluded &= match;
				
				if(!match)
				{
					break;
				}
			} // for(cnt)
			
			if(isIncluded)
			{
				ids.add(groupid);
			}
		} // while
		
		return ids;
	}
	
	/**
	 * Returns all used device functions for the device with the given id.
	 * @param deviceID the device id.
	 * @return a Collection of EIBDeviceFunction's
	 */
	public Collection getUsedDeviceFunctions(String deviceID)
	{
		Vector v=new Vector();
		
		Vector ids=(Vector)this.getIDList(this.getDataRootNode(deviceID), "function");
		
		for(Enumeration e=ids.elements(); e.hasMoreElements(); )
		{
			String id=(String)e.nextElement();
			Node fnode=this.getDataRootNode(id);
			
			String state=this.readDOMNodeValue(fnode, new StringTokenizer("state","/"));
			
			if(!state.equals("unused"))
			{
				EIBDeviceFunction func=new EIBDeviceFunction();
				func.setComObject(this.readDOMNodeValue(fnode, new StringTokenizer("com-object","/")));			
				func.setEisType(this.readDOMNodeValue(fnode, new StringTokenizer("eis-type","/")));
				func.setType(this.readDOMNodeValue(fnode, new StringTokenizer("type","/")));
				func.setName(this.getName(id));
				
				String gaddr=this.readDOMNodeValue(fnode, new StringTokenizer("eib-group-address-count","/"));
				if(!gaddr.equals(""))
				{
					func.setGroupAddressCount(gaddr);
				}
				
				v.addElement(func);
			}
		}
		
		return v;
	}
	
	public boolean isEIBPhAddressInUse(EIBPhaddress pa)
	{
		Vector aids=(Vector)this.getIDList(this.actuatorsNode, "actuator");
		for(Enumeration e1=aids.elements(); e1.hasMoreElements(); )
		{
			String id=(String)e1.nextElement();
			if(this.getPropertyByName(id, "bussystem").equals("EIB"))
			{
				if(this.getPropertyByName(id, "eib-physical-address").equals(pa.toString()))
				{
					return true;
				}
			}
		}
		Vector sids=(Vector)this.getIDList(this.actuatorsNode, "actuator");
		for(Enumeration e2=sids.elements(); e2.hasMoreElements(); )
		{
			String id=(String)e2.nextElement();
			if(this.getPropertyByName(id, "bussystem").equals("EIB"))
			{
				if(this.getPropertyByName(id, "eib-physical-address").equals(pa.toString()))
				{
					return true;
				}
			}
		}
		return false;
	}
}
