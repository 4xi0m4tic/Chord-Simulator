package chordsimulator;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

public class ChordNode
{
    public IPAddress ip;
    public BigInteger id;
    public ArrayList<FingerEntry> finger;
    
    private ChordSimulator sim;
    
    public ChordNode(int[] ip, ChordSimulator sim)
    {
        try
        {
            this.ip = new IPAddress(ip);
            this.setID(this.ip.toString());
            this.sim = sim;
        }
        catch(Exception ex)
        {
            System.err.println("" + ex.getMessage());
        }
    }
    
    public void setFinger(ArrayList<FingerEntry> finger)
    {
        //ArrayList<FingerEntry> clean = new ArrayList<>();
                
        for(int i = 1; i < finger.size(); i++)
        {
            if(finger.get(i).id.compareTo(this.id) == 0)
                finger.remove(i);
        }
        
        this.finger = finger;
    }
    
    public ChordNode lookup(BigInteger key)
    {
        sim.registerStep();
        
        //System.out.println("Asking node " + this.id.toString(16));
        // If this node has the desired key it returns its reference.
        if(this.hasKey(key))
        {
            return this;
        }
        // Else ask the closest node to the key.
        else
        {
            // The hasKey calls don't count as network messages because they could be calculated by the node locally.
            for(FingerEntry e : this.finger)
            {
                if(e.node.hasKey(key))
                    return e.node.lookup(key);
            }
            
            // Nobody immediately has the key. Ask someone
            // Reverse search the finger table for the closest node to the key.
            ChordNode highClose = this.finger.get(this.finger.size() - 1).node;
            for(int i = this.finger.size() - 1; i >= 0; i--)
            {
                // node.id < key
                if(this.finger.get(i).id.compareTo(key) <= 0)
                {
                    highClose = this.finger.get(i).node;
                    break;
                }
            }
            
            // Forward search the finger table for the closest node to the key.
            ChordNode lowClose = this.finger.get(0).node;
            for(int i = 0; i < this.finger.size(); i++)
            {
                // node.id > key
                if(this.finger.get(i).id.compareTo(key) >= 0)
                {
                    lowClose = this.finger.get(i).node;
                    break;
                }
            }
            
            // Check where to proceed.
            BigInteger highDiff = highClose.id.add(key.negate()).abs();
            BigInteger lowDiff = key.add(lowClose.id.negate()).abs();
            
            // lowDiff <= highDiff
            if(lowDiff.compareTo(highDiff) <= 0)
                return lowClose.lookup(key);
            else
                return highClose.lookup(key);
        }
    }
    
    // Returns true if this node contains the given key.
    public boolean hasKey(BigInteger key)
    {
        ChordNode pre = this.finger.get(0).node;
        
        // Check to see if this node is the first on the circle.
        // predecessor.id > this.id
        if(pre.id.compareTo(this.id) > 0)
        {
            // predecessor.id < key     covers the (predecessor, 2^m] range
            if(pre.id.compareTo(key) < 0)
                return true;
            // key <= this.id           covers the [0, this.id] range
            if(key.compareTo(this.id) <= 0)
                return true;
        }
        else
        {
            // key <= this.id
            if(key.compareTo(this.id) <= 0)
                // predecessor.id < key
                if(pre.id.compareTo(key) < 0)
                    return true;
        }
        
        return false;
    }
    
    // Sets the identifier of this node from the MD5 hash of a string.
    public void setID(String str) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("MD5"); 
        md.update(str.getBytes());
        byte[] digest = md.digest();
        this.id = new BigInteger(1, digest);
    }
}
