import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NQueens {
    private static boolean hasConflicts = false;
    static int n;
    static int[] queens;
    static int[] rows;
    static int[] diagonals;
    static int[] reverseDiagonals;

    static void putQueens() {
        int col = 1;
        for (int row = 0; row < n; row++) {
            queens[col] = row;
            rows[row]++;
            diagonals[col - row + n - 1]++;
            reverseDiagonals[col + row]++;

            col += 2;
            if (col >= n) {
                col = 0;
            }
        }
    }

    static void solve() {
        int iter = 0;
        int k = 1;
        int col, row;

        while (iter++ <= k * n) {
            col = findColumnOfQueenWithMaxConflicts();
            if (!hasConflicts) {
                break;
            }
            row = findRowWithMinConflicts(col);
            updateState(row, col);
        }

        //restart solving if there are still conflicts after k * n steps
        if (hasConflicts) {
            putQueens();
            solve();
        }
    }


    static int findColumnOfQueenWithMaxConflicts() {
        int maxConflicts = -1;
        List<Integer> colsWithMaxConflicts = new ArrayList<>();

        int curRow, curConflicts;

        for (int curCol = 0; curCol < n; curCol++) {
            curRow = queens[curCol];
            //we are counting the current queen 3 times - these are not conflicts, so we should subtract
            curConflicts = rows[curRow] + diagonals[curCol - curRow + n - 1] + reverseDiagonals[curCol + curRow] - 3;
            if (curConflicts == maxConflicts) {
                colsWithMaxConflicts.add(curCol);
            } else if (curConflicts > maxConflicts) {
                maxConflicts = curConflicts;
                colsWithMaxConflicts.clear();
                colsWithMaxConflicts.add(curCol);
            }
        }

        if (maxConflicts == 0) {
            hasConflicts = false;
        }

        Random rand = new Random();
        int randIndex = rand.nextInt(colsWithMaxConflicts.size());

        return colsWithMaxConflicts.get(randIndex);
    }

    static int findRowWithMinConflicts(int col) {
        int minConf = n + 1;
        List<Integer> rowsWithMinConf = new ArrayList<>();

        int curConflict;

        for (int curRow = 0; curRow < n; curRow++) {
            // if there is a queen
            if (queens[col] == curRow) {
                curConflict = rows[curRow] + diagonals[col - curRow + n - 1] + reverseDiagonals[col + curRow] - 3;
            }
            //else - no queen
            else {
                curConflict = rows[curRow] + diagonals[col - curRow + n - 1] + reverseDiagonals[col + curRow];
            }

            if (curConflict == minConf) {
                rowsWithMinConf.add(curRow);
            } else if (curConflict < minConf) {
                minConf = curConflict;
                rowsWithMinConf.clear();
                rowsWithMinConf.add(curRow);
            }
        }

        Random rand = new Random();
        int randIndex = rand.nextInt(rowsWithMinConf.size());

        return rowsWithMinConf.get(randIndex);
    }

    static void updateState(int row, int col) {
        //decrease number of queens for previous position
        int prevRow = queens[col];
        rows[prevRow]--;
        diagonals[col - prevRow + n - 1]--;
        reverseDiagonals[col + prevRow]--;

        //increase number of queens for new position
        queens[col] = row;
        rows[row]++;
        diagonals[col - row + n - 1]++;
        reverseDiagonals[col + row]++;
    }

    static void printQueens() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (queens[j] == i) {
                    System.out.print("*");
                } else {
                    System.out.print("_");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();

        queens = new int[n];
        rows = new int[n];
        diagonals = new int[2 * n - 1];
        reverseDiagonals = new int[2 * n - 1];

        hasConflicts = true;

        long startTime = System.nanoTime();

        putQueens();
        solve();

        long estimatedTime = System.nanoTime() - startTime;

        if (n < 50) {
            printQueens();
        }

        System.out.println("Estimated time: " + TimeUnit.NANOSECONDS.toMillis(estimatedTime) + " milliseconds");
    }
}
