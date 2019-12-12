package cougardb.documentdb;

import java.util.TreeMap;
import java.util.UUID;

public class IndexManager {

    private TreeMap<UUID, String> index;
    private String collectionName;

    public IndexManager(String collectionName){
        // TODO: Si existe este TreeMap en Memoria cargarlo
        this.index = new TreeMap<>();
        this.collectionName = collectionName;
    }

    public void addIndex(UUID id, String path){
        index.put(id, path);
    }
    public void deleteIndex(UUID id){
        if(index.containsKey(id))
            index.remove(id);
    }
    public void modifyID(UUID id, String path){
        deleteIndex(id);
        addIndex(id, path);
    }
    public String getPathByID(UUID id){
        return this.index.get(id);
    }


}
