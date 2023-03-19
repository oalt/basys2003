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
package basys.eib;


import basys.eib.exceptions.EIBAddressFormatException;

import org.apache.log4j.*;

/**
 *
 * EIBFrame.java
 *
 * @version $Id: EIBFrame.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * @author  oalt
 *
 */

public class EIBFrame  
{
  
  private static Logger logger=Logger.getLogger(EIBFrame.class);
  
  public static final int PRIORITY_SYSTEM = 0;
  
  
  private boolean repeat;
  private int priority;
  private EIBPhaddress srcaddr;
  private EIBAddress destaddr;
  private int rc;
  private int tpci;
  private int apci;
  private int apdata[]=null;
  private int ack;
  private int parity;
  private int len;
  
  private boolean short_data=false;
  
  /**
   * Constructor
   *
   * This constructor initializes an EIBFrame with all parameters
   */
  public EIBFrame(boolean repeat, int priority, EIBPhaddress srcaddr,
                  EIBAddress destaddr, int rc, int tpci, int apci,
                  int apdata[])  
  {

    this.repeat=repeat;
    this.priority=priority;
    this.srcaddr=srcaddr;
    this.destaddr=destaddr;
    this.rc=rc;
    this.tpci=tpci;
    this.apci=apci;
    this.apdata=apdata;
    if(apdata!=null)
    {
	    this.len=this.apdata.length;
		if(this.len==1 || this.apdata[0]!=0)
		{
			this.short_data=true;	
		}
    }
    else
    {
    	this.len=0;	
    }
  }

  /**
   * Constructor
   *
   * This Constructor initializes an EIBFrame from an int array
   */
  public EIBFrame(int[] ef)  
  {
    if( ( (ef[0]>>5) & 0x01) == 1)
    {
      this.repeat=false;
    }
    else
    {
      this.repeat=true;
    }
    this.priority=((ef[0]>>2) & 0x03);
    EIBPhaddress srcaddr = new EIBPhaddress();
    srcaddr.setHigh(ef[1]);
    srcaddr.setLow(ef[2]);
    this.srcaddr=srcaddr;
    if(((ef[5]>>7) & 0x01) == 1)	// check DAF
    {
      EIBGrpaddress dest = new EIBGrpaddress();
      dest.setHigh(ef[3]);
      dest.setLow(ef[4]);
      this.destaddr=dest;
    }
    else
    {
      EIBPhaddress dest = new EIBPhaddress();
      dest.setHigh(ef[3]);
      dest.setLow(ef[4]);
      this.destaddr=dest;
    }

    this.rc=((ef[5]>>4) & 0x07);
    this.len=ef[5] & 0x0F;

    this.tpci=((ef[6]>>2) & 0x3F);

    this.apci=((ef[6] & 0x03) << 8) | ((ef[7] & 0xC0));	// apci := 4bit msb ( x x . x x 0 0 0 0 0 0 )
    this.apdata=null;
	
	if(this.len!=0)
	{
		
		// logger.debug("APCI 4 bit msb: "+Integer.toHexString(this.apci));
		
    	this.apdata = new int[this.len];
	    if(((this.apci >> 6) <3 && (this.apci >> 6) > 0 || (this.apci >> 6) <11 && (this.apci >> 6) > 5 ))
	    {
	      // short data
		  //logger.debug("Shord data recognized.");
	      this.apdata[0]=(ef[7] & 0x3F);
		  this.short_data=true;
	    }
	    else
	    {
	    	apci |= (ef[7] & 0x3F);	
	    	this.apdata[0]=0;
	    }
	    for(int cnt=1; cnt < this.len; cnt++)
	    {
	      this.apdata[cnt]=ef[cnt+7] & 0xFF;
	    }
	}
	
    this.parity=ef[this.len + 7] & 0xFF;

    this.ack=ef[this.len + 8] & 0xFF;
    
    if(this.parity==0)
    {
      this.calcparity();
    }
  }


