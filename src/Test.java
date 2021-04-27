public class Test {
    public static void main(String[] args) {
        int nprocess = 5;
        int nresource = 3;
        int[][] alloc = new int[][] { {0, 1, 0},
                {2, 0, 0},
                {3, 0, 2},
                {2, 1, 1},
                {0, 0, 2}} ;

        int [][]max = new int[][] { {7, 5, 3},
                {3, 2, 2}, // Replacing with 7 creates unsafe system
                {9, 0, 2},
                {2, 2, 2},
                {4, 3, 3}};

        int []available = new int[] {3, 3, 2};

        Banker banker = new Banker(nprocess, nresource, alloc, max, available);
        banker.snapshot();
        banker.isSafe();
        System.out.println();
        banker.snapshot();
        int []request = new int[]{1, 0, 2};
        //int []request = new int[]{3, 2, 2}; // Creates exceeded maximum need

        banker.request("P1", request);
        banker.snapshot();
    }
}
