import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GeneticAlgorithmMain {

    public static void main(String[] args) {


        // Create an initial population
        Population myPop = new Population(50, true);
        
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        while (myPop.getFittest().getFitness() < 1000) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
            myPop = Algorithm.evolvePopulation(myPop);
            if (generationCount != 0 && generationCount % 100 == 0) {
            	try {
					Files.createDirectory(Paths.get("data"));
				} catch (IOException e) {
					System.out.println("Error enter directory: " + e.getMessage());
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