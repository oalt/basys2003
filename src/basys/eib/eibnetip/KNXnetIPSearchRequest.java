package basys.eib.eibnetip;

import basys.eib.eibnetip.exceptions.KNXnetIPInvalidDataException;
import basys.eib.eibnetip.exceptions.KNXnetIPNotSupportedException;

/**
 * KNXnetIPSearchRequest.java
 * 
 * @author  olli
 * @version $Id: KNXnetIPSearchRequest.java,v 1.1 2008/04/28 17:50:58 oalt Exp $
 */
public class KNXnetIPSearchRequest extends KNXnetIPFrameBase
{
    
    private HPAI discoveryEndpoint;
    
    
    public KNXnetIPSearchRequest()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see basys.eib.eibnetip.KNXnetIPFrameBase#fillFrame(byte[])
     */
    public void fillFrame(byte[] buf) throws KNXnetIPInvalidDataException, KNXnetIPNotSupportedException
    {
        this.header = new KNXnetIPHeader(buf);
        if(header.getServiceType() != KNXnetIPHeader.SEARCH_REQUEST)
        {
            throw new KNXnetIPInvalidDataException();
        }
        byte[] bufWithoutHeader=new byte[buf.length-this.header.getHeaderSize()];
        
        System.arraycopy(buf, this.header.getHeaderSize(), bufWithoutHeader, 0, buf.length-this.header.getHeaderSize());
        this.discoveryEndpoint = new HPAI(bufWithoutHeader);
    }

    public HPAI getDiscoveryEndpoint()
    {
        return discoveryEndpoint;
    }

    public void setDiscoveryEndpoint(HPAI discoveryEndpoint)
    {
        this.discoveryEndpoint = discoveryEndpoint;
    }
    
}
