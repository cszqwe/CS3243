import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GeneticAlgorithmMain {

    public static void main(String[] args) {


        // Create an initial population
        Population myPop = new Population(Population.DEFAULT_POPULATION, true);
    	//Population myPop = new Population("data/tpop50", Population.DEFAULT_POPULATION);
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        long time1 = System.currentTimeMillis();
        while (myPop.getFittest().getFitness() < 10000000 && generationCount <= 5000) {
        	long time2 = System.currentTimeMillis();
            System.out.println("Time " + (time2 - time1));
            time1 = time2;
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
            myPop = Algorithm.evolvePopulation(myPop);
            if (generationCount != 0 && generationCount % 10 == 0) {
            	try {
					Files.createDirectory(Paths.get("data"));
				} catch (IOException e) {
					//System.out.println("Error enter directory: " + e.getMessage());
				}
            	
            	myPop.saveToFile("data/pop" + generationCount);
            }
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        System.out.println(myPop.getFittest());

    }
}