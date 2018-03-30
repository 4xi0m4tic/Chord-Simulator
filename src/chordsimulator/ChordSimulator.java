package chordsimulator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class ChordSimulator
{
    private final int m = 128;
    private int N;
    ArrayList<ChordNode> network = new ArrayList<>();
    
    private int steps;
    
    public ChordSimulator(int N)
    {
        this.N = N;
        
        System.out.println("Creating a network of " + N + " nodes.");
        this.createNetwork();
        
        // Sorting the nodes by their identifiers in order to make the calculation of the finger tables easier.
        System.out.println("Sorting the node circle.");
        this.network.sort(new Comparator<ChordNode>() {
            @Override
            public int compare(ChordNode t, ChordNode t1)
            {   
                return t.id.compareTo(t1.id);
            }
        });
        
        for(int i = 0; i < this.network.size(); i++)
        {
            System.out.println("Setting the finger table for node " + (i+1));
            this.setFingerTable(i, this.network.get(i));
        }
    }
    
    public void registerStep()
    {
        this.steps++;
    }
    
    // Performs a lookup on the network and returns the steps taken, or a negative number in case of failure.
    public int lookup(BigInteger key)
    {
        // Choose a random node to ask.
        Random rnd = new Random();
        ChordNode node = this.network.get(rnd.nextInt(N));
        
        this.steps = 0;
        
        //System.out.println("Starting from node: " + node.id.toString(16));
        //System.out.println("Searching for: " + key.toString(16));
        //System.out.println("Found it on: " + node.lookup(key).id.toString(16));
        
        node.lookup(key).id.toString(16);
        
        return steps;
    }
    
    // Sets the finger table of the node n in the ith place in the node circle.
    private void setFingerTable(int i, ChordNode n)
    {
        ArrayList<FingerEntry> finger = new ArrayList<>();
        
        // Set the predecessor.
        if(i == 0) // The first node has the last node of the circle as his predecessor.
        {
            ChordNode pre = this.network.get(this.network.size() - 1);
            finger.add(new FingerEntry(pre.id, pre.ip, pre));
        }
        else
        {
            ChordNode pre = this.network.get(i - 1);
            finger.add(new FingerEntry(pre.id, pre.ip, pre));
        }
        
        // Set the next m finger entries.
        for(int k = 0; k < this.m; k++)
        {
            BigInteger nextID = n.id;
            
            // next = N + (2 ^ k)
            BigInteger pow = BigInteger.valueOf(2).pow(k);
            nextID = nextID.add(pow);
            
            // next = next mod (2 ^ m)
            pow = BigInteger.valueOf(2).pow(m);
            nextID = nextID.mod(pow);
            
            ChordNode nextNode = this.findNextClosestNode(nextID, n.id);
            //System.out.println(nextID.toString(16) + " close to " + nextNode.id.toString(16));
            finger.add(new FingerEntry(nextNode.id, nextNode.ip, nextNode));
        }

        // Remove any instances of the nodes ID appearing in its finger table
        
        n.setFinger(finger);
    }
    
    // Returns the node with the closest bigger identifier than the given number.
    private ChordNode findNextClosestNode(BigInteger num, BigInteger id)
    {
        for(int i = 0; i < this.network.size(); i++)
        {
            // node.id >= new.id for the first time.
            if(this.network.get(i).id.compareTo(num) >= 0)
                return this.network.get(i);
        }
        
        // node.id is between the last node and the first one.
        return this.network.get(0);
    }
    
    // Prints the whole network.
    public void printNetwork()
    {
        for(int i = 0; i < this.network.size(); i++)
            System.out.println((i+1) + "\t" + this.network.get(i).ip + "\t" + this.network.get(i).id.toString(16));
        
        System.out.println("");
    }
    
    // Prints the finger table of a given node.
    public void printFingerTable(int n)
    {
        ChordNode node = this.network.get(n);
        System.out.println("N" + (n+1) + "\t" + node.id.toString(16));
        
        for(int i = 0; i < node.finger.size(); i++)
            System.out.println((i+1) + "\t" + node.finger.get(i).id.toString(16));
    }
    
    // Creates N ChordNodes with random and unique IPs.
    private void createNetwork()
    {
        Random rnd = new Random();
        
        for(int i = 0; i < this.N; i++)
        {
            System.out.println("Creating node " + i);
            int[] ip;
            do
            {
                ip = new int[] {rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256)};
            } while(this.ipExists(ip));
            
            this.network.add(new ChordNode(ip, this));
        }
    }
    
    // Checks if a given IP exists in the network.
    private boolean ipExists(int[] ip)
    {
        for(ChordNode n : this.network)
        {
            int[] nodeip = n.ip.ipAddress;
            if((ip[0] == nodeip[0]) && (ip[1] == nodeip[1]) && (ip[2] == nodeip[2]) && (ip[3] == nodeip[3]))
                return true;
        }
        
        return false;
    }
}
