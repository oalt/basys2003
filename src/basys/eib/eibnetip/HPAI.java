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
 * Copyright (c) 2006        BASys 2003 Project, 
 *                           Vienna University of Technology, 
 *                           Department of Automation - Automation Systems group,
 *                           B. Malinowsky
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

import java.net.InetAddress;

import basys.eib.eibnetip.exceptions.KNXnetIPInvalidDataException;
import basys.eib.eibnetip.exceptions.KNXnetIPNotSupportedException;

/**
 * HPAI Host Protocol Address Information (HPAI) for KNXnet/IP host protocol IP
 * version 4, UDP/TCP
 */
public class HPAI
{
// host protocol code
    private static final byte IPV4_UDP = 0x01;

    private static final byte IPV4_TCP = 0x02;
    
    private byte protocol = IPV4_UDP;

    private int ip1 = 0, ip2 = 0, ip3 = 0, ip4 = 0;

    private int port = 0;

    private static final byte HPAI_SIZE = 0x08;

    public HPAI(boolean useUDP, String hostIP, int hostPort)
            throws KNXnetIPNotSupportedException
    {
        if (useUDP)
        {
            protocol = IPV4_UDP;
        }
        else
        {
            throw new KNXnetIPNotSupportedException(
                    "TCP host protocol requested");
        }
        
        try
        {
            byte[] addr = InetAddress.getByName(hostIP).getAddress();
            ip1 = addr[0];
            ip2 = addr[1];
            ip3 = addr[2];
            ip4 = addr[3];
        } catch (Exception e)
        {
            System.out.println("HPAI initialization: " + e);
        }
        port = hostPort;
    }

    public HPAI(byte[] buf) throws KNXnetIPInvalidDataException,
            KNXnetIPNotSupportedException
    {
        if (buf[0] != HPAI_SIZE)
            throw new KNXnetIPInvalidDataException("Wrong header");
        if (buf[1] != IPV4_UDP)
            throw new KNXnetIPNotSupportedException(
                    "Only UDP host protocol supported");
        protocol = buf[1];
        ip1 = buf[2];
        ip2 = buf[3];
        ip3 = buf[4];
        ip4 = buf[5];
        port = (((int) buf[6] & 255) << 8) | (((int) buf[7]) & 255);
    }

    public byte getSize()
    {
        return HPAI_SIZE;
    }

    public byte[] getHPAI()
    {
        byte[] hpai = new byte[HPAI_SIZE];
        hpai[0] = HPAI_SIZE;
        hpai[1] = protocol;
        hpai[2] = (byte) ip1;
        hpai[3] = (byte) ip2;
        hpai[4] = (byte) ip3;
        hpai[5] = (byte) ip4;
        hpai[6] = (byte) (port >>> 8);
        hpai[7] = (byte) (port & 255);
        return hpai;
    }

    public String getIP()
    {
        return (ip1 & 0xff) + "." + (ip2 & 0xff) + "." + (ip3 & 0xff) + "."
                + (ip4 & 0xff);
    }
    
    public int getPort()
    {
        return port;
    }
}
