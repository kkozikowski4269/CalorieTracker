package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day implements Comparable<Day>{
    private String name;
    private List<Meal> meals;
    private String date;

    public Day(){}

    public Day(LocalDate date){
        this.name = date.getDayOfWeek().toString();
        this.meals = new ArrayList<>();
        this.date = date.toString();
    }

    public void addMeal(Meal meal){
        this.meals.add(meal);
    }

    public void setMeals(List<Meal> meals){
        this.meals = meals;
    }

    public List<Meal> getMeals(){
        return this.meals;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }

    @JsonIgnore
    public int getCalories(){
        int calories = 0;
        for(Meal meal : this.meals){
            calories+=meal.getCalories();
        }
        return calories;
    }

    @Override
    public String toString() {
        return "Day{" +
                "date='" + date + '\'' +
                "name='" + name + '\'' +
                ", meals=" + meals +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Day)) return false;
        Day day = (Day) o;
        return getDate().equals(day.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate());
    }

    @Override
    public int compareTo(Day o) {
        return this.getDate().compareTo(o.getDate());
    }
}
