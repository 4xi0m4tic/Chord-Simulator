package chordsimulator;

import java.math.BigInteger;

public class FingerEntry
{
    public BigInteger id;
    public IPAddress ip;
    public ChordNode node;
    
    public FingerEntry(BigInteger id, IPAddress ip, ChordNode node)
    {
        this.id = id;
        this.ip = ip;
        this.node = node;
    }
}
