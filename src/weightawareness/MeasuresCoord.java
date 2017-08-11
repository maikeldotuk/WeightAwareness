/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weightawareness;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
/**
 * @author Miguel Frias Mosquea
 */
public final class MeasuresCoord {

    ArrayList<Measure> measures;
    File file;

    MeasuresCoord() {
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

    public ArrayList<Measure> getLastTen() {
        ArrayList<Measure> shortened = new ArrayList<>();
        for (int x = 1; x<11; x++) {
            shortened.add(this.measures.get(this.measures.size()-x));
        }
        Collections.reverse(shortened);
        return shortened;

    }


    public boolean addMeasure(double weight, double bodyfat, String aDate) {
        Measure aMeasure = new Measure(weight, bodyfat, LocalDate.now());
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
                LocalDate theDate = Measure.convertStringToLocalDate(aDate);
                Measure newMeasure = new Measure(weight, bodyfat, theDate);
                for (Measure theMeasure : measures) {
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

    public void updateFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Measure aMeasure : measures) {
                String sentence = aMeasure.weight + "\t" + aMeasure.bodyFat + "\t" + aMeasure.getFormattedDate();
                writer.write(sentence);
                writer.newLine();
            }
            System.out.println("File Written");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public ArrayList<Measure> readFile() {
        ArrayList<Measure> theMeasures = new ArrayList<>();
        System.out.println("Loading File");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = reader.readLine()) != null) {
                String[] anArray = s.split("\t");
                Measure aMeasure = new Measure(Double.parseDouble(anArray[0]), Double.parseDouble(anArray[1]), anArray[2]);
                theMeasures.add(aMeasure);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return theMeasures;
    }
}
