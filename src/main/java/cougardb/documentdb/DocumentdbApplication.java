package cougardb.documentdb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

@SpringBootApplication
public class DocumentdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentdbApplication.class, args);

        Gson gson = new Gson();
        ArrayList<CougarCollection> ccs = new ArrayList<CougarCollection>();
        ccs.add(new CougarCollection("AAA"));
        ccs.add(new CougarCollection("BBB"));

        Type collectionList = new TypeToken<ArrayList<CougarCollection>>(){}.getType();
        ccs = gson.fromJson(gson.toJson(ccs), collectionList);
        System.out.println(gson.toJson(ccs));
    }

}
