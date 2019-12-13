package cougardb.documentdb.model;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.TreeMap;
import java.util.UUID;

public class IndexManager implements Serializable {

    private TreeMap<UUID, Integer> index = null;
    private String collectionName;

    public IndexManager(String collectionName){
        this.collectionName = collectionName;
        this.index = new TreeMap<>();
    }

    public TreeMap<UUID, Integer> getIndex() {
        return index;
    }

    @PreDestroy
    public void save(){

        String path = "./index/"+this.collectionName;

        try {
            FileOutputStream file = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(this);
            out.close();
            file.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
