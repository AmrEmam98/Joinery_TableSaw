import joinery.DataFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JoineryExample {

    public static void main(String[] args) {
        try {
            DataFrame<Object> df = DataFrame.readCsv("titanic.csv");
            System.out.println(df.describe());
            System.out.println("Max for pclass " + df.describe().col(0).get(4));
            df = JoineryExample.addColumn(df);

            System.out.println(df.select(objects -> {
                return (objects.get(0).equals(Long.parseLong("1")));
            }).describe());

            DataFrame newDataFrame = df.retain("pclass", "survived", "age", "joinedColumn");
            DataFrame newDataFrame2 = df.retain("parch", "fare", "body", "joinedColumn");
            DataFrame newDataFrame3 = df.retain("joinedColumn");
            System.out.println(newDataFrame.joinOn(newDataFrame2, DataFrame.JoinType.INNER, "joinedColumn").describe());
            System.out.println(df.describe());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static DataFrame addColumn(DataFrame df) {
        List<Integer> newColValues = new ArrayList<>();
        for (int i = 0; i < df.length(); i++)
            newColValues.add(i + 1);

        return df.add("joinedColumn", newColValues);
    }
}
