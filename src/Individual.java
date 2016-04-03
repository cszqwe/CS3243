
public class Individual {
    static int defaultGeneLength = PlayerSkeleton.NUM_OF_FEATURES * 8;
    private byte[] genes = new byte[defaultGeneLength];
    private double[] weightVector = new double[PlayerSkeleton.NUM_OF_FEATURES];
    private PlayerSkeleton player;
    // The fitness value for this individual
    private int fitness = 0;
    
    public int size(){
    	return defaultGeneLength;
    }
    
    public byte getGene(int i){
    	return genes[i];
    }
    
    public void setGene(int index, byte val){
    	genes[index] = val;
    }
    /*
     * Function used to convert the byte to double.
     */
	public static double getDouble(byte[] b, int index) {
		long l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		l &= 0xffffffffl;
		l |= ((long) b[index + 4] << 32);
		l &= 0xffffffffffl;
		l |= ((long) b[index + 5] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) b[index + 6] << 48);
		l &= 0xffffffffffffffl;
		l |= ((long) b[index + 7] << 56);
		return Double.longBitsToDouble(l);
	}
	public static void putFloat(byte[] genes, double x, int index) {
		// byte[] b = new byte[4];
		long l = Double.doubleToLongBits(x);
		for (int i = 0; i < 8; i++) {
			genes[index + i] = new Long(l).byteValue();
			l = l >> 8;
		}
	}
    // Create a random individual
    public void generateIndividual() {
    	
    	
        for (int i = 0; i < PlayerSkeleton.NUM_OF_FEATURES; i++) {
            double randomVal = (Math.random() - 0.5) * 100;
            weightVector[i] = randomVal;
            putFloat(genes, randomVal, i * 8);
        }
        for (int i = 0 ; i< PlayerSkeleton.NUM_OF_FEATURES; i++){
        	weightVector[i] = getDouble(genes,i * 8);
        }
        player = new PlayerSkeleton(weightVector);
    }

    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }
    
    public int getFitness() {
    	if (player == null){
            for (int i = 0 ; i< PlayerSkeleton.NUM_OF_FEATURES; i++){
            	weightVector[i] = getDouble(genes,i * 8);
            }
            player = new PlayerSkeleton(weightVector);
    	}
        if (fitness == 0) {
            fitness = player.fitnessValue();
        }
        return fitness;
    }


}