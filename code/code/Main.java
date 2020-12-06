import java.io.*;
import java.util.Vector;

class Main
{    
    // converts hexadecimal to binary and adds zeros to match the length given
    static String HexaDecimal_To_Binary(String value,int len)
    {
        // value is the data in hexadecimal it is converted to decimal.
        long a = Long.parseLong(value,16);

        // the the decimal is converted to binary
        String b = Long.toBinaryString(a);

        // if the decimal is 3 the binary would be "11" but the length of the string is 2.
        // for easier comparision it is set to length give as parameter
        int length = b.length();
        for(int i=0;i<len-length;i++)
        {
            // length is increase by adding zeros before the binary number
            // the number is always positive so the value does not change
            b = "0" + b;
        }
        return b;

    }

    // This is one of the most important function of the code
    static void Set_Associative_Cache(Vector<String> addresses)
    {
        // sac is an object that stores the cache data
        SetAssociativeCache sac = new SetAssociativeCache();
        int hit = 0, miss = 0;

        // addresses are iterated in order and hits and misses are calculated
        for(String address : addresses)
        {

            // hexadecimal address is converted to binary number of length 32
            String binary = HexaDecimal_To_Binary(address, 32);
            
            // bits in byte off set is 2.
            String SbyteOffSet = binary.substring(30,32);// not used
            
            // bits in byte off set is 14.
            String Sindex = binary.substring(16,30);
            
            // bits in byte off set is 16.
            String Stag = binary.substring(0,16);

            // binary index is converted to decimal form so that it can be used in inbuilt functions
            int index = Integer.parseInt(Sindex,2);

            // the flag variable before, keeps a track of "if any of the conditions in the loop is executed"
            // if any condition of the loop is true then that means that we has decided that if it was a miss or a hit
            int flag = 0;

            // for any address we need to check the tags at a particular index.
            // we have got our index above. So now we need to check if the tag of the 
            // address matches with any tag in the cache at that index. The following loop
            // does the same.
            for(int i=0;i<4;i++)
            {
                // we check if the cache at the index has some data or not.
                // if it has some data at any of the ways, then the V array element
                // corresponding to that element will have true else it
                // will have false. It could have been zeros or one here
                // but as it checks the validity so I though it is better is it
                // is of the type boolean
                if(sac.V[index][i] == false)
                {
                    // if we enter this block that means that the data is not there
                    // and this is a miss. If it is the first way then there is no
                    // data at that index and hence it is a miss. if it is 2nd, 3rd or 4th
                    // way then the previous ways tag was not equal to the tag of this address.
                    flag = 1;
                    miss++;
                    sac.V[index][i] = true;
                    sac.tag[index][i] = Stag;

                    // once we set the V array's element corresponding to this element true then
                    // we need to update the recent number. (For LRU policy)
                    for(int j=0;j<=i;j++)
                    { // elemnts after the index "i" if any will be empty as the data is set from left to right

                        // if i == j set that element as most recently used
                        if(i==j)
                        {
                            sac.LRU[index][j] = 4;
                        }
                        else
                        {
                            // As the element is added more recently than others it means that the recent number
                            // of the other elements must decrease
                            sac.LRU[index][j]--;
                        }
                    }
                    // we have found out if it was a miss so we break out of the loop
                    break;
                }
                else
                {
                    // if V array's element is true that means that there is some element there in the cache

                    // if that element's address tag is equal to the tag of this address the it is a hit
                    if(sac.tag[index][i].equals(Stag))
                    {
                        flag = 1;
                        hit++;

                        // if the element found is already at the highest recent number then
                        // no change in the recent number takes place.
                        // e.g.     a b c d
                        //          1 2 3 4   ==> recent numbers
                        // now if access element 4 the recent numbers remain the same
                        if(sac.LRU[index][i] == 4)
                        {
                            // as the work of recent numberand the hit is done so we break out of the loop
                            break;
                        }

                        // e.g.     a b c d
                        //          1 2 3 4   ==> recent numbers
                        // now if access element c then the recent numbers change for the elements with greater recent
                        // numbers than the element that was accessed. So the above case will change to-
                        //          a b c d
                        //          1 2 4 3   ==> recent numbers
                        // this is done in the loop given below
                        int value = sac.LRU[index][i];
                        for(int j=0;j<4;j++)
                        {
                            if(i==j)
                            {
                                sac.LRU[index][j] = 4;
                            }
                            else
                            {
                                if(sac.LRU[index][j] >= value)
                                    sac.LRU[index][j]--;
                            }
                        }

                        // we are now done with the fact that this was a hit and we are done
                        // with updating the recent numbers so we break out of the loop
                        break;
                    }
                }
            }
            if(flag == 0)
            {

                // after the above iterations we can very well say that it is a miss
                // but we still need update the recent number after we update the 
                // tag value in one of the ways. We are using the LRU principle here

                miss++;
                for(int i=0;i<4;i++)
                {
                    // least recently used will have the recent number as 1.
                    if(sac.LRU[index][i] == 1)
                    {
                        sac.LRU[index][i] = 4;
                        // the recent number is updated to 4 as we replace that number
                        // it becomes the most recently used element
                        sac.tag[index][i] = Stag;// tag value of the address of the element is updated
                    }
                    else
                    {
                        // recent numbers of the other elements used decrease by one
                        sac.LRU[index][i]--;

                        // e.g.     a b c d
                        //          1 2 3 4   ==> recent numbers
                        // and we were trying to access element e then
                        // e.g.     e b c d
                        //          4 1 2 3   ==> recent numbers
                    }
                }    
            }
        }

        // once hits and misses are calculated, we print the data
        printData(hit, miss);
    }

