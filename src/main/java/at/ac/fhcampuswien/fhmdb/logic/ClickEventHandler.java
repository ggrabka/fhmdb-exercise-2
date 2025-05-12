package at.ac.fhcampuswien.fhmdb.logic;

@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T t);
}
