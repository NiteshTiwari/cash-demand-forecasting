import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rnaway on 28.11.2016.
 */
public class Method {
    public static void main(String[] args) {
        List<Double> ts = new ArrayList<Double>();
        ts = getData("C:\\1.csv");
        System.out.println(ts);
        System.out.println(interquartileDistanceMethod(ts));
        System.out.println(irwinMethod(ts));

        for (int i : interquartileDistanceMethod(ts)) {
            System.out.print(ts.get(i) + " ");
        }
        System.out.println();
        for (int i : irwinMethod(ts)) {
            System.out.print(ts.get(i) + " ");
        }
    }

    public static List<Double> getData(String filePath) {
        List<Double> ts = new ArrayList<Double>();
        BufferedReader reader;
        try (FileInputStream fin = new FileInputStream(filePath)) {
            reader = new BufferedReader(new InputStreamReader(fin));
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                if (line.equals("na")) {
                    ts.add(Double.NaN);
                } else {
                    ts.add(Double.parseDouble(line));
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
        } catch (IOException e) {
            System.out.println("Произошла ошика ввода-вывода");
        }
        return ts;
    }


    public static List<Integer> interquartileDistanceMethod(List<Double> ts) {
        List<Double> sortedTs = new ArrayList<>(ts);

        Collections.sort(sortedTs, new Comparator<Double>() {
            @Override
            public int compare(Double d1, Double d2) {
                return Double.compare(d1, d2);
            }
        });

        int n = sortedTs.size();
        int medianRank = (1 + n) / 2;
        int lowerQuartileRank = (1 + medianRank) / 2;
        int upperQuartileRank = n + 1 - lowerQuartileRank;

        Double lowerQuartile = sortedTs.get(lowerQuartileRank-1);
        Double upperQuartile = sortedTs.get(upperQuartileRank-1);

        List<Integer> outliers = new ArrayList<Integer>();

        for (int i=0; i<ts.size(); i++) {
            if (ts.get(i) > upperQuartile + 1.5*(upperQuartile - lowerQuartile) || ts.get(i) < lowerQuartile - 1.5*(upperQuartile - lowerQuartile)) {
                outliers.add(i);
            }
        }
        return outliers;
    }
    public static Double average(List<Double> data) {
        Double sum = 0.0;
        for (Double d : data) {
            sum += d;
        }
        return sum/data.size();
    }

    public static Double standardDeviation(List<Double> data) {
        Double average = average(data);
        Double sum = 0.0;
        for (Double d : data) {
            sum += Math.pow(d - average, 2);
        }
        return Math.sqrt(sum/(data.size()-1));
    }

    public static List<Integer> irwinMethod(List<Double> ts) {
        List<Integer> outliers = new ArrayList<Integer>();
        Double standardDeviation = standardDeviation(ts);
        Double criterion;
        for (int i=1; i < ts.size(); i++) {
            criterion = Math.abs(ts.get(i)-ts.get(i-1)) / standardDeviation;
            if (criterion > 2.95) {
                outliers.add(i);
            }

        }
        return outliers;
    }
}
