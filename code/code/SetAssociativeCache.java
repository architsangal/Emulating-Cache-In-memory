class SetAssociativeCache
{
    // valid bit
    boolean[][] V = new boolean[(int)Math.pow(2, 14)][4];
    
    // tag bits
    String[][] tag = new String[(int)Math.pow(2, 14)][4];
    
    // stores the recent numbers corresponding to the logic given below.
    // This helps in LRU policy.
    int[][] LRU = new int[(int)Math.pow(2, 14)][4];// for LRU
    /*
        tag[index] = { ABCH , ERTY , QTYE , WTYU}
        LRU[index] = {  1   ,  2   ,  4   ,  3  }
        ABCH corresponds to 1
        ERTY corresponds to 2
        QTYE corresponds to 4
        WTYU corresponds to 3

        this means that ABCH is LRU and QTYE is most recently used. According to LRU replacement policy
        we will replace ABCH if we need to.

        Here, above 1,2,3,4 are called as recent numbers.

        if the recent number is 0 then there is no element added
    */

    // could have been used for storing the data
    String[][] data = new String[(int)Math.pow(2, 14)][4];// not required but declared 
}
