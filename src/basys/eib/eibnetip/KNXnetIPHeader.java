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

import basys.eib.eibnetip.exceptions.KNXnetIPInvalidDataException;
import basys.eib.eibnetip.exceptions.KNXnetIPNotSupportedException;

/**
 * KNXnetIPHeader common header for all KNXnet/IP data packets or frames
 */
public class KNXnetIPHeader
{
    // KNXnet/IP service types
    
    // core services
    public static final short SEARCH_REQUEST = 0x201;
    
    public static final short SEARCH_RESPONSE = 0x202;
    
    public static final short DESCRIPTION_REQUEST = 0x203;
    
    public static final short DESCRIPTION_RESPONSE = 0x204;
    
    public static final short CONNECT_REQUEST = 0x205;

    public static final short CONNECT_RESPONSE = 0x206;

    public static final short CONNECTIONSTATE_REQUEST = 0x207;

    public static final short CONNECTIONSTATE_RESPONSE = 0x208;

    public static final short DISCONNECT_REQUEST = 0x209;

    public static final short DISCONNECT_RESPONSE = 0x20A;

    // device management services
    public static final short DEVICE_CONFIGURATION_REQUEST = 0x310;
    
    public static final short DEVICE_CONFIGURATION_RESPONSE = 0x311;
    
    // tunneling services
    public static final short TUNNELLING_REQUEST = 0x420;

    public static final short TUNNELLING_ACK = 0x421;

    // Version 1.0 header info
    private static final byte HEADER_SIZE_10 = 0x06;

    private static final byte EIBNETIP_VERSION = 0x10;
    
    private short type = 0;

    private int size = 0; // just msg body without header size

    

    public KNXnetIPHeader(short serviceType, int msgSize)
            throws KNXnetIPNotSupportedException
    {
//        if (serviceType < CONNECT_REQUEST || serviceType > DISCONNECT_RESPONSE
//                && serviceType != TUNNELLING_REQUEST
//                && serviceType != TUNNELLING_ACK)
//        {
//            throw new KNXnetIPNotSupportedException(
//                    "Not supported service type");
//        }
        type = serviceType;
        size = msgSize;
    }

    /**
     *
     */
    public KNXnetIPHeader(byte[] buf) throws KNXnetIPInvalidDataException
    {
        if (buf[0] != HEADER_SIZE_10 || buf[1] != EIBNETIP_VERSION)
        {
            throw new KNXnetIPInvalidDataException("Wrong header");
        }
        type = (short) ((buf[2] << 8) | ((int) buf[3] & 255));
//        if ((type < CONNECT_REQUEST || type > DISCONNECT_RESPONSE)
//                && type != TUNNELLING_REQUEST && type != TUNNELLING_ACK)
//        {
//            throw new KNXnetIPInvalidDataException("Not supported service type");
//        }
        size = ((buf[4] << 8) | ((int) buf[5] & 255)) - HEADER_SIZE_10;
        if (size < 0)
        {
            throw new KNXnetIPInvalidDataException("Negative message body size");
        }
    }

    public int getHeaderSize()
    {
        return HEADER_SIZE_10;
    }

    public void setMsgSize(int msgSize)
    {
        size = msgSize;
    }

    public int getMsgSize()
    {
        return size;
    }

    public short getServiceType()
    {
        return type;
    }

    public byte[] getHeader()
    {
        byte[] header = new byte[6];
        header[0] = HEADER_SIZE_10;
        header[1] = EIBNETIP_VERSION;
        header[2] = (byte) (type >>> 8);
        header[3] = (byte) (type & 255);
        header[4] = (byte) ((size + HEADER_SIZE_10) >>> 8);
        header[5] = (byte) ((size + HEADER_SIZE_10) & 255);
        return header;
    }

}
