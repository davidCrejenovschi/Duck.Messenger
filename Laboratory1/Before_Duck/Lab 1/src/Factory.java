
public interface Factory {

    Container createContainer(Strategy strategy);
}

class TaskContainerFactory implements Factory {

    private static TaskContainerFactory instance;

    private TaskContainerFactory() {}

    public static TaskContainerFactory getInstance() {
        if (instance == null) {
            instance = new TaskContainerFactory();
        }
        return instance;
    }

    @Override
    public Container createContainer(Strategy strategy) {

        switch (strategy) {

            case FIFO -> { return new QueueContainer(); }
            case LIFO -> { return new StackContainer(); }
            default-> throw new IllegalArgumentException("Invalid Strategy");
        }

    }
}