  /**
   * Get the EIBFrame data
   *
   * @return eib frame data as int array
   */
  public int[] getFrame()  
  {

    int[] frame=null;

    frame=new int[this.len+7];

    if(this.repeat)
    {
      frame[0]=0x90;
    }
    else
    {
      frame[0]=0xB0;
    }
    frame[0] |= (this.priority << 2);

    frame[1] = this.srcaddr.getHigh();
    frame[2] = this.srcaddr.getLow();

    frame[3] = this.destaddr.getHigh();
    frame[4] = this.destaddr.getLow();

    int daf;
    if(this.destaddr.getClass().getName().equals("basys.eib.EIBPhaddress"))
    {
      daf=0;
    }
    else
    {
      daf=1;
    }

    frame[5] = (daf << 7) | (this.rc << 4) | this.len;

    frame[6] = (this.tpci << 2) | (this.apci >> 8);

	if(this.len!=0)
	{
		if(this.short_data)
		{
	    	frame[7] = (apci & 0x0C0);
	    	frame[7] |= (this.apdata[0] & 0x3F);    	
		}
		else
	    {
	        frame[7] = (this.apci & 0x00FF);	
	    }
		for(int cnt=1; cnt<len; cnt++)
	    {
	    	//logger.debug("Append data: "+Integer.toHexString(apdata[cnt]));
	        frame[cnt+7]=apdata[cnt];
	    }
	   
	}
	/*
	System.out.print("Returned frame: ");
	for(int k=0; k<frame.length; k++)
	{
		System.out.print(Integer.toHexString(frame[k])+ " ");	
	}
	System.out.println();
	*/
    return frame;
  }

  /**
   * Get ack data
   *
   * @returnt ack data value
   */
  public int getAck()  {
    return this.ack;
  }

  /**
   * Set ack data
   *
   * @param ack the ack data value
   */
  public void setAck(int ack)  {
    this.ack=ack;
  }

  /**
   * Get Priority as String
   *
   * @return the frame priority as string
   */
  public String getPriority()  {
    String s="";
    switch(this.priority)
    {
      case 0: s="System"; break;
      case 1: s="High"; break;
      case 2: s="Alarm"; break;
      case 3: s="Low"; break;
    }
    return s;
  }

  /**
   * Get routing conter value
   *
   * @return the routing conter value
   */
  public int getRC()  {
    return this.rc;
  }

  /*  // toDo
  public boolean equals(EIBFrame ef)  {
    if(ef.srcaddr.equals(this.srcaddr)  && ef.destaddr.equals(this.destaddr) &&
       ef.repeat==this.repeat && ef.priority==this.priority && this.rc==ea.rc &&
       this.tpci==ef.tpci && this.apci==ef.apci && )
  }
  */

  /**
   * Get destination address
   *
   * @return the destination address
   */
  public EIBAddress getDestAddress()  {
    return this.destaddr;
  }

  /**
   * Get source address
   *
   * @return the destination address
   */
  public EIBPhaddress getSourceAddress()  {
    return this.srcaddr;
  }

  /**
   * Get EIB transport protocol control information
   *
   * @return the EIB TPCI value
   */
  public int getTPCI()  {
    return this.tpci;
  }

  /**
   * Get EIB transport protocol control information as string
   *
   * @return EIB TPCI as string
   */
  public String getTPCIstring()  {
    String s="";
    if(tpci==0)
    {
      if(this.destaddr.getHigh()==0  && this.destaddr.getLow()==0)
      {
      	s="T_BROADCAST_DATA_REQ_PDU";	
      }
      else if(destaddr.getClass().getName().equals("eib.EIBPhaddress"))
      {
        s="T_DATA_UNACK_REQ_PDU";
      }
      else
      {
        s="T_GROUPDATA_REQ_PDU";
      }
    }
    else if(tpci==0x20)  // 10 0000
    {
      if((this.apci>>8 & 0x3)==0)
      {
        s="T_CONNECT_REQ_PDU";
      }
      else if((this.apci>>8 & 0x3)==2)
      {
        s="T_CONNECT_CONF_PDU";
      }
      else if((this.apci>>8 & 0x3)==1)
      {
        s="T_DISCONNECT_REQ_PDU";
      }
    }
    else if((tpci>>4 & 0x3)==1)
    {
      s="T_DATA_REQ_PDU (Seq.: "+ (tpci & 0xF) +")";
    }
    else if((tpci>>4 & 0x3)==3)
    {
      if((this.apci>>8 & 0x3)==2)
      {
        s="T_DATA_ACK_PDU (Seq.: "+(tpci & 0xF)+")";
      }
      else if((this.apci>>8 & 0x3)==3)
      {
        s="T_DATA_NACK_PDU (Seq.: "+(tpci & 0xF)+")";
      }
    }
    else
    {
      s="Unknown !!! "+tpci;
    }
    return s;
  }

