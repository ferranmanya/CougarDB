package cougardb.documentdb;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;

public class Query{

    private List<Map<String, Object>>  jsonQuery;
    private String data;

    public Query(String jsonQuery, String data) {
        this.jsonQuery = JsonPath.read(jsonQuery, "$.query");
        this.data = data;
    }

    private String buildQuery(){
        String queryResult = "$..[?(";
        for (int i = 0; i < this.jsonQuery.size(); i++) {
            if(i>0)
                queryResult += "&& ";
            queryResult += "@."+(String)this.jsonQuery.get(i).get("field")+" "+(String)this.jsonQuery.get(i).get("condition");
            switch ((String)this.jsonQuery.get(i).get("type")){
                case "string":
                    queryResult += " '"+(String)this.jsonQuery.get(i).get("value")+"'";
                    break;
                default:
                    queryResult += " "+(String)this.jsonQuery.get(i).get("value")+" ";
                    break;
            }
        }
        queryResult += ")]";
        System.out.println(queryResult);
        return queryResult;
    }

    public List<Map<String, Object>> getResults(){
        List<Map<String, Object>> result = JsonPath.parse(this.data).read(buildQuery());
        return result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

