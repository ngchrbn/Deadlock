/**
 * A class that represents a process and its attributes:
 * nResources, name, allocated, maximum and need
 * @author Guy Cherubin Ntajugumba
 * @version 1.0
 */
public class Process {
    int nResources;
    String name;
    int[] allocated = new int[3];
    int[] maximum = new int[3];
    int[] need;

    /**
     * Constructs a new process
     * @param nResources number of resources
     * @param name process's name
     * @param allocated Amount currently allocated to the process
     * @param maximum Maximum demand for the process
     */
    Process(int nResources, String name, int[] allocated, int[] maximum) {
        this.nResources = nResources;
        this.name = name;
        for (int i=0; i<nResources; ++i) {
            this.allocated[i] = allocated[i];
            this.maximum[i] = maximum[i];
        }
        need = getNeed();
    }

    /**
     * Get process's name
     * @return process's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get Amount currently allocated to the process
     * @return Array allocated
     */
    public int[] getAllocated() {
        return allocated;
    }

    /**
     * Get Maximum demand for the process
     * @return Maximum demand for the process
     */
    public int[] getMaximum() {
        return maximum;
    }

    /**
     * Update the amount allocated to the process
     * @param allocated Array of new amount allocated to the process
     */
    public void updateAllocation(int []allocated) {
        System.arraycopy(allocated, 0, this.allocated, 0, nResources);
        need = getNeed();
    }

    /**
     * Get Remaining needs for the process
     * @return Array
     */
    public int[] getNeed() {
        need = new int[nResources];
        for (int i=0; i<nResources; ++i) {
            need[i] = maximum[i] - allocated[i];
        }
        return need;
    }

    /**
     * Return a representation of a process containing:
     * allocated values, maximum and need values
     * @return String representation of a process
     */
    @Override
    public String toString() {
        StringBuilder allocated = new StringBuilder();
        StringBuilder maximum = new StringBuilder();
        StringBuilder need = new StringBuilder();
        for (int i=0; i<nResources; ++i) {
            allocated.append(this.allocated[i]).append("  ");
            maximum.append(this.maximum[i]).append(" ");
            need.append(this.need[i]).append(" ");
        }
        return String.format("%-10s", name) +
                String.format("%15s", allocated) +
                String.format("%15s", maximum) +
                String.format("%15s", need);
    }
}
