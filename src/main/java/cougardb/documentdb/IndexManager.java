package cougardb.documentdb;

import java.util.TreeMap;
import java.util.UUID;

public class IndexManager {

    private TreeMap<UUID, Integer> index;
    private String collectionName;

    public IndexManager(String collectionName){
        // TODO: Si existe este TreeMap en Memoria cargarlo
        this.index = new TreeMap<>();
        this.collectionName = collectionName;
    }

    public void addIndex(UUID id, int idBlock){
        index.put(id, idBlock);
    }
    public void deleteIndex(UUID id){
        if(index.containsKey(id))   index.remove(id);

    }
    public void updateID(UUID id, int idBlock){
        deleteIndex(id);
        addIndex(id, idBlock);
    }
    public int getIDBlockByID(UUID id){
        return this.index.get(id);
    }

}
