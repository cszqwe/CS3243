
public class Individual implements Runnable, Comparable<Individual> {
    static int defaultGeneLength = PlayerSkeleton.NUM_OF_FEATURES;
    private double[] weightVector = new double[PlayerSkeleton.NUM_OF_FEATURES];
    private PlayerSkeleton player;
    // The fitness value for this individual
    private int fitness = -1;
    private int fitnessAvg = -1;
    Individual() {
    	
    }
    
    Individual(Individual other) { //deep copy
    	player = other.player;
    	fitness = other.fitness;
    	for(int i = 0; i < weightVector.length; ++i) {
    		this.weightVector[i] = other.weightVector[i];
    	}
    }
    
    
    public int size(){
    	return defaultGeneLength;
    }
    
    public void setWeight(int index, double value){
    	weightVector[index] = value;
    	reset();
    }
    
    public double getWeight(int index){
    	return weightVector[index];
    }
    

    // Create a random individual
    public void generateIndividual() {
        for (int i = 0; i < PlayerSkeleton.NUM_OF_FEATURES; i++) {
            double randomVal = (Math.random() - 0.5) * 100;
            weightVector[i] = randomVal;
        }
        player = new PlayerSkeleton(weightVector);
    }
    
    
    public void importFromString(String str) {
    	String[] nums = str.split("&");
    	if (nums.length != PlayerSkeleton.NUM_OF_FEATURES) {
    		System.out.println("Incompatible version");
    		generateIndividual();
    		return;
    	}
    	
    	for (int i = 0; i < PlayerSkeleton.NUM_OF_FEATURES; i++) {
    		weightVector[i] = Double.parseDouble(nums[i]);
    	}
    	
    	
    	player = new PlayerSkeleton(weightVector);
    }
    
    @Override
    public String toString() {
    	String[] strs = new String[PlayerSkeleton.NUM_OF_FEATURES];
    	for (int i = 0; i < PlayerSkeleton.NUM_OF_FEATURES; i++) {
    		strs[i] = String.valueOf(weightVector[i]);
    	}
		return String.join("&", strs);
    }

    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }
    
    public int getFitness() {
    	if (player == null){
            player = new PlayerSkeleton(weightVector);
    	}
        if (fitness == -1) {
        	//long time1 = System.currentTimeMillis();
            //fitness = (player.fitnessValue() + player.fitnessValue() + player.fitnessValue())/3;
            fitness = player.fitnessValue();
        	//long time2 = System.currentTimeMillis();
            //System.out.println("Time " + (time2 - time1));
        }else{
        	return fitness;
        }
        return fitness;
    }
    
    public int getFitnessAvg() {
    	if (player == null){
            player = new PlayerSkeleton(weightVector);
    	}
        if (fitnessAvg == -1) {
        	//long time1 = System.currentTimeMillis();
            //fitness = (player.fitnessValue() + player.fitnessValue() + player.fitnessValue())/3;
            fitnessAvg = (player.fitnessValue() + player.fitnessValue() + player.fitnessValue());
        	//long time2 = System.currentTimeMillis();
            //System.out.println("Time " + (time2 - time1));
        }else{
        	return fitnessAvg;
        }
        return fitnessAvg;
    }
    
    public int getFitForSa(){
    	if (player == null){
            player = new PlayerSkeleton(weightVector);
    	}
    	return this.player.fitnessValue();
    }
    
    public void reset() {
    	player = null;
    	fitness = -1;
    }

	@Override
	public void run() {
		getFitness();
	}

	@Override
	public int compareTo(Individual other) {		
		return other.getFitness() - this.getFitness();
	}


}

