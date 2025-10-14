package sorters;
import entities.Duck;

public class BubbleSort extends AbstractSorter {

    @Override
    public void sort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] > array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    @Override
    public void sort(Duck[] ducks, int N) {
        if (ducks == null) return;
        if (N <= 1) return;
        if (N > ducks.length) N = ducks.length;

        for (int end = N - 1; end > 0; end--) {
            boolean swapped = false;
            for (int i = 0; i < end; i++) {
                Duck a = ducks[i];
                Duck b = ducks[i + 1];

                int cmp = Integer.compare(b.resistance(), a.resistance());

                if (cmp > 0) {
                    ducks[i] = b;
                    ducks[i + 1] = a;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

}
