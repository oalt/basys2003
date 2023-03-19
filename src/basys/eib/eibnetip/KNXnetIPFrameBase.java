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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import basys.eib.eibnetip.exceptions.KNXnetIPInvalidDataException;
import basys.eib.eibnetip.exceptions.KNXnetIPNotSupportedException;
import basys.eib.exceptions.EIBConnectionNotPossibleException;

/**
 * KNXnetIPFrameBase base class for KNXnet/IP protocol frames
 */
public abstract class KNXnetIPFrameBase
{
    protected KNXnetIPHeader header;
    
    private byte[] invalidPacket; // stores last received packet (only if

    // unknown/invalid)

    protected int responseTime = 0; // ms

    public static final byte E_NO_ERROR = 0x00;

    public KNXnetIPFrameBase()
    {
        super();
    }

    /**
     * wait a certain timespan for a matching frame from the socket exception always indicates a connection timeout,
     * even tough there can be a more serious event (broken connection...)
     */
    public boolean receive(DatagramSocket socket) throws EIBConnectionNotPossibleException
    {
        byte[] buf = new byte[512];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        long start = System.currentTimeMillis();
        while (true)
        {
            if (System.currentTimeMillis() > (start + responseTime))
            {
                throw new EIBConnectionNotPossibleException("KNXnet/IP error: connection timeout");
            }
            try
            {
                // prevent that socket might get another timeout value
                synchronized (socket)
                {
                    socket.setSoTimeout(responseTime);
                    socket.receive(p);
                }
            }
            catch (SocketTimeoutException ste)
            {
                throw new EIBConnectionNotPossibleException("KNXnet/IP error: connection timeout");
            }
            catch (Exception e)
            {
                throw new EIBConnectionNotPossibleException(e.getMessage());
            }
            try
            {
                // Note: we create a copy, because objects might change
                // buffer...
                fillFrame((byte[]) buf.clone()); // see if frame meets the
                // criteria
                // erase last unknown packet, if any
                invalidPacket = null;
                return true;
            }
            catch (KNXnetIPInvalidDataException e)
            {
                invalidPacket = buf;
                return false;
            }
            catch (KNXnetIPNotSupportedException e)
            {
                throw new EIBConnectionNotPossibleException("KNXnet/IP error: unsupported mode");
            }
        }
    }

    /**
     * send the frame over socket
     */
    public boolean send(DatagramSocket socket, String ipAddr, int port)
    {
        try
        {
            byte[] buf = getFrame();
            System.out.println("sending frame to ip " + ipAddr + ", port " +
            port);
            Debug.printFrame(buf);
            DatagramPacket p = new DatagramPacket(buf, buf.length, InetAddress.getByName(ipAddr), port);
            socket.send(p);
        }
        catch (Exception e)
        {
            System.out.println("Datagram error: " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * try to fill the object with data out of buf[]
     */
    public abstract void fillFrame(byte[] buf) throws KNXnetIPInvalidDataException, KNXnetIPNotSupportedException;

    /**
     * return the byte frame
     */
    public byte[] getFrame()
    {
        System.out.println("getFrame NYI");
        return null;
    }

    /**
     * generates a copy from buf from position startPos on
     */
    protected byte[] copy(byte[] buf, int startPos)
    {
        byte[] tmp = new byte[buf.length - startPos];
        System.arraycopy(buf, startPos, tmp, 0, tmp.length);
        return tmp;
    }

    /**
     * concatenates buf1 and buf2
     */
    protected byte[] concat(byte[] buf1, byte[] buf2)
    {
        if (buf2 != null)
        {
            byte[] tmp = new byte[buf1.length + buf2.length];
            System.arraycopy(buf1, 0, tmp, 0, buf1.length);
            System.arraycopy(buf2, 0, tmp, buf1.length, buf2.length);
            return tmp;
        }
        return (byte[]) (buf1.clone());
    }

    public byte[] getInvalidPacket()
    {
        return invalidPacket;
    }

    public KNXnetIPHeader getHeader()
    {
        return header;
    }

    public void setHeader(KNXnetIPHeader header)
    {
        this.header = header;
    }

}