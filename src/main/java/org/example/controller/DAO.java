package org.example.controller;

import java.util.ArrayList;

/**
 *
 * @author kfkoz
 */
public interface DAO<T>
{
    ArrayList<T> getAll();
    T get(int pos);
    boolean add(T t);
    boolean delete(T t);
    boolean update(T t);
    boolean save(T t);
    boolean saveAll();
}