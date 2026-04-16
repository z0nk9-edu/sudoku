
import java.util.Scanner;

public class Main {
    static int[][] board = new int[9][9];
    static boolean[][] rowUsed = new boolean[9][10];
    static boolean[][] colUsed = new boolean[9][10];
    static boolean[][] boxUsed = new boolean[9][10];
    static int numOfBoards = 0;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Would you like to benchmark the generator? (y/n)");
        String benchmarkString = input.nextLine();
        if (benchmarkString.equalsIgnoreCase("y")) {
            System.out.println("How long would you like to run for in milliseconds?");
            long duration = input.nextLong();
            input.nextLine();
            System.out.println("Do you want cells to be removed during benchmarking? (y/n)");
            String removeString = input.nextLine();
            boolean willRemove = removeString.equalsIgnoreCase("y");
            System.out.println("Do you want to print the boards? (y/n)");
            String printString = input.nextLine();
            boolean willPrint = printString.equalsIgnoreCase("y");
            long startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime < duration) {
                generate(0, 0);
                if (willRemove) removeCells();
                if (willPrint) printBoard();
                resetState();
            }

            System.out.println("Total boards generated: " + numOfBoards);
            System.out.println("Average time to generate a board (milliseconds): " + (double) duration / numOfBoards);
        }
        System.out.println("Press enter to continue.");
        input.nextLine();
        System.out.println(
                "This is my sudoku game. There is no undo so please try to avoid guessing. \nThere is guarunteed to be exactly one unique solution to every board.");
        resetState();
        generate(0, 0);
        removeCells();
        printBoard();
        while (nextEmptyCell() != null) {
            System.out.println("Enter the row you want to edit or 0 to stop");
            int row = input.nextInt();
            if (row == 0)
                break;
            System.out.println("Enter the column you want to edit");
            int col = input.nextInt();
            if (board[row - 1][col - 1] != 0) {
                System.out.println("This space has been filled.");
                continue;
            }
            if (!inBounds(row, col)) {
                System.out.println("Out of bounds.");
                continue;
            }
            System.out.println("Enter the number you want to insert");
            int num = input.nextInt();
            if (numValid(row - 1, col - 1, num)) {
                addCell(row - 1, col - 1, num);
                printBoard();
            } else {
                System.out.println("Invalid number!");
            }
        }
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
        int[] nums = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        int count = 0;
        for (int num : nums) {
            if (cell == null) {
                return 1;
            }
            if (numValid(cell[0], cell[1], num)) {
                addCell(cell[0], cell[1], num);
                count += countSolutions();
                removeCell(cell[0], cell[1]);
                if (count > 1)
                    return count;
            }
        }
        return count;
    }

    public static int[] nextEmptyCell() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    return new int[] { i, j };
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
            if (i % 3 == 0 && i != 0) {
                System.out.println("---------------------");
            }

            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }

                if (board[i][j] == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
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

    public static boolean inBounds(int row, int col) {
        return row > 0 && row < 10 && col > 0 && col < 10;
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
