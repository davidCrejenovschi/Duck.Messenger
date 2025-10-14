package tasks;
import sorters.AbstractSorter;
import java.util.Arrays;

public class SortingTask extends Task {

    private final int[] numbers;
    private final AbstractSorter sorter;

    public SortingTask(String taskId_init, String description_init, int[] numbers_init, AbstractSorter sorter_init) {
        super(taskId_init, description_init);
        numbers = numbers_init;
        sorter = sorter_init;
    }

    @Override
    public void execute() {
        sorter.sort(numbers);
        for (int number : numbers) {
            System.out.print(number + " ");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return "Sorted numbers: " + Arrays.toString(numbers);
    }
}