package services;
import exceptions.validators.FriendshipValidationException;
import entities.pairs.FriendshipPair;
import repository.FriendshipRepository;
import validators.FriendshipValidator;

import java.util.*;

public class FriendshipService {

    FriendshipRepository repository;
    FriendshipValidator validator;

    public FriendshipService(FriendshipRepository repository_init, FriendshipValidator validator_init) {
        repository = repository_init;
        validator = validator_init;
    }

    public void addFriendship(Long leftId, Long rightId) throws FriendshipValidationException {
        FriendshipPair pair = new FriendshipPair(leftId, rightId);
        validator.validateFriendship(pair);
        repository.add(pair);
        repository.writeToFile();
    }

    public void deleteFriendship(long friendshipId) throws FriendshipValidationException {
        validator.validateId(friendshipId);
        repository.delete(friendshipId);
        repository.writeToFile();
    }

    public FriendshipPair getFriendship(long id) throws FriendshipValidationException {
        validator.validateId(id);
        return repository.getById(id);
    }

    public Collection<FriendshipPair> getAllFriendships() {
        return repository.getAll();
    }

    public int viewCommunities(Collection<FriendshipPair> friendships) {
        Map<Long, Set<Long>> graph = getFriendshipGraph(friendships);
        return countConnectedComponents(graph);
    }
    private Map<Long, Set<Long>> getFriendshipGraph(Collection<FriendshipPair> friendships) {

        Map<Long, Set<Long>> graph = new HashMap<>();

        for (FriendshipPair pair : friendships) {
            long left = pair.getLeft();
            long right = pair.getRight();

            if (!graph.containsKey(left)) {
                graph.put(left, new HashSet<>());
            }
            graph.get(left).add(right);

            if (!graph.containsKey(right)) {
                graph.put(right, new HashSet<>());
            }
            graph.get(right).add(left);
        }

        return graph;
    }
    private List<Set<Long>> extractConnectedComponents(Map<Long, Set<Long>> graph) {

        Set<Long> visited = new HashSet<>();
        List<Set<Long>> components = new ArrayList<>();

        for (Long node : graph.keySet()) {
            if (!visited.contains(node)) {
                Set<Long> component = new HashSet<>();
                dfs(node, graph, visited, component);
                components.add(component);
            }
        }

        return components;
    }
    private void dfs(Long node, Map<Long, Set<Long>> graph, Set<Long> visited, Set<Long> component) {
        visited.add(node);
        component.add(node);
        for (Long neighbor : graph.getOrDefault(node, Collections.emptySet())) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited, component);
            }
        }
    }
    private int countConnectedComponents(Map<Long, Set<Long>> graph) {
        return extractConnectedComponents(graph).size();
    }

    public Set<Long> viewMostSociableCommunity(Collection<FriendshipPair> friendships) {

        Map<Long, Set<Long>> graph = getFriendshipGraph(friendships);
        List<Set<Long>> components = extractConnectedComponents(graph);

        Set<Long> mostSociable = null;
        int maxDiameter = -1;

        for (Set<Long> component : components) {
            int diameter = calculateComponentDiameter(component, graph);
            if (diameter > maxDiameter) {
                maxDiameter = diameter;
                mostSociable = component;
            }
        }

        return mostSociable;

    }
    private int calculateComponentDiameter(Set<Long> component, Map<Long, Set<Long>> graph) {
        int diameter = 0;
        for (Long node : component) {
            Map<Long, Integer> distances = bfsDistances(node, component, graph);
            int farthest = 0;
            for (int d : distances.values()) {
                if (d > farthest) {
                    farthest = d;
                }
            }
            if (farthest > diameter) {
                diameter = farthest;
            }
        }
        return diameter;
    }
    private Map<Long, Integer> bfsDistances(Long start, Set<Long> component, Map<Long, Set<Long>> graph) {
        Map<Long, Integer> distance = new HashMap<>();
        Queue<Long> queue = new LinkedList<>();
        queue.add(start);
        distance.put(start, 0);

        while (!queue.isEmpty()) {
            Long current = queue.poll();
            int currDist = distance.get(current);

            for (Long neighbor : graph.getOrDefault(current, Collections.emptySet())) {
                if (component.contains(neighbor) && !distance.containsKey(neighbor)) {
                    distance.put(neighbor, currDist + 1);
                    queue.add(neighbor);
                }
            }
        }

        return distance;
    }
}
