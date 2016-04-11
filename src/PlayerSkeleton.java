import java.util.Arrays;


public class PlayerSkeleton {
	
	/**
	 * The feature used to evaluate the Heuristics function.
	 * 0-9 The number of each column.
	 * 10-18 The difference of height of adjacent column.
	 * 19 The maximum column height
	 * 20 The minimum column height
	 * 21 The difference between the maximum height and the minimum height
	 * 22 Number of holes.
	 * 23 Mean of height
	 * 24 Variation of height (sum of square of height of each column - mean) 
	 * 25 Score in this round
	 * 26 Lost or not
	 */
	static final int NUM_OF_FEATURES = 5;
	int firnessScore = -1;
	double featureFactor[] = new double[NUM_OF_FEATURES];
	//double weightVector[] = new double[NUM_OF_FEATURES];// The corresponding weightVector, which would be got by learning lots of game object.
	//-49.60378242198775&-10.245403765327232&-160.34072961864766&-1.9745173985707076&-11.484562814456758&-1.9743858472254128
	double weightVector[] = {-49.60378242198775, -10.245403765327232, -160.34072961864766, -1.9745173985707076, -11.484562814456758, -1.9743858472254128};
	/*
	 * FakeSateClass is a class which is similar to the State class. The reason to use such a class is 
	 * to simulate the make move function and get the field from the State.
	 */
	
    public static class FakeState {
    	public static final int COLS = 10;
    	public static final int ROWS = 21;
    	public static final int N_PIECES = 7;

    	//all legal moves - first index is piece type - then a list of 2-length arrays
    	protected static int[][][] legalMoves = new int[N_PIECES][][];
    	
    	//indices for legalMoves
    	public static final int ORIENT = 0;
    	public static final int SLOT = 1;
    	
    	//possible orientations for a given piece type
    	protected static int[] pOrients = {1,2,4,4,4,2,2};
    	
    	//the next several arrays define the piece vocabulary in detail
    	//width of the pieces [piece ID][orientation]
    	protected static int[][] pWidth = {
    			{2},
    			{1,4},
    			{2,3,2,3},
    			{2,3,2,3},
    			{2,3,2,3},
    			{3,2},
    			{3,2}
    	};
    	//height of the pieces [piece ID][orientation]
    	private static int[][] pHeight = {
    			{2},
    			{4,1},
    			{3,2,3,2},
    			{3,2,3,2},
    			{3,2,3,2},
    			{2,3},
    			{2,3}
    	};
    	private static int[][][] pBottom = {
    		{{0,0}},
    		{{0},{0,0,0,0}},
    		{{0,0},{0,1,1},{2,0},{0,0,0}},
    		{{0,0},{0,0,0},{0,2},{1,1,0}},
    		{{0,1},{1,0,1},{1,0},{0,0,0}},
    		{{0,0,1},{1,0}},
    		{{1,0,0},{0,1}}
    	};
    	private static int[][][] pTop = {
    		{{2,2}},
    		{{4},{1,1,1,1}},
    		{{3,1},{2,2,2},{3,3},{1,1,2}},
    		{{1,3},{2,1,1},{3,3},{2,2,2}},
    		{{3,2},{2,2,2},{2,3},{1,2,1}},
    		{{1,2,2},{3,2}},
    		{{2,2,1},{2,3}}
    	};
    	private int[][] field = new int[ROWS][COLS];
    	private int[] top = new int[COLS];
    	private int nextPiece;
    	private boolean lost;
    	private int turn;
    	private int clearedRowsLastTime; //The number of rows cleared by the last move.
        public FakeState(int nextPiece, int turn, int field[][], int top[]) {
            copyField(field);
            this.top = top.clone();
            this.nextPiece = nextPiece;
            this.turn = turn;
        }
        private void copyField(int[][] field) {
        	for (int i = 0 ; i< ROWS; i++){
        		for (int j = 0; j< COLS; j++){
        			this.field[i][j] = field[i][j];
        		}
        	}
        }
        // returns false if you lose - true otherwise
        public boolean makeMove(int orient, int slot) {
        	int piece = nextPiece;
            // height if the first column makes contact
            int height = top[slot] - pBottom[piece][orient][0];
            // for each column beyond the first in the piece
            for (int c = 1; c < pWidth[piece][orient]; c++) {
                height = Math.max(height, top[slot + c] - pBottom[piece][orient][c]);
            }

            // check if game ended
            if (height + pHeight[piece][orient] >= ROWS) {
            	lost = true;
                return false;
            }

            // for each column in the piece - fill in the appropriate blocks
            for (int i = 0; i < pWidth[piece][orient]; i++) {

                // from bottom to top of brick
                for (int h = height + pBottom[piece][orient][i]; h < height + pTop[piece][orient][i]; h++) {
                    field[h][i + slot] = turn;
                }
            }

            // adjust top
            for (int c = 0; c < pWidth[piece][orient]; c++) {
                top[slot + c] = height + pTop[piece][orient][c];
            }

            int rowsCleared = 0;

            // check for full rows - starting at the top
            for (int r = height + pHeight[piece][orient] - 1; r >= height; r--) {
                // check all columns in the row
                boolean full = true;
                for (int c = 0; c < COLS; c++) {
                    if (field[r][c] == 0) {
                        full = false;
                        break;
                    }
                }
                // if the row was full - remove it and slide above stuff down
                if (full) {
                    rowsCleared++;
                    // for each column
                    for (int c = 0; c < COLS; c++) {

                        // slide down all bricks
                        for (int i = r; i < top[c]; i++) {
                            field[i][c] = field[i + 1][c];
                        }
                        // lower the top
                        top[c]--;
                        while (top[c] >= 1 && field[top[c] - 1][c] == 0)
                            top[c]--;
                    }
                }
            }
            clearedRowsLastTime = rowsCleared;
            return true;
        }
        
