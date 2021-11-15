package org.example;

/**
 *
 * @author kfkoz
 */
public class FoodItem implements Comparable<FoodItem>
{
    private String name;
    private int calories;

    public FoodItem(){}

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

    @Override
    public boolean equals(Object obj) {
        if(obj == null || this.getClass() != obj.getClass()){
            return false;
        }
        FoodItem other = (FoodItem) obj;
        if(this.name.equals(other.name) && this.calories == other.calories){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(FoodItem other){
        if(this.name.compareTo(other.name) == 0){
            return Integer.compare(this.calories, other.calories);
        }else{
            return this.name.compareToIgnoreCase(other.name);
        }
    }
}