package org.example;

import java.util.ArrayList;

/**
 * @param <T> The Datatype that is being stored and manipulated
 * @param <E> The Datatype used to search for and get the specific element
 * @author kfkoz
 */
public interface DAO<T, E>
{
    ArrayList<T> getAll();
    T get(E e);
    boolean delete(T t);
    boolean update(T t);
    boolean save(T t);
}