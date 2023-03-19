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
 * Copyright (c) 2003-2008,  BASys 2003 Project, 
 *                           Vienna University of Technology, 
 *                           Department of Automation - Automation Systems group,
 *                           Oliver Alt 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice, this list
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
package basys.eib.eibnetip;

import basys.eib.EIBPhaddress;

/**
 * DeviceInformationDIB.java
 * 
 * @author  olli
 * @version $Id: DeviceInformationDIB.java,v 1.1 2008/04/28 17:50:58 oalt Exp $
 */
public class DeviceInformationDIB extends DIB
{

    private int knxMedium = DIB.KNX_MEDIUM_TP1;
    
    private int deviceStatus = DIB.DEVICE_STATUS_NON_PROGRAM_MODE;
    
    private EIBPhaddress physicalAddr;
    
    private int projectInstallID = 0x0011;
    
    private int serialNumber[] = new int[6];
    
    private int[] deviceRoutingMCAddress = {224, 0, 23, 12};
    
    private int macAddress[] = new int[6];
    
    private String deviceName = "";
    
    private final int headerSize = 54;
    
    public DeviceInformationDIB()
    {
        super();
        this.size = headerSize;
        this.descriptionType = DIB.DESCRIPTION_TYPE_DEVICE_INFO;
    }

    public DeviceInformationDIB(byte[] dipFrame)
    {
        super();
        this.size = headerSize;
        this.descriptionType = DIB.DESCRIPTION_TYPE_DEVICE_INFO;
        //...
    }
    
    public DeviceInformationDIB(int knxMedium, 
                                int deviceStatus,
                                EIBPhaddress physicalAddr,
                                int projectInstallID,
                                int serialNumber[],
                                int macAddress[],
                                String deviceName)
    {
        this.knxMedium = knxMedium;
        this.deviceStatus = deviceStatus;
        this.physicalAddr = physicalAddr;
        this.serialNumber = serialNumber;
        this.macAddress = macAddress;
        this.deviceName = deviceName;
        this.size = headerSize;
        this.descriptionType = DIB.DESCRIPTION_TYPE_DEVICE_INFO;
    }
    
    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public int getDeviceStatus()
    {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus)
    {
        this.deviceStatus = deviceStatus;
    }

    public int getKnxMedium()
    {
        return knxMedium;
    }

    public void setKnxMedium(int knxMedium)
    {
        this.knxMedium = knxMedium;
    }

    public int[] getMacAddress()
    {
        return macAddress;
    }

    public void setMacAddress(int[] macAddress)
    {
        this.macAddress = macAddress;
    }

    public EIBPhaddress getPhysicalAddr()
    {
        return physicalAddr;
    }

    public void setPhysicalAddr(EIBPhaddress physicalAddr)
    {
        this.physicalAddr = physicalAddr;
    }

    public int getProjectInstallID()
    {
        return projectInstallID;
    }

    public void setProjectInstallID(int projectInstallID)
    {
        this.projectInstallID = projectInstallID;
    }

    public int[] getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int[] serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public int[] getDeviceRoutingMCAddress()
    {
        return deviceRoutingMCAddress;
    }

    public void setDeviceRoutingMCAddress(int[] deviceRoutingMCAddress)
    {
        this.deviceRoutingMCAddress = deviceRoutingMCAddress;
    }

    public byte[] getDIP()
    {
        byte frame[] = new byte[this.size];
        
        frame[0] = (byte) this.size;
        frame[1] = (byte) this.descriptionType;
        frame[2] = (byte) this.knxMedium;
        frame[3] = (byte) this.deviceStatus;
        
        frame[4] = (byte) this.physicalAddr.getHigh();
        frame[5] = (byte) this.physicalAddr.getLow();
        
        frame[6] = (byte) ((this.projectInstallID >> 8) & 0xFF);
        frame[7] = (byte) (this.projectInstallID & 0xFF);
        
        for(int i=0; i<4; i++)
        {
            frame[14+i] = (byte)this.deviceRoutingMCAddress[i];
        }
        
        for(int cnt=0; cnt < 6; cnt++)
        {
            frame[8+cnt] = (byte) this.serialNumber[cnt];
            frame[18+cnt] = (byte) this.macAddress[cnt];
        }
        
        for(int cnt =0; cnt < 30; cnt++)
        {
            if(this.deviceName.length()>cnt)
            {
                char c = this.deviceName.charAt(cnt);
                frame[24+cnt] = (byte) (c & 0xFF);
            }
            else
            {
                frame[24+cnt] = 0;
            }
        }
        
        return frame;
    }

}
