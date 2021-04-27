import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class that represents the Banker's Algorithm with its attributes:
 * number of process, number of resources, available amount of each resource,
 * maximum demand of each process, amount currently allocated to each process,
 * remaining needs of each process, and the safe sequence
 * @author Guy Cherubin Ntajugumba
 * @version 1.0
 */
public class Banker {
    int nprocess;
    int nresource;
    int []available;
    int [][]maximum;
    int [][]alloc;
    int [][]need;
    int []safeSequence;
    Process process;
    ArrayList<Process> processes = new ArrayList<>();

    /**
     * Constructs a new Banker from a user input:
     * First the different arrays are constructed and nprocess processes are constructed
     * as well as the need for each process calculated
     * @param nprocess Number of processes
     * @param nresource Number of resources
     * @param alloc Amount currently allocated to each process
     * @param maximum Maximum demand of each process
     * @param available Available amount of each resource
     */
    Banker(int nprocess, int nresource, int [][]alloc, int [][]maximum, int []available) {
        this.nprocess = nprocess;
        this.nresource = nresource;
        this.available = new int[this.nresource];
        this.alloc = new int[this.nprocess][this.nresource];
        this.maximum = new int[this.nprocess][this.nresource];
        need = new int[this.nprocess][nresource];
        safeSequence = new int[nprocess];
        System.arraycopy(available, 0, this.available, 0, nresource);
        for (int i=0; i<this.nprocess; ++i) {
            process = new Process(nresource, String.format("P%s", i), alloc[i], maximum[i]);
            processes.add(process);
        }
        System.arraycopy(getAlloc(), 0, this.alloc, 0, nprocess);
        System.arraycopy(getMaximum(), 0, this.maximum, 0, nprocess);
        setNeed();
    }

    /**
     * Get Amount currently allocated to each process
     * @return 2-d Array
     */
    public int[][] getAlloc() {
        for (int i=0; i<nprocess; ++i) {
            for (int j=0; j<nresource; ++j) {
                alloc[i][j] = processes.get(i).getAllocated()[j];
            }
        }
        return alloc;
    }

    /**
     * Get Maximum demand of each process
     * @return 2-d Array
     */
    public int[][] getMaximum() {
        for (int i=0; i<nprocess; ++i) {
            for (int j=0; j<nresource; ++j) {
                maximum[i][j] = processes.get(i).getMaximum()[j];
            }
        }
        return maximum;
    }

    /**
     * Calculate the remaining needs of each process
     */
    public void setNeed() {
        for (int i=0; i<nprocess; ++i) {
            for (int j=0; j<nresource; ++j) {
                need[i][j] = maximum[i][j] - alloc[i][j];
            }
        }
    }

    /**
     * Output the values of each process
     */
    public void snapshot() {
        System.out.println("\nSnapShot:");
        System.out.printf("==>Available: %s%n", Arrays.toString(available));
        System.out.printf("%-10s%15s%15s%15s%n", "Process", "Allocation", "Maximum", "Need");
        for (int i=0; i<nprocess; ++i) {
            System.out.println(processes.get(i).toString());
        }
        System.out.println();
    }

    /**
     * Checks if the System is safe or unsafe
     * Initialized with work: equivalent to available array,
     *  finished array of nProcess elements initialized with false.
     * It iterated through every process to find which one is unfinished and
     * its need <= work. If found, work is incremented with the values of its allocation
     * of that process. It continues until the finished array contains only true values.
     * And finally, it gives a safe sequence to follow.
     * @return boolean
     */
    public boolean isSafe() {
        boolean safe; // Returned value
        int counter = 0; // Count all finished processes
        boolean []finished = new boolean[nprocess]; // Holds state of each process(finished or unfinished)
        for (int i=0; i<nprocess; ++i) {
            finished[i] = false; // Indicated the initial state of each process
        }

        int []work = new int[nresource]; // Equivalent to the available array
        System.arraycopy(available, 0, work, 0, nresource);

        while (counter < nprocess) {
            boolean flag = false;
            for (int i=0; i<nprocess; ++i) {
                if (!finished[i]) {
                    int j;
                    for (j=0; j<nresource; ++j) {
                        if (need[i][j] > work[j]) {
                            System.out.println(processes.get(i).getName() + " must wait");
                            break;
                        }
                    }
                    if (j == nresource) {
                        safeSequence[counter++] = i;
                        finished[i] = true;
                        flag = true;
                        System.out.println(processes.get(i).getName() + " finished and put on safe sequence");
                        for (j=0; j<nresource; ++j) {
                            work[j] = work[j] + alloc[i][j];
                        }
                    }
                }
            }
            if (!flag) {
                break;
            }
        }
        if (counter <nprocess) {
            System.out.println("The System is unsafe!");
        }
        else {
            System.out.print("\nSafe Sequence: ");
            for (int i=0; i<nprocess; ++i) {
                System.out.print("P" + safeSequence[i]);
                if (i != nprocess-1) {
                    System.out.print(" --> ");
                }
            }
        }
        safe = true;
        for (int i=0; i<nprocess; ++i) {
            safe &= finished[i];
        }
        return safe;
    }

    /**
     * Checks if the provided process name is registered
     * @param processName process's name
     * @return boolean
     */
    public boolean isNameFound(String processName) {
        for (int i=0; i<nprocess; ++i) {
            if (processes.get(i).getName().equals(processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a process's index from a process name
     * @param processName process's name
     * @return int index value
     */
    public int getProcessIndex(String processName) {
        int index;
        for (index=0; index<nprocess; ++index) {
            if (processes.get(index).getName().equals(processName)) {
                break;
            }
        }
        return index;
    }

    /**
     * First check if the name exists, if found, get its index.
     * After it checks if its request <= its need, if yes goto next step, otherwise
     * indicates that the process has exceeded its maximum claim
     * If the above condition is true, checks if its request <= Available;
     * If true, goto to next step, otherwise indicates that the process must wait
     * as the resources are not available.
     * @param processName process's name
     * @param request An array containing the request values
     */
    public void request(String processName, int []request) {
        boolean exceededMax = false;
        if (isNameFound(processName)) {
            int processIndex = getProcessIndex(processName);
            System.out.println("Request: " + Arrays.toString(request));
            System.out.println("Need: " + Arrays.toString(need[processIndex]));
            for (int i=0; i<nresource; ++i) {
                if (request[i] > need[processIndex][i]) {
                    exceededMax = true;
                    break;
                }
            }
            if (exceededMax) {
                System.out.println("Can't exceed the maximum need!");
            }
            else {
                boolean wait = false;
                for (int i=0; i<nresource; ++i) {
                    if (request[i] > available[i]) {
                        wait = true;
                        break;
                    }
                }
                if (wait) {
                    System.out.println(processName + " must wait since the resources are not available");
                }
                else {
                    int []tmpAvailable = available;
                    int []tmpAlloc = alloc[processIndex];
                    int []tmpNeed = need[processIndex];
                    for (int i=0; i<nresource; ++i) {
                        available[i] -= request[i];
                        alloc[processIndex][i] += request[i];
                        need[processIndex][i] -= request[i];
                    }
                    if (isSafe()) {
                        System.out.println("\nThe System is safe");
                        System.out.println("\n*******Resources Request granted!*******\n");
                        processes.get(processIndex).updateAllocation(alloc[processIndex]);
                    }
                    else {
                        System.out.println("\n*******Resources Request Rejected!*******");
                        available = tmpAvailable;
                        alloc[processIndex] = tmpAlloc;
                        need[processIndex] = tmpNeed;
                    }
                }
            }
        }
        else {
            System.out.println("The Process name is not found!");
        }
    }
}

