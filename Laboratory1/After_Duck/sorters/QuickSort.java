package sorters;
import entities.Duck;

public class QuickSort extends AbstractSorter {

    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) return;
        qs(array, 0, array.length - 1);
    }

    private void qs(int[] v, int st, int dr) {
        if (st >= dr) return;
        int pivot = v[(st + dr) >>> 1];
        int i = st, j = dr;
        while (i <= j) {
            while (v[i] < pivot) i++;
            while (v[j] > pivot) j--;
            if (i <= j) {
                int tmp = v[i]; v[i] = v[j]; v[j] = tmp;
                i++; j--;
            }
        }
        if (st < j) qs(v, st, j);
        if (i < dr) qs(v, i, dr);
    }

    @Override
    public void sort(Duck[] ducks, int N) {
        if (ducks == null) return;
        if (N <= 1) return;
        if (N > ducks.length) N = ducks.length;
        qs(ducks, 0, N - 1);
    }

    private void qs(Duck[] v, int st, int dr) {
        if (st >= dr) return;
        Duck pivot = v[(st + dr) >>> 1];
        int i = st, j = dr;
        while (i <= j) {
            while (compareForOrder(v[i], pivot) < 0) i++;
            while (compareForOrder(v[j], pivot) > 0) j--;
            if (i <= j) {
                Duck tmp = v[i]; v[i] = v[j]; v[j] = tmp;
                i++; j--;
            }
        }
        if (st < j) qs(v, st, j);
        if (i < dr) qs(v, i, dr);
    }

    private int compareForOrder(Duck a, Duck b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;

        return Integer.compare(a.resistance(), b.resistance());
    }
}
