/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weightawareness;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 *
 * @author maike
 */
public final class MeasuresCoord {

    ArrayList<Measure> measures;
    File file;

    MeasuresCoord() throws IOException, FileNotFoundException, ParseException {

        //Task 1, check the file exist and if not, create it. Pg 460 of Sybex OCP
        file = new File("data.txt");
        if (file.exists()) {
            measures = readFile();
        } else {
            measures = new ArrayList<Measure>();
        }
    }

    public ArrayList<Measure> getAll() {
        return this.measures;
    }

    public boolean addMeasure(double weight, double bodyfat, String aDate) throws IOException, ParseException {
        Measure aMeasure = new Measure(weight, bodyfat, Calendar.getInstance());

        if (measures.isEmpty()) {
            measures.add(aMeasure);
            System.out.println("New data added");
            updateFile();
            return true;
        } else {

            if (aDate.isEmpty()) {
                Measure lastMeasure = measures.get(measures.size() - 1);

                //This prevent adding another measure for today.
                if (aMeasure.equals(lastMeasure)) {
                    System.out.println("Date already added");
                    return false;
                } else {
                    measures.add(aMeasure);
                    System.out.println("New data added");
                    updateFile();
                    return true;
                }
            } else {
                Calendar theDate = Measure.convertStringToCal(aDate);
                Measure newMeasure = new Measure(weight, bodyfat, theDate);
                for (Measure theMeasure: measures) {
                    if (theMeasure.equals(newMeasure)) {
                        measures.set(measures.indexOf(theMeasure), newMeasure);
                        updateFile();
                        return true;
                    }
                }
                return false;
               
            }

        }

    }

    public void updateFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Measure aMeasure : measures) {
                String sentence = aMeasure.weight + "\t" + aMeasure.bodyFat + "\t" + aMeasure.getFormatedDate();
                writer.write(sentence);
                writer.newLine();
            }
            System.out.println("File Writen");
        }

    }

    public ArrayList<Measure> readFile() throws FileNotFoundException, IOException, ParseException {
        ArrayList<Measure> theMeasures = new ArrayList<>();
        System.out.println("Loading File");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = reader.readLine()) != null) {
                String[] anArray = s.split("\t");
                Measure aMeasure = new Measure(Double.parseDouble(anArray[0]), Double.parseDouble(anArray[1]), anArray[2]);
                theMeasures.add(aMeasure);
            }
        }
        return theMeasures;
    }

    public Measure getLast() {
        return this.measures.get(this.measures.size() - 1);
    }

    public void modMeasureByDate() {
        //TODO
    }

}