  /**
   * Get EIB application protocol control information value
   *
   * @return APCI value
   */
  public int getAPCI()  {
    return this.apci;
  }

  /**
   * Get EIB application protocol control information as string
   *
   * @return APCI as string
   */
  public String getAPCIstring()  {
    
    if(this.len==0)
    {
    	return "-";	
    }
    
    int tmp=((this.apci>>6) & 0x0F);
	
	
	
    String s="unknown !!! 0x"+ Integer.toHexString(this.apci);

    switch(tmp)
    {
      case 1: s="A_READ_VALUE_RES_PDU"; break;
      case 2: s="A_WRITE_VALUE_REQ_PDU"; break;
      case 6: s="A_READ_ADC_REQ_PDU"; break;
      case 7: s="A_READ_ADC_RES_PDU"; break;
      case 8: s="A_READ_MEMORY_REQ_PDU"; break;
      case 9: s="A_READ_MEMORY_RES_PDU"; break;
      case 10: s="A_WRITE_MEMORY_REQ_PDU"; break;
    }

    switch(this.apci)
    {
      case 0: s="A_READ_VALUE_REQ_PDU"; break;

      case 0x0C0: s="A_SET_PHYSADDR_REQ_PDU"; break;
      case 0x100: s="A_READ_PHYSADDR_REQ_PDU"; break;
      case 0x140: s="A_SET_PHYSADDR_RES_PDU"; break;
      case 0x3DC: s="A_READ_PHYSADDR_SERNO_REQ_PDU"; break;
      case 0x3DD: s="A_READ_PHYSADDR_SERNO_RES_PDU"; break;
      case 0x3DE: s="A_SET_PHYSADDR_SERNO_REQ_PDU"; break;
      case 0x3DF: s="A_SEVICE_INFORMATION_REQ_PDU"; break;

      case 0x3E0: s="A_SET_SYS_ID_REQ_PDU"; break;
      case 0x3E1: s="A_READ_SYS_ID_REQ_PDU"; break;
      case 0x3E2: s="A_READ_SYS_ID_RES_PDU"; break;
      case 0x3E3: s="A_READ_SYS_ID_SELECTIVE_REQ_PDU"; break;
      
      case 0x3D5: s="A_READ_PROPERY_VAKUE_REQ_PDU"; break;
      case 0x3D6: s="A_READ_PROPERY_VAKUE_RES_PDU"; break;
	  case 0x3D7: s="A_WRITE_PROPERY_VAKUE_REQ_PDU"; break;
	  case 0x3D8: s="A_READ_PROPERY_DESCRIPTION_REQ_PDU"; break;
	  case 0x3D9: s="A_READ_PROPERY_DESCRIPTION_RES_PDU"; break;
	  
	  case 0x2C0: s="A_UREAD_MEMORY_REQ_PDU"; break;
	  case 0x2C1: s="A_UREAD_MEMORY_RES_PDU"; break;
	  case 0x2C2: s="A_UWRITE_MEMORY_REQ_PDU"; break;
	  case 0x2C4: s="A_UWRITE_MEMORY_BIT_REQ_PDU"; break;
	  case 0x2C5: s="A_UREAD_MFACTINFO_REQ_PDU"; break;
	  case 0x2C6: s="A_UREAD_MFACTINFO_RES_PDU"; break;
	  
	  case 0x300: s="A_READ_MASK_VERSION_REQ_PDU"; break;
	  case 0x340: s="A_READ_MASK_VERSION_RES_PDU"; break;
	  case 0x380: s="A_RESTART_REQ_PDU"; break;
	  case 0x3D0: s="A_WRITE_MEMORY_BIT_REQ_PDU"; break;
	  case 0x3D1: s="A_AUTHORIZE_REQ_PDU"; break;
	  case 0x3D2: s="A_AUTHORIZE_RES_PDU"; break;
	  case 0x3D3: s="A_SETKEY_REQ_PDU"; break;
	  case 0x3D4: s="A_SETKEY_RES_PDU"; break;
	  case 0x3FF: s="FILE OPERATION"; break;
	  
	  
	  
    }


    return s;
  }

