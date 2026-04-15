
public class Main {
    static int[][] board = new int[9][9];
    static boolean[][] rowUsed = new boolean[9][10];
    static boolean[][] colUsed = new boolean[9][10];
    static boolean[][] boxUsed = new boolean[9][10];
    static int numOfBoards = 0;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long duration = 1000;
        
        while (System.currentTimeMillis() - startTime < duration) {
            generate(0, 0);
            resetState();
        }
        
        System.out.println("Total boards generated in 1 second: " + numOfBoards);
    }
    
    public static void resetState() {
        board = new int[9][9];
        rowUsed = new boolean[9][10];
        colUsed = new boolean[9][10];
        boxUsed = new boolean[9][10];
    }    

    public static boolean generate(int row, int col) {
        final int[] nums = randomNumbers();
        int boxIndex = (row / 3) * 3 + (col / 3);
        if (row == 9) {
            printBoard();
            numOfBoards++;
            return true;
        }
        for (int num : nums) {
            if (numValid(row, col, num)) {
                board[row][col] = num;
                rowUsed[row][num] = true;
                colUsed[col][num] = true;
                boxUsed[boxIndex][num] = true;

                if (moveToNextCell(row, col)) {
                    return true;
                }

                rowUsed[row][num] = false;
                colUsed[col][num] = false;
                boxUsed[boxIndex][num] = false;
            }
        }
        return false;
    }

    public static void removeCell(int row, int col) {
        int num = board[row][col];
        int boxIndex = (row / 3) * 3 + (col / 3);
        board[row][col] = 0;
        rowUsed[row][num] = false;
        colUsed[col][num] = false;
        boxUsed[boxIndex][num] = false;
    }

    public static int countSolutions(int count) {
        int[] cell = nextEmptyCell();
        int[] nums = randomNumbers();
        int boxIndex = (cell[0] / 3) * 3 + (cell[1] / 3);
        for (int num : nums) {
            if (numValid(cell[0], cell[1], num)) {
                board[cell[0]][cell[1]] = num;
                rowUsed[cell[0]][num] = true;
                colUsed[cell[1]][num] = true;
                boxUsed[boxIndex][num] = true;

                

                rowUsed[cell[0]][num] = false;
                colUsed[cell[1]][num] = false;
                boxUsed[boxIndex][num] = false;
            }
        }
        return count;
    }

    public static int[] nextEmptyCell() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static boolean moveToNextCell(int row, int col) {
        if (col == 8) {
            return generate(row + 1, 0);
        } else {
            return generate(row, col + 1);
        }
    }

    public static void printBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean numValid(int row, int col, int num) {
        int boxIndex = (row / 3) * 3 + (col / 3);

        return !rowUsed[row][num] &&
                !colUsed[col][num] &&
                !boxUsed[boxIndex][num];
    }

    public static int[] randomNumbers() {
        int[] nums = new int[9];
        for (int i = 0; i < 9; i++) {
            nums[i] = i + 1;
        }

        for (int i = 0; i < 9; i++) {
            int randIndex = (int) (Math.random() * 9);
            int temp = nums[i];
            nums[i] = nums[randIndex];
            nums[randIndex] = temp;
        }

        return nums;
    }
}
