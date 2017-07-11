/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weightawareness;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 *
 * @author miguelfriasmosquea
 */
public class Measure {

    double weight;
    double bodyFat;
    Calendar date;

    Measure(double weight, double bodyFat, Calendar date)
    {
        this.weight = weight;
        this.bodyFat = bodyFat;
        this.date = date;
    }
    
        Measure(double weight, double bodyFat, String date) throws ParseException
    {
        this.weight = weight;
        this.bodyFat = bodyFat;
        this.date = convertStringToCal(date);
    }
    
    @Override
    public String toString() {
        String humanDate = getFormatedDate();
        String description = humanDate + "\t" + weight + "Kgs\t" + bodyFat + "% bf";
        return description;
    }
    
    public String getFormatedDate() {
        int theMonth = date.get(Calendar.MONTH) +1;
        int theDate = date.get(Calendar.DATE);
        String theStringMonth, theStringDate;
        
        //This could be done with a lambda
        if (theMonth <10) {
            theStringMonth = "0" + theMonth;
        } else {
            theStringMonth = "" + theMonth;
        }
        
        if (theDate <10) {
            theStringDate = "0" + theDate;
        } else {
            theStringDate = "" + theDate;
        }
        return theStringDate + "/" + theStringMonth + "/" + date.get(Calendar.YEAR);
    }
    
    public static Calendar convertStringToCal(String theString) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        cal.setTime(sdf.parse(theString));
        return cal;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Measure)) return false;
        Measure anotherMeasure = (Measure) obj;
        return this.getFormatedDate().equals(anotherMeasure.getFormatedDate());
    }
}