  /**
   * Get the application data as int array
   *
   * @return application data
   */
  public int[] getApdata()  {
    return this.apdata;
  }

  /**
   * Get application data as hex string
   *
   * @return application data as hex string
   */
  public String getApdataString()  
  {
  	if(this.apdata==null)
  	{
  		return "-";	
  	}
    String s="";
    Integer in;
    int j=0;
    if(!this.short_data)
    {
    	j=1;
    }
    for(int i=j; i<this.apdata.length; i++)
    {
      if(this.apdata[i]<0x10)
      {
        s+="0";
      }
      s+=Integer.toHexString(this.apdata[i])+" ";
    }
    s=s.toUpperCase();
    return s;
  }

  /**
   * Get repeat flag
   *
   * @return repeat flag value
   */
  public boolean getRepeat()  {
    return this.repeat;
  }

  /**
   * Get frame parity value
   *
   * @return parity value
   */
  public int getParity()  {
    return this.parity;
  }

  /**
   * Call this method to calculate the frame parity (checksum)
   *
   * @return the calculated checksum
   */
  public int calcparity()  {

    int frame[] = getFrame();

    int i=frame[0];

    for(int cnt=1; cnt < (frame[5] & 0x0F)+7; cnt++)
    {
      i = i ^ frame[cnt];
    }

    this.parity=(~i & 0xFF);

    return (~i & 0xFF);

  }

  /**
   * Returns the entire frame as hex string
   *
   * @return the hex strin of the frame
   */
  public String toHexString()  
  {
    String s="";

    int[] f=this.getFrame();

    this.calcparity();

    for(int cnt=0; cnt < f.length; cnt++)
    {
      if(f[cnt] < 16)
      {
        s+="0";
      }
      s+=Integer.toHexString(f[cnt])+" ";
    }

    if(this.parity<16)
    {
      s+="0";
    }
    s+=Integer.toHexString(this.parity);

    s+=" "+Integer.toHexString(this.getAck());
    s=s.toUpperCase();

    return s;
  }

  /**
   * Set the frame repeat flag
   *
   * @param b teh value to set
   */
  public void setRepeat(boolean b)  
  {
    this.repeat=b;
  }

  /**
   * Set frame priority value
   *
   * @param priority the value to set
   */
  public void setPriority(int priority)  {
    this.priority=priority;
  }

  /**
   * Set frame source address
   *
   * @param srcaddress Frame source address
   */
  public void setSrcAddress(EIBPhaddress srcaddr)  {
    this.srcaddr=srcaddr;
  }

  /**
   * Set frame source address from a string
   *
   * @param s string of a EIB physical address
   */
  public void setSrcAddress(String s)  {
    try
    {
    	this.srcaddr.setAddress(s);
    }
    catch(EIBAddressFormatException afe)
    {
    	afe.printStackTrace();
    }
  }

  /**
   * Set frame destination address
   *
   * @param destaddr Frame destination address
   */
  public void setDestAddress(EIBAddress destaddr)  {
    this.destaddr=destaddr;
  }

  /**
   * Set routing conter value
   *
   * @param rc routing counter value
   */
  public void setRC(int rc)  {
    this.rc=rc;
  }

  /**
   * Set TPCI value
   *
   * @param tpci TPCI value
   */
  public void setTPCI(int tpci)  {
    this.tpci=tpci;
  }

  /**
   * Set APCI value
   *
   * @param APCI value
   */
  public void setAPCI(int apci)  
  {
    this.apci=apci;
  }

  /**
   * Set application data from int array
   *
   * @param data array
   */
  public void setApdata(int[] apdata)  
  {
    this.apdata=apdata;
    this.len=apdata.length;
  }

