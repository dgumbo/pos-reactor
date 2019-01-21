
package zw.co.hisolutions.pos.reports.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
//@Data
@NoArgsConstructor
public class ReportData {
    String[] columnNames;
    Object[][] data;

    public void setDataMap(List<Map<String, Object>> result) {
        data=new String[result.size()][columnNames.length];
        for(int j=0;j<data.length;j++){
            for(int i=0;i<columnNames.length;i++){
                data[j][i]=""+result.get(j).get(columnNames[i]);
            }
        }
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) { 
        Arrays.parallelSetAll(columnNames, (i) -> columnNames[i].trim());
     
        this.columnNames = columnNames; 
    }

    public void setColumnNames(List<String> columnNames) {
        columnNames.forEach(c -> {columnNames.set(columnNames.indexOf(c), c.trim());});
     
        this.columnNames = columnNames.toArray(new String[0]);
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }
    
    
}
