
public class Algorithm {

    /* GA parameters */
	private static final double crossoverRate = 0.6;
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;
    private static final int elitismSize = 3;

    /* Public methods */
    
    // Evolve a population
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.size(), false);
        //long startMili=System.currentTimeMillis();
        // Keep our best individual
        if (elitism) {
        	Individual[] fittests = pop.getFittests(elitismSize);
        	for (int i = 0; i < fittests.length; ++i) {
        		Individual item = fittests[i];
        		newPopulation.saveIndividual(i, item);
        	}
        }
        //long time1 = System.currentTimeMillis();
        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = elitismSize;
        } else {
            elitismOffset = 0;
        }
        // Loop over the population size and create new individuals with
        // crossover
        int i;
        for (i = elitismOffset; i < pop.size() - 1; i+=2) {
            Individual indiv1 = new Individual(tournamentSelection(pop));
            Individual indiv2 = new Individual(tournamentSelection(pop));
            crossover(indiv1, indiv2);
            //newIndiv.updateWithGenes();
            newPopulation.saveIndividual(i, indiv1);
            newPopulation.saveIndividual(i+1, indiv2);
        }
        while (i < pop.size()) {
        	newPopulation.saveIndividual(i, new Individual(tournamentSelection(pop)));
        	++i;
        }
        //long time2=System.currentTimeMillis();

        // Mutate population
        for (i = elitismOffset; i < newPopulation.size(); i++) {
            //mutate(newPopulation.getIndividual(i));
        	mutateSA(newPopulation.getIndividual(i));
            //newPopulation.getIndividual(i).updateWithGenes();
        }
        //long time3=System.currentTimeMillis();
        //System.out.println(startMili);
        //System.out.println(time1);
        //System.out.println(time2);
        //System.out.println(time3);
        //newPopulation.resetFitness(1);
        //System.out.println(" Time 1: " + (time1 - startMili) + " Time 2: " + (time2 - time1) + " Time 3: " + (time3 - time2));
        return newPopulation;
    }

    // Crossover individuals
    private static void crossover(Individual indiv1, Individual indiv2) {
    	if (Math.random() >= crossoverRate) {
    		return;
    	}
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() < uniformRate) {
            	double weight = indiv1.getWeight(i);
            	indiv1.setWeight(i, indiv2.getWeight(i));
            	indiv2.setWeight(i, weight);
            }
        }
    }

    // Mutate an individual
    private static void mutate(Individual indiv) {
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() < mutationRate) {
                    double randomVal = (Math.random() - 0.5) * Math.random() * 4000;
                    double weight = indiv.getWeight(i) + randomVal;
                    indiv.setWeight(i, weight);
                   // putFloat(genes, randomVal, i * 8);
            }
        }
        //indiv.updateGenes();
    }

    // Mutate an individual with Simulated Annealing
    private static void mutateSA(Individual indiv) {
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() < mutationRate) {
                    SimulatedAnnealing sa = new SimulatedAnnealing(indiv , i);
                   // putFloat(genes, randomVal, i * 8);
            }
        }
        //indiv.updateGenes();
    }
    
    // Select individuals for crossover
    private static Individual tournamentSelection(Population pop) {
    	
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        Individual fittest = tournament.getFittest();
        return fittest;
    }
}