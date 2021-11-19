package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *
 * @author kfkoz
 */
public class DayDAO implements DAO<Day>
{

    private ArrayList<Day> days;
    private String fName;
    private File file;
    private static final DayDAO dao = new DayDAO();

    private DayDAO(){
        this.fName = null;
        this.file = null;
    }

    public void setFile(File file){
        this.file = file;
        this.days = getAll();
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

    @Override
    public ArrayList<Day> getAll() {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        this.days = new ArrayList<>();
        if(this.file.length() > 0) {
            try {
                days = mapper.readValue(this.file, new TypeReference<>() {
                });
            }catch (IOException e){
                System.err.println("Error fetching data: " + e);
            }
        }
        return days;
    }

    @Override
    public Day get(int pos) {
        if(this.days != null && this.days.size() > 0){
            return this.days.get(pos);
        }else{
            return null;
        }
    }

    @Override
    public boolean delete(Day day) {
        if(this.days.contains(day)) {
            this.days.remove(day);
            this.saveAll();
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Day day) {
        if (this.days.contains(day)) {
            this.saveAll();
            return true;
        }
        return false;
    }
    @Override
    public boolean save(Day day) {
        this.days.add(day);
        this.saveAll();
        return true;
    }

    public void saveAll(){
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            FileWriter fileWriter = new FileWriter(this.fName, false);
            SequenceWriter seqWriter = mapper.writer().writeValuesAsArray(fileWriter);
            for (Day d : this.days) {
                seqWriter.write(d);
            }
            seqWriter.close();
        }catch(IOException e){
            System.err.println("Failed to save data: " + e);
        }
    }
}
