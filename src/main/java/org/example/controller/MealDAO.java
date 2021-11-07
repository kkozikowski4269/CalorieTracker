package org.example.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.example.model.FoodItem;
import org.example.model.Meal;


/**
 *
 * @author kfkoz
 */
public class MealDAO //implements DAO<Meal>
{

    private ArrayList<Meal> meals;
    private String fName;
    public MealDAO(String fName)
    {
        this.fName = fName;
        this.meals = getAll();
    }

    public ArrayList<Meal> getAll()
    {

        ArrayList<Meal> meals = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(fName)))
        {
            String line = reader.readLine();
            while(line != null)
            {
                String[] values = line.split(";");

                Meal meal = new Meal();
                meal.setName(values[0]);
                for(int i = 1; i < values.length; i++)
                {
                    String[] itemInfo = values[i].split(",");
                    String name = itemInfo[0];
                    int calories = Integer.parseInt(itemInfo[1]);
                    meal.addItem(new FoodItem(name, calories));
                }

                meals.add(meal);
                line = reader.readLine();
            }
        } catch (IOException ex) {
            //saveAll(fName);
        }

        return meals;
    }

    public Meal get(int pos)
    {
        return this.meals.get(pos);
    }

    public boolean add(Meal meal)
    {
        this.meals.add(meal);
        return true;
    }

    public boolean delete(Meal meal)
    {
        for(int i = 0; i < this.meals.size(); i++)
        {
            if(this.meals.get(i).getName().equals(meal.getName()) &&
                    this.meals.get(i).getCalories() == meal.getCalories())
            {
                boolean match = true;
                Meal storedMeal = this.meals.get(i);
                for(int j = 0; j < meal.getAll().size(); j++)
                {
                    if(!meal.getItem(j).getName().equals(storedMeal.getItem(j).getName()) &&
                            meal.getItem(j).getCalories() != storedMeal.getItem(j).getCalories())
                    {
                        match = false;
                    }
                }
                if(match)
                {
                    this.meals.remove(this.meals.get(i));
                    return saveAll();
                }
            }
        }
        return saveAll();
    }

    public boolean update(Meal oldMeal, Meal update)
    {
        oldMeal.setName(update.getName());
        oldMeal.setAll(update.getAll());
        return saveAll();
    }

    public boolean save(Meal meal)
    {
        Path filePath = Paths.get(this.fName);
        if(Files.notExists(filePath))
        {
            try
            {
                Files.createFile(filePath);
            }
            catch(IOException e)
            {
                System.err.println(e.toString());
            }
        }

        File theFile = filePath.toFile();

        try(PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(theFile,true)))
        )
        {
            out.print(meal.getName() + ";");
            ArrayList<FoodItem> items = meal.getAll();
            for(FoodItem item : items)
            {
                out.print(item.getName() + ",");
                out.print(item.getCalories() + ";");
            }
            out.println();

            return true;
        }
        catch(IOException e)
        {
            System.err.println(e.toString());
        }
        return false;
    }

    public boolean saveAll()
    {
        Path filePath = Paths.get(fName);
        if(Files.notExists(filePath))
        {
            try
            {
                Files.createFile(filePath);
            }
            catch(IOException e)
            {
                System.err.println(e.toString());
            }
        }

        File theFile = filePath.toFile();

        try(PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(theFile)))
        )
        {
            for(int i = 0; i < meals.size(); i++)
            {
                out.print(meals.get(i).getName() + ";");
                ArrayList<FoodItem> items = meals.get(i).getAll();
                for(FoodItem item : items)
                {
                    out.print(item.getName() + ",");
                    out.print(item.getCalories() + ";");
                }
                out.println();
            }
            return true;
        }
        catch(IOException e)
        {
            System.err.println(e.toString());
        }
        return false;
    }

    public static boolean setCalorieLimit(int limit)
    {
        Path filePath = Paths.get("CalorieLimit");
        if(Files.notExists(filePath))
        {
            try
            {
                Files.createFile(filePath);
            }
            catch(IOException e)
            {
                System.err.println(e.toString());
            }
        }

        File theFile = filePath.toFile();

        try(PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(theFile)))
        )
        {
            out.print(limit);

            return true;
        }
        catch(IOException e)
        {
            System.err.println(e.toString());
        }
        return false;
    }

    public static int getCalorieLimit()
    {
        try(BufferedReader reader = new BufferedReader(new FileReader("CalorieLimit")))
        {
            String lineStr = reader.readLine();
            return Integer.parseInt(lineStr);
        } catch (IOException ex) {
            System.out.println(ex.toString());
            return -1;
        }
    }
}
