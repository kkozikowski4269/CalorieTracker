package org.example;

import java.util.ArrayList;

/**
 *
 * @author kfkoz
 */
public interface DAO<T>
{
    ArrayList<T> getAll();
    T get(int pos);
    boolean delete(T t);
    boolean update(T t, T u);
    boolean save(T t);
}