public class SimulatedAnnealing {
	
	double temp;
	double coolingRate;
	double changeRate = 1; // The amount of change.
	Individual individual;
	Algorithm ga;
	int index; //The index of trained factor.
	double initValue; //Init value of the trained factor.
    public  double acceptanceProbability(int oldFitness, int newFitness, double temperature) {
        // If the new solution is better, accept it
        if (newFitness > oldFitness) {
            return 1.0;
        }
        return Math.exp((newFitness - oldFitness) / temperature);
    }
    public  static double acceptanceProbabilityStatic(int oldFitness, int newFitness, double temperature) {
        // If the new solution is better, accept it
        if (newFitness > oldFitness) {
            return 1.0;
        }
        return Math.exp((newFitness - oldFitness) / temperature);
    }
    public SimulatedAnnealing(double coolingRate, double temp, Individual individual){
    	this.coolingRate = coolingRate;
    	this.temp = temp;
    	this.individual = individual;
    }
    
    public SimulatedAnnealing(Individual individual, int index){
    	this.individual = individual;
    	this.temp = 10000;
    	this.coolingRate = 0.3;
    	this.index = index;
    }
    
    public void setInitValue(double initValue){
    	this.initValue = initValue;
    }
    public void train() {
//    	double randomVal = (Math.random() - 0.5) * Math.random() * 500;
//    	individual.setWeight(index, randomVal);
    	initValue = (Math.random() - 0.5) * Math.random() * 500;
    	double currentValue = individual.getWeight(index);
    	int currentFitness = individual.getFitForSa();
    	while (temp > 1) {
        	
            individual.setWeight(index, currentValue + initValue * (changeRate));
        	int rightFitness = individual.getFitForSa();
        	individual.setWeight(index, currentValue - initValue * (changeRate));
        	int leftFitness = individual.getFitForSa();
        	if (rightFitness > leftFitness){
	            if (acceptanceProbability(currentFitness, rightFitness, temp) > Math.random()) {
	                individual.setWeight(index, currentValue + initValue * (changeRate));
	                currentValue = currentValue + initValue * (changeRate);
	                currentFitness = rightFitness;
	            }
        	}else{
	            if (acceptanceProbability(currentFitness, leftFitness, temp) > Math.random()) {
	                individual.setWeight(index, currentValue - initValue * (changeRate));
	                currentValue = currentValue - initValue * (changeRate);
	                currentFitness = leftFitness;
	            }        		
        	}
            // Keep track of the best solution found
            
            // Cool system
            temp *= 1-coolingRate;
            changeRate *= 1-coolingRate;
        }

    }
    public static void trainGA() {
//    	double randomVal = (Math.random() - 0.5) * Math.random() * 500;
//    	individual.setWeight(index, randomVal);
    	double initValue = 1;
    	double currentValue = 0.015;
    	Algorithm ga = new Algorithm();
    	int currentFitness = ga.fitness();
    	double temp = 10000;
    	double changeRate = 1;
    	double left = 0;
    	double right = 0;
    	while (temp > 1) {
        	right = currentValue + initValue * (changeRate);
        	if (right > 1) right = 1;
            ga.setRate(right);
        	int rightFitness = ga.fitness();
        	left = currentValue - initValue * (changeRate);
        	if (left < 0) left = 0;
        	ga.setRate(left);
        	int leftFitness = ga.fitness();
        	if (rightFitness > leftFitness){
	            if (acceptanceProbabilityStatic(currentFitness, rightFitness, temp) > Math.random()) {
	            	ga.setRate(right);
	            	currentFitness = leftFitness;
	                currentValue = right;
	            }
        	}else{
	            if (acceptanceProbabilityStatic(currentFitness, leftFitness, temp) > Math.random()) {
	            	ga.setRate(left);
	                currentFitness = leftFitness;
	                currentValue = left;
	            }        		
        	}
            // Keep track of the best solution found
            
            // Cool system
            temp *= 1-0.03;
            changeRate *= 1-0.03;
            System.out.println(currentFitness+ " "+ currentValue);
        }

    }
    public static void main(String args[]){
    	trainGA();
    	
    }
}