    	public int[][] getField() {
    		return field;
    	}
    	public int getClearedRows(){
    		return clearedRowsLastTime;
    	}
    	
    	public int getColumnHeight(int columnID){
    		return top[columnID];
    	}
    	
    	public int getHoles(){
    		int totalGrid = 0;
    		int totalHeight = 0;
    		for (int i = 0; i< COLS; i++){
    			totalHeight += top[i];
    		}
    		for (int i = 0; i< ROWS; i++){
    			for (int j = 0; j< COLS; j++){
    				if (field[i][j] > 0) totalGrid++;
    			}
    		}
    		return totalHeight - totalGrid;
    	}
    	
    	public int getRoughness() {
    		int result = 0;
    		for (int i = 0; i < COLS-1; i++ )
    			result += Math.abs(top[i] - top[i+1]);
    		return result;
    	}
    	
    	public int getWalls(){
       		int result = 0;
    		for (int i = 1; i < COLS-1; i++ )
    			if ((top[i-1] - top[i] >= 2)&&(top[i+1] - top[i] >= 2))
    				result += Math.min(top[i-1] - top[i],top[i+1] - top[i]);
    		if (top[1] - top[0] >= 2) result += top[1] - top[0];
    		if (top[COLS-2] - top[COLS - 1] >= 2) result += top[COLS-2] - top[COLS - 1];
    		return result;
    		
    	}

    }
	
    /*
     * Constructor: Use a set of weightVector to initialize the weightVector
     */
    public PlayerSkeleton(double[] weightVector){
    	for (int i = 0 ; i < NUM_OF_FEATURES; i++ ){
    		this.weightVector[i] = weightVector[i];
    	}
    }

    public PlayerSkeleton(){
    	//Do nothing is no parameters provided.
    }
    
	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		int numOfChoice = legalMoves.length;
		double maxValue = 0;
		int currentChoice = -1;//-1 means have not choosed
		for (int i = 0; i < numOfChoice; i++){
			FakeState tmpState = new FakeState(s.getNextPiece(),s.getTurnNumber(),s.getField(),s.getTop());
			tmpState.makeMove(legalMoves[i][FakeState.ORIENT], legalMoves[i][FakeState.SLOT]);
			double tmpValue = evaluateState(tmpState);
			if (tmpValue > maxValue || currentChoice == -1){
				currentChoice = i;
				maxValue = tmpValue;
			}
		}
		return currentChoice;
	}
	
	/*
	 * The Heuristics function
	 * Get all the corresponding feature values based on the state.
	 */
	private double evaluateState(FakeState tmpState) {
		double sum = 0;
		double maximumHeight = 0;
		double minimumHeight = 10;
		double mean;
		double variation;
		for (int i = 0; i< 10; i++){
			//featureFactor[i] = tmpState.getColumnHeight(i);
			sum += tmpState.getColumnHeight(i);
			if (tmpState.getColumnHeight(i) > maximumHeight){
				maximumHeight = tmpState.getColumnHeight(i);
			}
			if (tmpState.getColumnHeight(i) < minimumHeight){
				minimumHeight = tmpState.getColumnHeight(i);
			}
		}
		mean = sum / 10;
		variation = 0;
		for (int i = 0; i< 10; i++){
			variation += (mean - tmpState.getColumnHeight(i))*(mean - tmpState.getColumnHeight(i));
		}
		for (int i = 10; i< 19; i++){
//			featureFactor[i] = Math.abs(featureFactor[i-10] - featureFactor[i-9]);
		}
		//featureFactor[0] = maximumHeight;
		//featureFactor[1] = maximumHeight - minimumHeight;
		featureFactor[0] = tmpState.getHoles();
		//featureFactor[3] = mean;
		featureFactor[1] = tmpState.clearedRowsLastTime;
		if (tmpState.lost) featureFactor[2] = 1; else featureFactor[2] = 0;
		featureFactor[3] = tmpState.getRoughness();
		featureFactor[4] = tmpState.getWalls();
		//featureFactor[5] = variation;
		double finalScore = 0;
		for (int i = 0; i< NUM_OF_FEATURES; i++){
			finalScore += featureFactor[i] * weightVector[i];
		}
		return finalScore;
	}


	public int fitnessValue(){
		State s = new State();
		//new TFrame(s);
		while(!s.hasLost()) {
			s.makeMove(this.pickMove(s,s.legalMoves()));
			//s.draw();
			//s.drawNext(0,0);
		}
		
		//System.out.println(s.getRowsCleared());
		return s.getRowsCleared();
	}
	
	
	
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}


	
}
