package factories;
import containers.Container;
import strategies.forcontainers.Strategy;

public interface Factory {

    Container createContainer(Strategy strategy);
}