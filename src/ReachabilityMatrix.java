import java.util.*;
public class ReachabilityMatrix {
  public static void main(String[] args){
      ArrayList<int[][]> matrices = new ArrayList<>();
      java.util.Scanner scan = new java.util.Scanner(System.in);

     // MAIN MENU
      boolean runMenu = true;
      while(runMenu){
          try{
              System.out.print("""
                      
                      ------MAIN MENU------
                      1. Enter graph data
                      2. Print outputs
                      3. Exit program
                      
                      Enter option number:\s""");
              int input = Integer.parseInt(scan.nextLine());

              // in case user inputs an option not on menu
              if (input < 1 || input > 3)
                  System.out.println("Invalid menu option. Please choose between options 1 - 3.");
              if(input == 2 && matrices.isEmpty())
                  System.out.println("** Error ** Your matrix is empty. Try inputting data first.");
              else{
                  switch(input){
                      /* Enter Graph Data */
                      case 1:
                          // clear list data for new matrix data
                          matrices.clear();

                          // user inputs node counts. Checks for valid node count.
                          System.out.print("How many nodes will your matrix have? (no more than 5): ");
                          int nodes = Integer.parseInt(scan.nextLine());
                          while(nodes < 1 || nodes > 5){
                              System.out.print("Invalid node count. Enter a node count between 1 - 5: ");
                              nodes = Integer.parseInt(scan.nextLine());
                          }

                          // user populates initial matrix data; matrix added to 'matrices' list
                          int[][] A1 = new int[nodes][nodes];
                          for(int i = 0; i < nodes; i++){
                              for(int j = 0; j < nodes; j++){
                                  System.out.print("Enter A1 ["+i+","+j+"]: ");
                                  A1[i][j] = Integer.parseInt(scan.nextLine());
                              }
                          }
                          matrices.add(A1);
                          break;

                      /* Print outputs */
                      case 2:
                          printOutputs(matrices);
                          break;

                      /* Exit */
                      case 3:
                          System.out.println("Exiting...");
                          runMenu = false;
                  }
              }
          }catch(NumberFormatException e){
              System.out.println("Invalid menu option. Please choose between options 1 - 3.");
          }catch(NoSuchElementException e){

          }
      }

  }

    /* print any 2D array */
    public static void printMatrix(int[][] matrix){
        for(int[] row: matrix){
            for(int i : row) {
                System.out.printf(i+"\t\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    /* print all outputs */
    public static void printOutputs(ArrayList<int[][]> matrices){
        System.out.println("Input Matrix:");
        printMatrix(matrices.getFirst());
        System.out.println("Reachability Matrix:");
        reachabilityMatrix(matrices);
        inDegree(matrices);
        outDegree(matrices);
        selfLoops(matrices);
        cyclesLengthN(matrices);
        paths1Edge(matrices);
        pathsNEdges(matrices);
        paths1ToNEdges(matrices);
        cyclesLength1ToN(matrices);
    }

    /* calculates/adds all matrices A2 to AN to the primary list
       calculates/adds reachability matrix to the primary list */
    public static void reachabilityMatrix(ArrayList<int[][]> list){
        int[][] A1 = list.getFirst();
        int n = A1.length;

        // incrementally calculating each matrix up to 'AN'
        for(int listIndex = 0; listIndex < n-1; listIndex++){
            int[][] temp = list.get(listIndex);
            int[][] AN = new int[n][n];
            for(int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        AN[i][j] += A1[i][k] * temp[k][j]; // multiplying A1 by AN (starting with A1 * A1)
                    }
                }
            }
            list.add(AN);
        }

        // adding all matrices in list two find reachability matrix
        int[][] reachability = new int[n][n];
        for(int[][] matrix : list){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    reachability[i][j] += matrix[i][j];
                }
            }
        }
        list.add(reachability);
        printMatrix(reachability);
    }

    /* total paths of length 1 edge */
    public static void paths1Edge(ArrayList<int[][]> list){
        int[][] A1 = list.getFirst();
        int n = A1.length;
        int output = 0;

        for (int[] ints : A1) {
            for (int col = 0; col < n; col++) {
                output += ints[col];
            }
        }
        System.out.println("Total number of paths of length 1 edge: "+output);
    }

    /* total paths of length N edges */
    public static void pathsNEdges(ArrayList<int[][]> list){
        int[][] AN = list.get(list.size()-2);
        int n = AN.length;
        int output = 0;

        for (int[] ints : AN) {
            for (int col = 0; col < n; col++) {
                output += ints[col];
            }
        }
        System.out.println("Total number of paths of length "+n+" edges: "+output);
    }

    /* total paths of length 1 to N edges */
    public static void paths1ToNEdges(ArrayList<int[][]> list){
        int[][] reachabilityMatrix = list.getLast();
        int n = reachabilityMatrix.length;
        int output = 0;

        for (int[] ints : reachabilityMatrix) {
            for (int col = 0; col < n; col++) {
                output += ints[col];
            }
        }
        System.out.println("Total number of paths of length 1 to "+n+" edges: "+output);
    }

    /* sums each row of the first matrix, prints as out-degree */
    public static void outDegree(ArrayList<int[][]> list){
        int[][] A1 = list.getFirst();
        int n = A1.length;
        int output;

        System.out.println("Out-degrees:");
        for(int i = 0; i < n; i ++){
            output = 0;
            for(int j = 0; j < n; j++){
                output += A1[i][j];
            }
            System.out.println("Node "+(i+1)+" out-degree is "+output);
        }
        System.out.println();
    }

    /* sums each column of the first matrix, prints as in-degree */
    public static void inDegree(ArrayList<int[][]> list){
        int[][] A1 = list.getFirst();
        int n = A1.length;
        int output;

        System.out.println("In-Degrees: ");
        for(int i = 0; i < n; i ++){
            output = 0;
            for (int[] ints : A1) {
                output += ints[i];
            }
            System.out.println("Node "+(i+1)+" in-degree is "+output);
        }
        System.out.println();
    }

    /* sums major diagonal of the first matrix, prints as number of self-loops */
    public static void selfLoops(ArrayList<int[][]> list){
        int[][] A1 = list.getFirst();
        int n = A1.length;
        int output = 0;

        for(int i = 0; i < n; i ++) {
            output += A1[i][i];
        }
        System.out.println("Total number of self-loops: "+output);
    }

    /* sums major diagonal of last matrix (not reachability), prints as number of cycles of length N */
    public static void cyclesLengthN(ArrayList<int[][]> list){
        int[][] AN = list.get(list.size()-2);
        int n = AN.length;
        int output = 0;

        for(int i = 0; i < n; i ++) {
            output += AN[i][i];
        }
        System.out.println("Total number of cycles of length 1 to "+n+" edges: "+output);
    }

    /* sums major diagonal of reachability matrix, prints as number of cycles of length 1 to N */
    public static void cyclesLength1ToN(ArrayList<int[][]> list){
        int[][] reachabilityMatrix = list.getLast();
        int n = reachabilityMatrix.length;
        int output = 0;

        for(int i = 0; i < n; i ++) {
            output += reachabilityMatrix[i][i];
        }
        System.out.println("Total number of cycles of length 1 to "+n+" edges: "+output);
    }
}
