
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
            removeCells();
            printBoard();
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
        if (row == 9) {
            numOfBoards++;
            return true;
        }
        for (int num : nums) {
            if (numValid(row, col, num)) {
                addCell(row, col, num);
                if (moveToNextCell(row, col)) {
                    return true;
                }
                removeCell(row, col);
            }
        }
        return false;
    }

    public static void removeCells() {
        int row = 0, col = 0, num = 0;
        while (countSolutions() == 1) {
            row = (int) (Math.random() * 9);
            col = (int) (Math.random() * 9);
            while (board[row][col] == 0) {
                row = (int) (Math.random() * 9);
                col = (int) (Math.random() * 9);
            }
            num = removeCell(row, col);
        }
        addCell(row, col, num);
    }

    public static int countSolutions() {
        int[] cell = nextEmptyCell();
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int count = 0;
        for (int num : nums) {
            if (cell == null) {
                return 1;
            }
            if (numValid(cell[0], cell[1], num)) {
                addCell(cell[0], cell[1], num);
                count += countSolutions();
                removeCell(cell[0], cell[1]);
                if (count > 1) return count;
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

    public static int removeCell(int row, int col) {
        int num = board[row][col];
        int boxIndex = (row / 3) * 3 + (col / 3);
        board[row][col] = 0;
        rowUsed[row][num] = false;
        colUsed[col][num] = false;
        boxUsed[boxIndex][num] = false;
        return num;
    }

    public static void addCell(int row, int col, int num) {
        int boxIndex = (row / 3) * 3 + (col / 3);
        board[row][col] = num;
        rowUsed[row][num] = true;
        colUsed[col][num] = true;
        boxUsed[boxIndex][num] = true;
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
