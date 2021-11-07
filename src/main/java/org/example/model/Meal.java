package org.example.model;
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

    public Meal()
    {
        this.foodItems = new ArrayList<>();
    }

    public Meal(String name)
    {
        this.name = name;
        this.foodItems = new ArrayList<>();
    }

    public static final int getNumMeals()
    {
        return numMeals;
    }

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
        int cals = 0;

        for(FoodItem item : this.getAll())
        {
            cals += item.getCalories();
        }
        return cals;
    }

    public void addItem(FoodItem item)
    {
        this.foodItems.add(item);
    }

    public FoodItem removeItem(int pos)
    {
        FoodItem removed = this.foodItems.get(pos);
        this.foodItems.remove(pos);

        return removed;
    }

    public void removeAll()
    {
        this.foodItems.removeAll(foodItems);
    }

    public final FoodItem getItem(int pos)
    {
        return this.foodItems.get(pos);
    }

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

    public void setItem(int pos, FoodItem newItem)
    {
        FoodItem oldItem = this.foodItems.get(pos);

        oldItem.setName(newItem.getName());
        oldItem.setCalories(newItem.getCalories());
    }

    public void setAll(ArrayList<FoodItem> foodItems)
    {
        this.foodItems = foodItems;
    }

    public final ArrayList<FoodItem> getAll()
    {
        return this.foodItems;
    }

    @Override
    public String toString()
    {
        return this.getName() + " (" + this.getCalories() + " Calories)";
    }
}
