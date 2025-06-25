import java.io.*;

/**
 * Needleman-wunsch Sequence Allignment Algorithm
 * 
 * @version 6/24/2025
 * @author Lorenzo Canali
 */

class NW_SeqAllignAlg {
    static int G = -1; //gap penalty
    static int matchScore =  1; //match score
    static int mismatchScore = -1; //mismatch score

    private enum Direction {DIAG, UP, LEFT} // for traceback

    /**
     * s Function (substitution): match = matchScore, else mismatchScore
     */
    static int sFunction(int a, int b) {
        return (a == b) ? matchScore : mismatchScore;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data.in"));
        PrintWriter pw = new PrintWriter(new FileWriter("data.out"));

        char[] xSeq = br.readLine().toCharArray();
        char[] ySeq = br.readLine().toCharArray();

        int rows = ySeq.length + 1;
        int cols = xSeq.length + 1;

        int[][] score = new int[rows][cols]; //The Score Matrix
        Direction[][] trace = new Direction[rows][cols]; //The Traceback Matrix

        score[0][0] = 0; //Equals 0 because it is the start cell
        trace[0][0] = null; //Derived from no where because it is the start cell
        for (int i = 1; i < rows; i++) {
            score[i][0] = i * G;
            trace[i][0] = Direction.UP;
        }
        for (int j = 1; j < cols; j++) {
            score[0][j] = j * G;
            trace[0][j] = Direction.LEFT;
        }
        
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                // fFunction
                int diagnol = score[i - 1][j - 1] + sFunction(xSeq[j - 1], ySeq[i - 1]); // F(i - 1, j - 1) + S(xSeq[j - 1], ySeq[i - 1])
                int up = score[i - 1][j] + G; // F(i - 1, j) + G
                int left = score[i][j - 1] + G; // F(i, j - 1) + G

                int best = diagnol;
                Direction direction = Direction.DIAG;

                if (up > best) {
                    best = up;
                    direction = Direction.UP;
                }
                if (left > best) {
                    best = left;
                    direction = Direction.LEFT;
                }

                score[i][j] = best;
                trace[i][j] = direction;
            }
        }

        //Now traceback from bottom-right
        StringBuilder a1 = new StringBuilder();
        StringBuilder a2 = new StringBuilder();
        int i = rows - 1;
        int j = cols - 1;

        while (i > 0 || j > 0) {
            Direction d = trace[i][j];

            if (d == Direction.DIAG) {
                //No Gap
                a1.append(xSeq[j-1]);
                a2.append(ySeq[i-1]);
                i--; 
                j--;
            } 

            else if (d == Direction.LEFT) {
                a1.append(xSeq[j-1]);
                //Gap in Seqeunce Two if it is left
                a2.append('-');
                j--;
            } 

            else { //Up
                //Gap in Sequence One if it is up
                a1.append('-');
                a2.append(ySeq[i-1]);
                i--;
            }
        }

        //Reverse and print
        pw.println(a1.reverse().toString());
        pw.println(a2.reverse().toString());
        pw.flush();
        br.close();
        pw.close();
    }
}