package org.example.model;

/**
 *
 * @author kfkoz
 */
public class FoodItem
{
    private String name;
    private int calories;

    public FoodItem(String name, int calories)
    {
        this.setName(name);
        this.setCalories(calories);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setCalories(int cals)
    {
        this.calories = cals;
    }

    public int getCalories()
    {
        return this.calories;
    }

    @Override
    public String toString()
    {
        return this.getName() + " - " + this.getCalories();
    }
}