public class SimulatedAnnealing {
	
	double temp;
	double coolingRate;
	double changeRate = 1; // The amount of change.
	Individual individual;
	int index; //The index of trained factor.
	double initValue; //Init value of the trained factor.
    public  double acceptanceProbability(int oldFitness, int newFitness, double temperature) {
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
    	this.coolingRate = 0.03;
    }
    
    public void train() {

    	double currentValue = initValue;
    	int currentFitness = individual.getFitnessAvg();
    	while (temp > 1) {
        	
            individual.setWeight(index, currentValue + initValue * (changeRate));
        	int rightFitness = individual.getFitnessAvg();
        	individual.setWeight(index, currentValue - initValue * (changeRate));
        	int leftFitness = individual.getFitnessAvg();
        	if (rightFitness > leftFitness){
	            if (acceptanceProbability(currentFitness, rightFitness, temp) > Math.random()) {
	                individual.setWeight(index, currentValue + initValue * (changeRate));
	                currentValue = rightFitness;
	            }
        	}else{
	            if (acceptanceProbability(currentFitness, rightFitness, temp) > Math.random()) {
	                individual.setWeight(index, currentValue + initValue * (changeRate));
	                currentValue = rightFitness;
	            }        		
        	}
            // Keep track of the best solution found
            
            // Cool system
            temp *= 1-coolingRate;
            changeRate *= 1-coolingRate;
        }

    }
}