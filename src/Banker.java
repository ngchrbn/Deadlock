import java.util.ArrayList;
import java.util.Arrays;

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

    public int[][] getAlloc() {
        for (int i=0; i<nprocess; ++i) {
            for (int j=0; j<nresource; ++j) {
                alloc[i][j] = processes.get(i).getAllocated()[j];
            }
        }
        return alloc;
    }

    public int[][] getMaximum() {
        for (int i=0; i<nprocess; ++i) {
            for (int j=0; j<nresource; ++j) {
                maximum[i][j] = processes.get(i).getMaximum()[j];
            }
        }
        return maximum;
    }

    public void setNeed() {
        for (int i=0; i<nprocess; ++i) {
            for (int j=0; j<nresource; ++j) {
                need[i][j] = maximum[i][j] - alloc[i][j];
            }
        }
    }

    public void snapshot() {
        System.out.println("\nSnapShot:");
        System.out.printf("==>Available: %s%n", Arrays.toString(available));
        System.out.printf("%-10s%15s%15s%15s%n", "Process", "Allocation", "Maximum", "Need");
        for (int i=0; i<nprocess; ++i) {
            System.out.println(processes.get(i));
        }
        System.out.println();
    }

    public boolean isSafe() {
        boolean safe = false;
        int counter = 0;
        boolean []finished = new boolean[nprocess];
        for (int i=0; i<nprocess; ++i) {
            finished[i] = false;
        }

        int []work = new int[nresource];
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

    public boolean isNameFound(String processName) {
        for (int i=0; i<nprocess; ++i) {
            if (processes.get(i).getName().equals(processName)) {
                return true;
            }
        }
        return false;
    }

    public int getProcessIndex(String processName) {
        int index;
        for (index=0; index<nprocess; ++index) {
            if (processes.get(index).getName().equals(processName)) {
                break;
            }
        }
        return index;
    }

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
                        System.out.println("\nThe System is unsafe!");
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

