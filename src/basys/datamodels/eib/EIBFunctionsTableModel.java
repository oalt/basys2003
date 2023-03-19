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
package basys.datamodels.eib;

import basys.LocaleResourceBundle;
import basys.eib.dataaccess.EIBCommunicationObject;
import basys.eib.EIBDeviceFunction;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

/**
 * EIBFinctionsTableModel.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBFunctionsTableModel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EIBFunctionsTableModel extends AbstractTableModel
{
	private static Logger logger=Logger.getLogger(EIBFunctionsTableModel.class);
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	private Vector normalizedDevicefunctions;
	private String[] colkeys={ "tc.nr", "tc.name", "tc.funcname", "tc.eis", "tc.func", "tc.funcgroups"	};
	
	private String[] functions;
	
	private Vector[] funcgroups;
	
	/**
	 * 
	 */
	public EIBFunctionsTableModel(Vector normalizeddeviceFunctions)
	{
		super();
		this.normalizedDevicefunctions=normalizeddeviceFunctions;
		this.functions=new String[this.normalizedDevicefunctions.size()];
		for(int i=0; i<this.normalizedDevicefunctions.size(); i++)
		{
			this.functions[i]="unused";
		}
		
		this.funcgroups=new Vector[this.normalizedDevicefunctions.size()];
		for(int i=0; i<this.normalizedDevicefunctions.size(); i++)
		{
			this.funcgroups[i]=new Vector();
		}
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount()
	{
		return this.normalizedDevicefunctions.size();
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount()
	{
		return colkeys.length;
	}

	/**
	 * 
	 */
	public String getColumnName(int col) 
	{
		return locale.getString(colkeys[col]);
	}
	
	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		EIBCommunicationObject co=(EIBCommunicationObject)this.normalizedDevicefunctions.get(rowIndex);
		
		Object o="";
		
		switch(columnIndex)
		{
			case 0:
				o=co.getNumber();
			break;
			case 1:
				o=co.getName();
			break;
			case 2:
				o=co.getFunction();
			break;
			case 3:
				o=co.getType();
			break;
			case 4:
				o=this.functions[rowIndex];
			break;
			case 5:
				o=this.getFuncGroupValue(rowIndex);
			break;	
		}
		
		return o;
	}
	
	/**
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) 
	{
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col != 4) 
        { 
            return false;
        } 
        else 
        {
      		return true;
    	}
	}
	
	
	public void setValueAt(Object value, int row, int col)
	{
		System.out.println(value);
		if(col==4)
		{
			this.functions[row]=(String)value;
		}
	}
	
	public void setContent(Vector normalizedDevicefunctions)
	{
		this.normalizedDevicefunctions = normalizedDevicefunctions;
		this.fireTableDataChanged();
	}
	
	public void fireTableDataChanged()
	{
		super.fireTableDataChanged();
		this.functions=new String[this.normalizedDevicefunctions.size()];
		for(int i=0; i<this.normalizedDevicefunctions.size(); i++)
		{
			this.functions[i]="unused";
		}
		this.funcgroups=new Vector[this.normalizedDevicefunctions.size()];
		for(int i=0; i<this.normalizedDevicefunctions.size(); i++)
		{
			this.funcgroups[i]=new Vector();
		}
	}
	
	/**
	 * Set new function group
	 * @param rowIndices selected table rows
	 */
	public void setFunctionGroup(int[] rowIndices)
	{
		
		// check if no group exists
		for(int cnt=0; cnt<rowIndices.length; cnt++)
		{
			if(!this.funcgroups[ rowIndices[cnt] ].isEmpty())
			{
				return;
			}
		}
		
		// Build function group Vector
		Vector v=new Vector();
		for(int cnt=0; cnt<rowIndices.length; cnt++)
		{
			EIBCommunicationObject co=(EIBCommunicationObject)this.normalizedDevicefunctions.get(rowIndices[cnt]);
			v.addElement(co.getNumber());
		}
		
		// Set function groups
		for(int cnt=0; cnt<rowIndices.length; cnt++)
		{
			this.funcgroups[rowIndices[cnt]]=v;
		}
		//logger.debug("Setze funktionsgruppe: "+v.toString());
		super.fireTableDataChanged();
		
	}

	/**
	 * Returns the device functions.
	 * 
	 * @return a Vector wich contains Vectors with EIBDeviceFunctions objects
	 */
	public Vector getDeviceFunctions()
	{
		Vector devfuncs=new Vector();
		
		boolean done[] = new boolean[this.normalizedDevicefunctions.size()]; // prepare double selections
		for(int j=0; j<done.length; j++)
		{
			done[j]=false;
		}
		
		// iterate over all entries		
		for(int cnt=0; cnt < this.normalizedDevicefunctions.size(); cnt++)
		{
			Vector devfun=new Vector();
			Vector fgrps=null;
			
			for(int i=cnt; i<this.normalizedDevicefunctions.size(); i++)
			{
				EIBCommunicationObject co=(EIBCommunicationObject)this.normalizedDevicefunctions.get(i);
				
				if(this.funcgroups[i].isEmpty() && !done[i] && devfun.isEmpty() && !this.functions[i].equals("unused"))	// no function group defined
				{
					EIBDeviceFunction f=new EIBDeviceFunction();
					f.setName(co.getName());
					f.setComObject(co.getNumber());
					f.setEisType(co.getType());
					f.setType(this.functions[i]);
					f.setFunctionName(co.getFunction());
					devfun.addElement(f);
					done[i]=true;
					break;
				}
				else	// add all entries to the entire function group 
				{
					if(fgrps==null)
					{
						fgrps=this.funcgroups[i];
					}
					for(Enumeration e=fgrps.elements(); e.hasMoreElements(); )
					{
						String number=(String)e.nextElement();
						if(number.equals(co.getNumber()) && !done[i] && !this.functions[i].equals("unused"))
						{
							EIBDeviceFunction f=new EIBDeviceFunction();
							f.setName(co.getName());
							f.setComObject(co.getNumber());
							f.setEisType(co.getType());
							f.setType(this.functions[i]);
							f.setFunctionName(co.getFunction());
							devfun.addElement(f);
							done[i]=true;
						}
						
					} // for(Enumeration ... )
				} // if
				
			} // for
			
			if(!devfun.isEmpty()) // add only matching entries
			{
				devfuncs.addElement(devfun);
			}		
		} // for

		return devfuncs;
	}
	
	/**
	 * Calculate String for displaying the function groups
	 * @param rowIndex the index of the table row
	 * @return the calculated String
	 */
	private String getFuncGroupValue(int rowIndex)
	{
		String s="";
		
		for(int cnt=0; cnt<this.funcgroups[rowIndex].size(); cnt++)
		{
			if((cnt+1)==this.funcgroups[rowIndex].size())
			{
				s+=this.funcgroups[rowIndex].get(cnt);
			}
			else
			{
				s+=this.funcgroups[rowIndex].get(cnt)+", ";
			}
		}
		
		return s;	
	}
	
	
	
}
