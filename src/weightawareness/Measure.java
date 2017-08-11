/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weightawareness;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Miguel Frias Mosquea
 */
public class Measure {

    double weight;
    double bodyFat;
    LocalDate theDate;
    static final DateTimeFormatter aFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    Measure(double weight, double bodyFat, LocalDate date)
    {
        this.weight = weight;
        this.bodyFat = bodyFat;
        this.theDate = date;
    }
    
        Measure(double weight, double bodyFat, String date)
    {
        this.weight = weight;
        this.bodyFat = bodyFat;
        this.theDate = LocalDate.parse(date,Measure.aFormat);
    }
    
    @Override
    public String toString() {
        String humanDate = getFormattedDate();
        String description = humanDate + "\t" + weight + "Kgs\t" + bodyFat + "% bf";
        return description;
    }
    
    public String getFormattedDate() {
        return this.theDate.format(Measure.aFormat);
    }
    
    public static LocalDate convertStringToLocalDate(String theString)  {

        return LocalDate.parse(theString,Measure.aFormat);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Measure)) return false;
        Measure anotherMeasure = (Measure) obj;
        return this.getFormattedDate().equals(anotherMeasure.getFormattedDate());
    }
}
