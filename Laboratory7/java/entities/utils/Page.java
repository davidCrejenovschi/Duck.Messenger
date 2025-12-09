package entities.utils;

import java.util.Collection;

public record Page<E>(Collection<E> elementsOnPage, int totalNumberOfElements) { }
