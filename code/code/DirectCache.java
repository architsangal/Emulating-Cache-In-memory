class DirectCache
{
    // this could have been an array of objects but I though it is more relevent to have multiple arrays
    // each element of these array corresponds to the data at a particular index

    // V array here corresponds to the validity bit
    boolean[] V = new boolean[(int)Math.pow(2, 16)];

    // tag array stores the tag for the index given
    String[] tag = new String[(int)Math.pow(2, 16)];

    // could have been used for storing the data
    String[] data = new String[(int)Math.pow(2, 16)];// not required but declared 
}