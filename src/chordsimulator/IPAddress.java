package chordsimulator;

public class IPAddress 
{
    public int[] ipAddress;
    
    public IPAddress(int[] ipAddress)
    {
        this.setIPAdress(ipAddress);
        
        // Reference to the node that corresponds to this IP
    }
    
    public void setIPAdress(int[] ipAddress)
    {
        if(ipAddress.length != 4)
            throw new IllegalArgumentException("ERROR: The IP address must have 4 parts.");
        
        for(int i : ipAddress)
        {
            if(i < 0)
                throw new IllegalArgumentException("ERROR: The IP address must be positive.");
            if(i > 255)
                throw new IllegalArgumentException("ERROR: The IP address must be bellow 255.");
        }
        
        this.ipAddress = ipAddress;
    }
    
    public String toString()
    {
        return (this.ipAddress[0] + "." + this.ipAddress[1] + "." + this.ipAddress[2] + "." + this.ipAddress[3]);
    }
}
