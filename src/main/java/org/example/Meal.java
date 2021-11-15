package org.example;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

/**
 *
 * @author kfkoz
 */
public class Meal
{
    private static int numMeals = 0;
    private String name;
    private ArrayList<FoodItem> foodItems;
    private int calories;

    public Meal(){}

    public Meal(String name)
    {
        this.name = name;
        this.foodItems = new ArrayList<>();
        this.calories = 0;
    }
    @JsonIgnore
    public static final int getNumMeals()
    {
        return numMeals;
    }
    @JsonIgnore
    public static void setNumMeals(int num)
    {
        numMeals = num;
    }

    public final String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCalories()
    {
        return this.calories;
    }

    public void setCalories(int calories){
        this.calories = calories;
    }

    public void addItem(FoodItem item)
    {
        this.foodItems.add(item);
        this.calories+=item.getCalories();
    }

    public FoodItem removeItem(int pos)
    {
        FoodItem removed = this.foodItems.get(pos);
        this.foodItems.remove(pos);
        this.calories-=removed.getCalories();
        return removed;
    }

    public void removeAll()
    {
        this.foodItems.removeAll(foodItems);
    }
    @JsonIgnore
    public final FoodItem getItem(int pos)
    {
        return this.foodItems.get(pos);
    }

    @JsonIgnore
    public final FoodItem getItem(String name)
    {
        for(FoodItem item : this.foodItems)
        {
            if(item.getName().equals(name))
            {
                return item;
            }
        }
        return new FoodItem("ERROR", -1);
    }
    @JsonIgnore
    public void setItem(int pos, FoodItem newItem)
    {
        FoodItem oldItem = this.foodItems.get(pos);

        oldItem.setName(newItem.getName());
        oldItem.setCalories(newItem.getCalories());
    }

    public void setFoodItems(ArrayList<FoodItem> foodItems)
    {
        this.foodItems = foodItems;
    }

    public final ArrayList<FoodItem> getFoodItems()
    {
        return this.foodItems;
    }

    @Override
    public String toString()
    {
        return this.getName() + " (" + this.getCalories() + " Calories)";
    }
}
