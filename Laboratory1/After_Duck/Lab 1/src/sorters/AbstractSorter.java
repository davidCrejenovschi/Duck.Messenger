package sorters;
import entities.Duck;

public abstract class AbstractSorter {

    public abstract void sort(int [] array);

    public abstract  void sort(Duck[] ducks, int N);
}