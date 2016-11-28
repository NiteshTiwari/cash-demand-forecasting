import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnaway on 28.11.2016.
 */
public class Method {
    public static void main(String[] args) {
        List<Double> ts = new ArrayList<Double>();
        ts = getData("C:\\1.csv");
        System.out.println(ts);
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
}
