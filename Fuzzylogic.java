/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzylogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.NativeMath.round;
import java.lang.Double;
/**
 *
 * @author hana
 */
public class Fuzzylogic {

    //load csv file
    static String[][] loadCSV() throws IOException{
        String thisLine; 
        String[][] data = null;
        try {
            BufferedReader readData = new BufferedReader(new FileReader("D:\\AI\\influencers.csv")); //memanggil file text
            List<String[]> lines = new ArrayList<String[]>();
            readData.readLine();
            while ((thisLine = readData.readLine()) != null) {
                lines.add(thisLine.split(","));
            }
            data = new String[lines.size()][3];
            lines.toArray(data);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Fuzzylogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
    
    static void printString(String[][] array){
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j]);   
            }
             System.out.println("");
        }
    }
    
    //convert string to double
    static double[][] convertToDouble (String[][] array){
        double[][] result = new double[100][3];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result[i][j] = Double.parseDouble(array[i][j]); 
            }
        }
        return result;
    }
    
    static void printDouble(double[][] array){
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j]);   
            }
             System.out.println("");
        }
    }
    
    //calculate gradien 
    static double leftGradien(double a, double b, double x){
        double n = (x-a)/(b-a);
        /*n = n*100;
        n = Math.round(n);
        n = n /100;*/
        return n;
    }
    
    static double rightGradien(double a, double b, double x){
        double n = (a-x)/(a-b);
        /*n = n*100;
        n = Math.round(n);
        n = n /100;*/
        return n;
    }
    
    //follower count input
    static double folCountLarge(double x){
        double result = 0;
        if (x <= 100000 && x >= 50000) {
            result = 1;
        }else if (x <= 40000){
            result = 0;
        }else if (x > 40000 && x < 50000){
            result = leftGradien(40000, 50000, x);
        }
        return result;
    }
    
    static double folCountSmall(double x){
        double result = 0;
        if (x <= 20000) {
            result = 1;
        }else if (x <= 100000 && x >= 30000){
            result = 0;
        }else if (x > 20000 && x < 30000){
            result = rightGradien(30000, 20000, x);
        }
        return result;
    }
    
    static double folCountMedium(double x){
        double result = 0;
        if (x <= 50000 && x >= 30000) {
            result = 1;
        }else if (x <= 20000){
            result = 0;
        }else if (x >= 60000){
            result = 0;
        }else if (x < 30000 && x > 20000){
            result = leftGradien(20000, 30000, x);
        }else if (x < 60000 && x > 50000){
            result = rightGradien(60000, 50000, x);
        }
        return result;
    }
    
    //engagement rate input
    static double engRateHigh(double x){
        double result = 0;
        if (x <= 10 && x >= 6) {
            result = 1;
        }else if (x <= 5){
            result = 0;
        }else if (x > 5 && x < 6){
            result = leftGradien(5, 6, x);
        }
        return result;
    }
    
    static double engRateLow(double x){
        double result = 0;
        if (x <= 2) {
            result = 1;
        }else if (x <= 10 && x >= 3){
            result = 0;
        }else if (x > 2 && x < 3){
            result = rightGradien(3, 2, x);
        }
        return result;
    }
    
    static double engRateAvg(double x){
        double result = 0;
        if (x <= 6 && x >= 3) {
            result = 1;
        }else if (x <= 2.5){
            result = 0;
        }else if (x >= 6.8){
            result = 0;
        }else if (x < 3 && x > 2.5){
            result = leftGradien(2.5, 3, x);
        }else if (x < 6.8 && x > 6){
            result = rightGradien(6.8, 6, x);
        }
        return result;
    }
    
    //determine linguistik input value
    static double[][] fuzzification (double[][] array){
        double[][] membership = new double[100][7];
        for (int i = 0; i < array.length; i++) {
            membership[i][0] = array[i][0];
            //follower count Large
            membership[i][1] = folCountLarge(array[i][1]);
            //follower count Medium
            membership[i][2] = folCountMedium(array[i][1]);
            //follower count Small
            membership[i][3] = folCountSmall(array[i][1]);
            //engagement rate High
            membership[i][4] = engRateHigh(array[i][2]);
            //engagement rate Avg
            membership[i][5] = engRateAvg(array[i][2]);
            //engagement rate Low
            membership[i][6] = engRateLow(array[i][2]);
        }
        return membership;
    }
    
    //determine output value from input value using conjuction rule
    static double[][] inferenceRule (double[] record){
        double[][] rule = new double[10][2];
        double min = 0;
            //Large and High = Accepted
            rule[1][0] = 3;
            min = record[1];
            if(min > record[4]){
                min = record[4];
            }
            rule[1][1] = min;
            //Large and Avg = Accepted
            rule[2][0] = 3;
            min = record[1];
            if(min > record[5]){
                min = record[5];
            }
            rule[2][1] = min;
            //Large and Low = Considered
            rule[3][0] = 2;
            min = record[1];
            if(min > record[6]){
                min = record[6];
            }
            rule[3][1] = min;
            //Medium and High = Accepted
            rule[4][0] = 3;
            min = record[2];
            if(min > record[4]){
                min = record[4];
            }
            rule[4][1] = min;
            //Medium and Avg = Considered
            rule[5][0] = 2;
            min = record[2];
            if(min > record[5]){
                min = record[5];
            }
            rule[5][1] = min;
            //Medium and Low = Considered
            rule[6][0] = 2;
            min = record[2];
            if(min > record[6]){
                min = record[6];
            }
            rule[6][1] = min;
            //Small and High = Considered
            rule[7][0] = 2;
            min = record[3];
            if(min > record[4]){
                min = record[4];
            }
            rule[7][1] = min;
            //Small and Avg = Reject
            rule[8][0] = 1;
            min = record[3];
            if(min > record[5]){
                min = record[5];
            }
            rule[8][1] = min;
            //Medium and Low = Reject
            rule[9][0] = 1;
            min = record[3];
            if(min > record[6]){
                min = record[6];
            }
            rule[9][1] = min;
        return rule;
    }
    
    //determine maximum output value using disjunction rule
    static double outputAccepted(double[][] rule){
        double output;
        double max = 0;
        //determine Accepted
        for (int i = 0; i < rule.length; i++) {
            if (rule[i][0] == 3.0){
                if (rule[i][1] > max){
                    max = rule[i][1];
                }
            }
        }
        output = max;
        return output;
    }
    
    static double outputConsidered(double[][] rule){
        double output;
        double max = 0;
        for (int i = 0; i < rule.length; i++) {
            if (rule[i][0] == 2.0){
                if (rule[i][1] > max){
                    max = rule[i][1];
                }
            }
        }
        output = max;
        return output;
    }
    
    static double outputRejected(double[][] rule){
        double output;
        double max = 0;
        for (int i = 0; i < rule.length; i++) {
            if (rule[i][0] == 1.0){
                if (rule[i][1] > max){
                    max = rule[i][1];
                }
            }
        }
        output = max;
        return output;
    }
    //determine inference rule
    //deterine linguistik output value
    static double[][] inference(double[][] data){
        double[][] result = new double[100][4];
        for (int i = 0; i < data.length; i++) {
            double[][] rule = inferenceRule(data[i]);
            result[i][0] = data[i][0];
            result[i][1] = outputAccepted(rule);
            result[i][2] = outputConsidered(rule);
            result[i][3] = outputRejected(rule);
        }
        return result;
    }

    //determine membership output function
    static double memAccepted(double x){
        double result = 0;
        if (x <= 20 && x >=15) {
            result = 1;
        }else if (x <= 11){
            result = 0;
        }else if (x > 11 && x < 15){
            result = leftGradien(11, 15, x);
        }
        return result;
    }
    
    static double memRejected(double x){
        double result = 0;
        if (x <= 7) {
            result = 1;
        }else if (x <= 20 && x >= 11){
            result = 0;
        }else if (x > 7 && x < 11){
            result = rightGradien(11, 7, x);
        }
        return result;
    }
    
    static double memConsidered(double x){
        double result = 0;
        if (x == 11) {
            result = 1;
        }else if (x <= 7){
            result = 0;
        }else if (x >= 15){
            result = 0;
        }else if (x < 11 && x > 7){
            result = leftGradien(7, 11, x);
        }else if (x < 15 && x > 11){
            result = rightGradien(15, 11, x);
        }
        return result;
    }
    
    static Random rand = new Random();
    
    static double[][] outputFunction(){
        double[][] temp = new double[10][4];
        for (int i = 0; i < temp.length; i++) {
            temp[i][0] = rand.nextInt(21);
            //Accepted
            temp[i][1] = memAccepted(temp[i][0]);
            //Considered
            temp[i][2] = memConsidered(temp[i][0]);
            //Rejected
            temp[i][3] = memRejected(temp[i][0]);
        }
        return temp;
    }
    
    static double[][] defuzzyReplace(double[][] membership, double[] data){
        double[][] result = new double[10][4];
        //determine accepted
        if (data[1] <= 1 && data[1] > 0.8 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] >= 15){
                    membership[i][1] = data[1];
                }
            }
        }else if(data[1] <= 0.8 && data[1] > 0.6 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] >= 14){
                    membership[i][1] = data[1];
                }
            }
        }else if(data[1] <= 0.6 && data[1] > 0.4 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] >= 13){
                    membership[i][1] = data[1];
                }
            }
        }else if (data[1] <= 0.4 && data[1] > 0.2 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] >= 12){
                    membership[i][1] = data[1];
                }
            }
        }else if(data[1] <= 0.2 && data[1] > 0 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] >= 11){
                    membership[i][1] = data[1];
                }
            }
        }
        //determine considered
        if (data[2] <= 1 && data[2] > 0.8 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] == 11){
                    membership[i][2] = data[2];
                }
            }
        }else if(data[2] <= 0.8 && data[2] > 0.6 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] <= 12 && membership[i][0] >= 10 ){
                    membership[i][2] = data[2];
                }
            }
        }else if(data[2] <= 0.6 && data[2] > 0.4 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] <= 13 && membership[i][0] >= 9){
                    membership[i][2] = data[2];
                }
            }
        }else if (data[2] <= 0.4 && data[2] > 0.2 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] <= 14 && membership[i][0] >= 8){
                    membership[i][2] = data[2];
                }
            }
        }else if(data[2] <= 0.2 && data[2] > 0 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] <= 15 && membership[i][0] >= 7){
                    membership[i][2] = data[2];
                }
            }
        }
        //determine rejected
        if (data[3] <= 1 && data[3] > 0.8 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] <= 7){
                    membership[i][3] = data[3];
                }
            }
        }else if(data[3] <= 0.8 && data[3] > 0.6 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] <= 8){
                    membership[i][3] = data[3];
                }
            }
        }else if(data[3] <= 0.6 && data[3] > 0.4 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] <= 9){
                    membership[i][3] = data[3];
                }
            }
        }else if (data[3] <= 0.4 && data[3] > 0.2 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] <= 10){
                    membership[i][3] = data[3];
                }
            }
        }else if(data[3] <= 0.2 && data[3] > 0 ){
            for (int i = 0; i < membership.length; i++) {
                if (membership[i][0] <= 11){
                    membership[i][3] = data[3];
                }
            }
        }
        result = membership.clone();
        return result;
    }
    
    //using mamdani method
    static double[][] defuzzification(double[][] record){
        double[][] result = new double[100][2];
        double [][] outputFunc = outputFunction();
        for (int i = 0; i < record.length; i++) {
            result[i][0] = record[i][0];
            double[][] func = defuzzyReplace(outputFunc, record[i]);
            double[][] sigma = new double[10][2];
            for (int j = 0; j < func.length; j++) {
                double max = func[j][1];
                for (int k = 1; k < func[j].length; k++) {
                    if (func[j][k] > max){
                        max = func[j][k];
                    }
                }
                sigma[j][0] = func[j][0];
                sigma[j][1] = max;
            }
            //determine the equation
            double upper = 0;
            double downer = 0;
            for (int n = 0; n < sigma.length; n++) {
                double x = sigma[n][0]*sigma[n][1];
                upper = upper+x;
                downer = downer+sigma[n][1];
            }
            double x = upper/downer;
            result[i][1] = x;
        }
        return result;
    }
    
    static int[] findTheBest(double[][] data){
        int[] record = new int[20];
        int n = 0;
        for (int j = 0; j < 20; j++) {   
            double max = 0;
            for (int i = 0; i < data.length; i++) {
                if(data[i][1] > max ){
                    max = data[i][1];
                    n = i;
                }
            }
            record[j] = n;
            data[n][1] = 0;
        }
        return record;
    }
    
    //write into csv file
    static void writeCSV(int[] chosen){
        try (Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("chosen.csv")))){
            wr.write("ID" + "\n");
            for (int i = 0; i < chosen.length; i++) {
                int j = chosen[i];
                wr.write(j + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
       String[][] array = new String[100][3];
       array = loadCSV();
        //printString(array);
        double[][] data = convertToDouble(array);
        //printDouble(data);
        double[][] membership = fuzzification(data);
        //printDouble(membership);
        double[][] output = inference(membership);
        //printDouble(output);
        double[][] result = defuzzification(output);
        //printDouble(result);
        int[] chosen = findTheBest(result);
        writeCSV(chosen);
       
    }
    
}
