import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableSaw {

    public static void main(String[] args) {
        try {
            Table df = Table.read().csv("titanic.csv");

            System.out.println(df.structure());
            System.out.println(df.summary());
            df = TableSaw.addColumn(df);
            System.out.println(df.structure());
            System.out.println(df.summary());
            Table newDataFrame = df.select("pclass", "survived", "name", "FakeColumn");
            Table newDataFrame2 = df.select("parch", "ticket", "fare", "FakeColumn");
            Table joinedTable = newDataFrame.joinOn("FakeColumn").inner(newDataFrame2);
            System.out.println(joinedTable.structure());
            System.out.println(df.structure());
            int[] genders =  df.stream()
                    .mapToInt((row) -> {
                        String gender = row.getString("sex");
                        if (gender.equals("male"))
                            return 1;
                        return 0;
                    }).toArray();


//            int[] genders = df.stream().mapToInt(row -> {
//                String gender = row.getString("sex");
//                if (gender.equals("female"))
//                    return 1;
//                else
//                    return 0;
//            }).toArray();
            Table newDf3=df.select("body");
            df.removeColumns("sex");
            IntColumn column=IntColumn.create("Gender",genders);
            df.addColumns(column);
            System.out.println(df.structure());

            System.out.println(TableSaw.addFakeDate(df).structure());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Table addColumn(Table df) {
        Integer[] values = new Integer[df.rowCount()];
        for (int i = 0; i < df.rowCount(); i++) {
            values[i] = i + 1;
        }

        IntColumn column = IntColumn.create("FakeColumn", values);
        return df.addColumns(column);
    }
    public static Table addFakeDate(Table df){
        List<LocalDate> dateList=new ArrayList<>();
        for(int i=0;i<df.rowCount();i++)
        {


            dateList.add(LocalDate.of(2020,5,i%30==0?1:i%30));
        }
        DateColumn column = DateColumn.create("FakeDateColumn", dateList);
        return df.addColumns(column);
    }
}