  /**
   *
   */
  public static String[] getTPCIStringList()  {
    String[] list={
                    "T_DATA_REQ_PDU (Broadcast, Group, Unack)",
                    "T_DATA_REQ_PDU (0)",
                    "T_DATA_REQ_PDU (1)",
                    "T_DATA_REQ_PDU (2)",
                    "T_DATA_REQ_PDU (3)",
                    "T_DATA_REQ_PDU (4)",
                    "T_DATA_REQ_PDU (5)",
                    "T_DATA_REQ_PDU (6)",
                    "T_DATA_REQ_PDU (7)",
                    "T_DATA_REQ_PDU (8)",
                    "T_DATA_REQ_PDU (9)",
                    "T_DATA_REQ_PDU (10)",
                    "T_DATA_REQ_PDU (11)",
                    "T_DATA_REQ_PDU (12)",
                    "T_DATA_REQ_PDU (13)",
                    "T_DATA_REQ_PDU (14)",
                    "T_DATA_REQ_PDU (15)",
                    "T_CONNECT_REQ_PDU",
                    "T_CONNECT_CONF_PDU",
                    "T_DISCONNECT_REQ_PDU",
                    "T_DATA_ACK_PDU (0)",
                    "T_DATA_ACK_PDU (1)",
                    "T_DATA_ACK_PDU (2)",
                    "T_DATA_ACK_PDU (3)",
                    "T_DATA_ACK_PDU (4)",
                    "T_DATA_ACK_PDU (5)",
                    "T_DATA_ACK_PDU (6)",
                    "T_DATA_ACK_PDU (7)",
                    "T_DATA_ACK_PDU (8)",
                    "T_DATA_ACK_PDU (9)",
                    "T_DATA_ACK_PDU (10)",
                    "T_DATA_ACK_PDU (11)",
                    "T_DATA_ACK_PDU (12)",
                    "T_DATA_ACK_PDU (13)",
                    "T_DATA_ACK_PDU (14)",
                    "T_DATA_ACK_PDU (15)",
                    "T_DATA_NACK_PDU (0)",
                    "T_DATA_NACK_PDU (1)",
                    "T_DATA_NACK_PDU (2)",
                    "T_DATA_NACK_PDU (3)",
                    "T_DATA_NACK_PDU (4)",
                    "T_DATA_NACK_PDU (5)",
                    "T_DATA_NACK_PDU (6)",
                    "T_DATA_NACK_PDU (7)",
                    "T_DATA_NACK_PDU (8)",
                    "T_DATA_NACK_PDU (9)",
                    "T_DATA_NACK_PDU (10)",
                    "T_DATA_NACK_PDU (11)",
                    "T_DATA_NACK_PDU (12)",
                    "T_DATA_NACK_PDU (13)",
                    "T_DATA_NACK_PDU (14)",
                    "T_DATA_NACK_PDU (15)",
                  };
    return list;
  }

  /**
   *
   */
  public static String[] getAPCIStringList()  {

    String[] list = {
                        "A_READ_VALUE_REQ_PDU",
                        "A_READ_VALUE_RES_PDU",
                        "A_WRITE_VALUE_REQ_PDU",
                        "A_SET_PHYSADDR_REQ_PDU",
                        "A_READ_PHYSADDR_REQ_PDU",
                        "A_SET_PHYSADDR_RES_PDU",
                        "A_READ_PHYSADDR_SERNO_REQ_PDU",
                        "A_READ_PHYSADDR_SERNO_RES_PDU",
                        "A_SET_PHYSADDR_SERNO_REQ_PDU",
                        "A_SEVICE_INFORMATION_REQ_PDU",
                        "A_SET_SYS_ID_REQ_PDU",
                        "A_READ_SYS_ID_REQ_PDU",
                        "A_READ_SYS_ID_RES_PDU",
                        "A_READ_SYS_ID_SELECTIVE_REQ_PDU",
                        "A_READ_ADC_REQ_PDU",
                        "A_READ_ADC_RES_PDU",
                        "A_READ_MEMORY_REQ_PDU",
                        "A_READ_MEMORY_RES_PDU",
                        "A_WRITE_MEMORY_REQ_PDU",
                    };
    return list;
  }
}
