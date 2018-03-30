package chordsimulator;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MainLogic
{
    public static void main(String[] args)
    {
        Scanner file;
        ArrayList<String> filenames = new ArrayList<>();

        try
        {
            file = new Scanner(new File("filenames2.txt"));
            System.out.println("Loading the filenames.");
            while(file.hasNextLine()) filenames.add(file.nextLine());
            file.close();
        }
        catch(Exception ex)
        {
            System.err.println("" + ex);
        }
        
        
        // ~100, ~1000, ~10000, 100000 Nodes
        // 100, 1000, 10000         Searches
        
        ChordSimulator sim = new ChordSimulator(100); 
        BigInteger key;
        Random rnd = new Random();
        double avrg = 0;
        int f = rnd.nextInt(filenames.size()) % filenames.size();
        
        sim.printNetwork();
        
        System.out.println("Searching for 100 keys.");
        avrg = 0;
        for(int i = 0; i < 100; i++)
        {
            try
            {
                key = makeID(filenames.get(f));
                
                avrg += sim.lookup(key);
                
                f += rnd.nextInt(filenames.size());
                f %= filenames.size();
            }
            catch(Exception ex)
            {
                System.err.println("" + ex);
            }
        }
        avrg = avrg / 100.0;
        System.out.println("Average messages to search 100 keys: " + avrg);
        
        
        System.out.println("Searching for 1000 keys.");
        avrg = 0;
        for(int i = 0; i < 1000; i++)
        {
            try
            {
                key = makeID(filenames.get(f));
                
                avrg += sim.lookup(key);
                
                f += rnd.nextInt(filenames.size());
                f %= filenames.size();
            }
            catch(Exception ex)
            {
                System.err.println("" + ex);
            }
        }
        avrg = avrg / 1000.0;
        System.out.println("Average messages to search 1000 keys: " + avrg);
        
        System.out.println("Searching for 10000 keys.");
        avrg = 0;
        for(int i = 0; i < 10000; i++)
        {
            try
            {
                key = makeID(filenames.get(f));
                
                avrg += sim.lookup(key);
                
                f += rnd.nextInt(filenames.size());
                f %= filenames.size();
            }
            catch(Exception ex)
            {
                System.err.println("" + ex);
            }
        }
        avrg = avrg / 10000.0;
        System.out.println("Average messages to search 10000 keys: " + avrg);
    }
    
    private static BigInteger makeID(String str) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("MD5"); 
        md.update(str.getBytes());
        byte[] digest = md.digest();
        return new BigInteger(1, digest);
    }
}
