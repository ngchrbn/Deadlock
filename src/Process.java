import java.util.Arrays;

public class Process {
    int nResources;
    String name;
    boolean state;
    int[] allocated = new int[3];
    int[] maximum = new int[3];
    int[] need;

    Process(int nResources, String name, int[] allocated, int[] maximum) {
        this.nResources = nResources;
        this.name = name;
        state = false;
        for (int i=0; i<nResources; ++i) {
            this.allocated[i] = allocated[i];
            this.maximum[i] = maximum[i];
        }
        need = getNeed();
    }

    public String getName() {
        return name;
    }

    public int[] getAllocated() {
        return allocated;
    }

    public int[] getMaximum() {
        return maximum;
    }

    public void updateAllocation(int []allocated) {
        System.arraycopy(allocated, 0, this.allocated, 0, nResources);
        need = getNeed();
    }
    public int[] getNeed() {
        need = new int[nResources];
        for (int i=0; i<nResources; ++i) {
            need[i] = maximum[i] - allocated[i];
        }
        return need;
    }
    @Override
    public String toString() {
        return String.format("%-10s", name) +
                String.format("%15s", allocated[0] + "  " + allocated[1] + "  " + allocated[2]) +
                String.format("%15s", maximum[0] + " " + maximum[1] + " " + maximum[2]) +
                String.format("%15s", need[0] + " " + need[1] + " " + need[2]);
    }
}