    // This is the one of the most important functions
    static void Direct_Cache(Vector<String> addresses)
    {
        // dc object stores the cache data
        DirectCache dc = new DirectCache();
        int hit = 0, miss = 0;

        // hit and miss are fount out in sequence
        for(String address : addresses)
        {
            String binary = HexaDecimal_To_Binary(address, 32);

            // byte off set is 2 bits. As a block is of 2^2 bytes.
            String SbyteOffSet = binary.substring(30,32);// not used
            
            // index is 16 bits
            String Sindex = binary.substring(14,30);
            
            // tag is 14 bits
            String Stag = binary.substring(0,14);

            // index is converted to decimal system.
            int index = Integer.parseInt(Sindex,2);

            // if V[index] is false then that means that the place is empty and has no data
            // so we can directly store the data and we set V[index] as true signifying that
            // some data is stored here. This is a case of miss as there is no data.
            if(dc.V[index] == false)
            {
                miss++;
                dc.V[index] = true;
                dc.tag[index] = Stag;
            }
            // if there is some data i.e. V[index] is true then we need to check the tag value if it is a miss or hit.
            else
            {
                // this condition will never be executed logically but we have put it here for safely
                if(dc.tag[index] == null)
                {
                    miss++;
                }
                else if(dc.tag[index].equals(Stag)) // if the tag is same the it is a hit
                {
                    hit++;
                }
                else// if the tag does not matches  the it is a miss
                {
                    miss++;
                    dc.tag[index] = Stag;
                }
            }
        }

        // once hits and misses are calculated, we print the data
        printData(hit, miss);
    }

    // hits and misses are passed and data is calculated and printed
    static void printData(int hits, int misses)
    {
        System.out.println("=> Hits   : " + hits);
        System.out.println("=> Misses : " + misses);

        // calculating the ratios  when hits and misses are given
        double hit_rate = ((double)hits)/(hits+misses);
        double miss_rate = ((double)misses)/(hits+misses);
        double hitToMiss = ((double)hits)/(misses);
        System.out.println("=> Hit  Rate : " + hit_rate);
        System.out.println("=> Miss Rate : " + miss_rate);
        System.out.println("=> Hit To Miss Rate : " + hitToMiss);
    }

    static void computation(String fileAddress,String fileName) throws Exception
    {
        // How to read file in java
        // Reference take from GeeksForGeeks
        File file1 = new File(fileAddress);
        BufferedReader br = new BufferedReader(new FileReader(file1));
        
        // this is a vector storing addresses from the files
        Vector<String> address = new Vector<String>();
        String st;
        while ((st = br.readLine()) != null) 
        {
            // st stores a line and the second word is the address in hexa-decimal
            String[] temp = st.split(" ");
            // second word at the index 1 is stored in address
            address.add(temp[1].substring(2));// substring(2) removes "0x" from the address
        }

        
        System.out.println(fileName);
        System.out.println();
        System.out.println("Accesses : " + address.size()+"\n");

        // Direct Map computations
        System.out.println("Direct Cache");
        Direct_Cache(address);

        // Set Associative Cache computations
        System.out.println("\nSet Associative Cache");
        Set_Associative_Cache(address);

        br.close();
    }
    
    public static void main(String args[]) throws Exception
    {
        // name of files that needs to be executed
        String[] fileName = {"gcc.trace","gzip.trace","mcf.trace","swim.trace","twolf.trace"};
        int count = 1;
        for(String temp : fileName)
        {
            System.out.print("\n" + count + ". ");

            // for computation and displaying data
            computation("../traces/" + temp,temp);
            count++;
        }
        //computation("../traces/test.trace","test.trace");
        // I was using the above statement to debug my code. I left it here if you want to use it.
        // just uncomment it and it can be used.
    }
}