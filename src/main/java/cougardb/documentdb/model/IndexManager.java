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

}
