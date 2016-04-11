import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Population {

	public static final int DEFAULT_POPULATION = 50;
	
    Individual[] individuals;
    
    /*
     * Constructors
     */
    // Create a population
    public Population(int populationSize, boolean initialise) {
        individuals = new Individual[populationSize];
        // Initialise population
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < size(); i++) {
                Individual newIndividual = new Individual();
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
            }
        }
    }
    
    public Population(String filepath, int populationSize) {
    	try {
			List<String> lines = Files.readAllLines(Paths.get(filepath), StandardCharsets.US_ASCII);
			individuals = new Individual[populationSize];
	    	for (int i = 0; i < Math.min(size(), lines.size()); i++) {
	            Individual newIndividual = new Individual();
	            newIndividual.importFromString(lines.get(i));
	            saveIndividual(i, newIndividual);
	        }
	    	for (int i = Math.min(size(), lines.size()); i < size(); i++) {
	    		Individual newIndividual = new Individual();
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
	    	}

    	} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			return;
		}
    }
    
    public void saveToFile(String filepath) {
    	List<String> lines = new ArrayList<String>();
    	for (int i = 0; i < size(); i++) {
            lines.add(individuals[i].toString());
        }
    	try {
			Files.write(Paths.get(filepath), lines, StandardCharsets.US_ASCII);
		} catch (IOException e) {
			System.out.println("Error writing file: " + e.getMessage());
		}
    }

    /* Getters */
    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public Individual getFittest() {
        Individual fittest = individuals[0];
        Thread[] threads= new Thread[size()];
        // Loop through individuals to find fittest
        
        for (int i = 0; i < size(); i++) {
            threads[i] = new Thread(getIndividual(i));
            threads[i].start();
        }
        for (int i = 0; i< size(); i++){
        	try{
        		threads[i].join();
        	}catch (Exception e) {
                System.out.println("Exception from " + i + ".run");
            }
        }
        
        for (int i = 0 ; i < size(); i++){
        	if (fittest.getFitness() < getIndividual(i).getFitness()){
        		fittest = getIndividual(i);
        	}
        }
        
        return fittest;
    }
    
    public void sort() {
    	Arrays.sort(individuals);
    }
    
    public Individual[] getFittests(int size) {
        Thread[] threads= new Thread[size()];
        // Loop through individuals to find fittest
        
        for (int i = 0; i < size(); i++) {
            threads[i] = new Thread(getIndividual(i));
            threads[i].start();
        }
        for (int i = 0; i< size(); i++){
        	try{
        		threads[i].join();
        	}catch (Exception e) {
                System.out.println("Exception from " + i + ".run");
            }
        }
        
        sort();
        Individual[] fittests = Arrays.copyOfRange(individuals, 0, size);
        
        return fittests;
    }
    
    public void resetFitness(int superElite) {
    	for (int i = superElite; i < size(); i++) {
        	individuals[i].reset();
        }
    }
    
    /* Public methods */
    // Get population size
    public int size() {
        return individuals.length;
    }

    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }
}