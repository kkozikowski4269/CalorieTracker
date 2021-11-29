package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *
 * @author kfkoz
 */
public class DayDAO implements DAO<Day, LocalDate>{
    private Map<String,Day> dayMap;
    private String fName;
    private File file;
    private static final DayDAO dao = new DayDAO();

    private DayDAO(){
        this.fName = null;
        this.file = null;
    }

    public void setFile(File file){
        this.file = file;
        this.dayMap = this.listToMap(getAll());
    }

    public File getFile(){
        return this.file;
    }

    public void setFName(String fName){
        this.fName = fName;
    }

    public String getFName(){
        return this.fName;
    }

    public static DayDAO getInstance(){
        return dao;
    }


    public ArrayList<Day> getAll() {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        if(this.file.length() > 0) {
            try {
                return mapper.readValue(this.file, new TypeReference<>() {
                });
            }catch (IOException e){
                System.err.println("Error fetching data: " + e);
            }
        }

        return null;
    }

    @Override
    public Day get(LocalDate date) {
        if(this.dayMap != null && !this.dayMap.isEmpty()){
            return this.dayMap.get(date.toString());
        }else{
            return null;
        }
    }

    @Override
    public boolean delete(Day day) {
        return this.dayMap.remove(day.getDate(),day);
    }

    @Override
    public boolean update(Day day) {
        if(this.dayMap.containsKey(day.getDate())) {
            this.dayMap.replace(day.getDate(), day);
            return true;
        }
        return false;
    }

    @Override
    public boolean save(Day day) {
        if(!this.dayMap.containsKey(day.getDate())) {
            this.dayMap.put(day.getDate(), day);
            return true;
        }
        return false;
    }

    public void saveAll(){
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            FileWriter fileWriter = new FileWriter(this.fName, false);
            SequenceWriter seqWriter = mapper.writer().writeValuesAsArray(fileWriter);
            for (Day d : this.dayMap.values()) {
                seqWriter.write(d);
            }
            seqWriter.close();
        }catch(IOException e){
            System.err.println("Failed to save data: " + e);
        }
    }

    private Map<String,Day> listToMap(List<Day> dayList){
        Map<String,Day> map = new HashMap<>();
        if(dayList != null) {
            for (Day d : dayList) {
                map.put(d.getDate(), d);
            }
        }
        return map;
    }
}