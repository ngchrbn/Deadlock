import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.printf("%60s%n%n", "******Banker's Algorithm******");
        System.out.print("Enter the number of Process: ");
        int nProcess = input.nextInt();
        System.out.print("Enter the number of resources: ");
        int nResource = input.nextInt();

        int [][]alloc = new int[nProcess][nResource]; // Amount currently allocated to each process
        int [][]max = new int[nProcess][nResource]; // Maximum demand of each process
        int []available = new int[nResource]; // Available amount of each resource

        // Get Available amount of each resource
        System.out.println();
        for (int i=0; i<nResource; ++i) {
            System.out.print("Enter available instances for resource " + Character.toString(65+i) + ": ");
            available[i] = input.nextInt();
        }


        // Get Amount currently allocated to each process
        for (int i=0; i<nProcess; ++i) {
            System.out.println();
            for (int j=0; j<nResource; ++j) {
                System.out.printf("Enter allocation value for Process %d in Resource %s: ",
                        (i+1), Character.toString(65+j));
                alloc[i][j] = input.nextInt();
            }
        }

        // Maximum demand of each process
        for (int i=0; i<nProcess; ++i) {
            System.out.println();
            for (int j=0; j<nResource; ++j) {
                System.out.printf("Enter maximum need for Process %d in Resource %s: ",
                        (i+1), Character.toString(65+j));
                max[i][j] = input.nextInt();
            }
        }

        Banker banker = new Banker(nProcess, nResource, alloc, max, available);
        banker.snapshot();
        banker.isSafe();
        menu(nResource, banker);


    }

    private static void menu(int nResource, Banker banker) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n1. Request. \n2. Release \n3. Quit");
        System.out.print("Your choice: ");
        int choice = input.nextInt();
        while (choice != 1 && choice != 2 && choice != 3) {
            System.out.println("Wrong Choice. Try Again");
            System.out.println("1. Request. \n2. Release \n3. Quit");
            System.out.print("Your choice: ");
            choice = input.nextInt();
        }

        if (choice == 1) {
            int []request = new int[nResource]; // Contains values for the request
            System.out.print("Enter the Process Name (ex. P0): ");
            String processName = input.next();
            for (int i=0; i<nResource; ++i) {
                System.out.printf("Enter value for Resource %s: ", Character.toString(65+i));
                request[i] = input.nextInt();
            }
            banker.request(processName, request);
            banker.snapshot();
            menu(nResource, banker);
        }
        else if (choice == 2) {
            int []release = new int[nResource]; // Contains values for the request
            System.out.print("Enter the Process Name (ex. P0): ");
            String processName = input.next();
            for (int i=0; i<nResource; ++i) {
                System.out.printf("Enter value for Resource %s: ", Character.toString(65+i));
                release[i] = input.nextInt();
            }
            banker.release(processName, release);
            banker.snapshot();
            menu(nResource, banker);
        }
        else {
            System.exit(0);
        }
    }
}
