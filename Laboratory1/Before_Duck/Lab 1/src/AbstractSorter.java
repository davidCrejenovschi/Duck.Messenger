
abstract class AbstractSorter{

    abstract void sort(int[] array);
}

class BubbleSort extends AbstractSorter{

    @Override
    public void sort(int[] array) {

        for(int i = 0; i < array.length; i++) {

            for (int j = i + 1; j < array.length; j++) {

                if (array[i] > array[j]) {

                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }
}

class QuickSort extends AbstractSorter{

    @Override
    public void sort(int[] array) {

        qs(array, 0, array.length - 1);

    }

    private void qs(int[] v, int st, int dr){

        if(st < dr){

            int mid = (st + dr)/2;
            int aux = v[st];
            v[st] = v[mid];
            v[mid] = aux;
            int  i = st, j = dr, k = 0;
            while(i < j){

                if(v[i] > v[j]){

                    aux = v[i];
                    v[i] = v[j];
                    v[j] = aux;
                    k = 1 - k;
                }
                i+=k;
                j-=1-k;
            }
            qs(v, st, i-1);
            qs(v, i+1, dr);
        }
    }
}