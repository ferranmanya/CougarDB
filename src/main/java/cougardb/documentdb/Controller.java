package cougardb.documentdb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cougardb.documentdb.exceptions.CollectionAlreadyExistsException;
import cougardb.documentdb.model.CollectionBlock;
import cougardb.documentdb.model.CougarCollection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

public class Controller {

    private static final String METADATA = "metadata.json";
    private ObjectMapper mapper;
    private static Controller instance = null;
    private List<CougarCollection> collections;

    protected Controller() {
        this.mapper = new ObjectMapper();
        this.collections = readMetadata();
    }

    public static Controller getInstance() {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (Controller.class) {
                if (instance == null) {
                    instance = new Controller();
                }
            }
        }
        return instance;
    }

    public void createCollection(String CollectionName) throws CollectionAlreadyExistsException {
        writeCollection(new CougarCollection(CollectionName));
    }

    private synchronized void writeMetadata() {
        try {
            this.mapper.writeValue(new File(METADATA), this.collections);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized List<CougarCollection> readMetadata(){
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(METADATA));
            return this.mapper.readValue(jsonData, new TypeReference<List<CougarCollection>>(){});
        } catch (IOException e) {
            return new ArrayList<CougarCollection>();
        }
    }

    private synchronized void writeCollection(CougarCollection newCollection) throws CollectionAlreadyExistsException {
        if (this.collections.contains(newCollection)) throw new CollectionAlreadyExistsException("Collection " +newCollection.getCollectionName()+" already exists.");
        this.collections.add(newCollection);
        writeMetadata();
    }

    public void dropCollection(String collectionName) throws FileNotFoundException
    {
        Optional<CougarCollection> result = this.collections.stream().filter(collection -> collection.getCollectionName().equals(collectionName)).findFirst();
        if (result.isEmpty()){
            throw new FileNotFoundException(collectionName + " does not exist.");
        }
        CougarCollection collection = result.get();
        collection.readFileBlocks(false);
        collection.getBlocks().forEach(block -> {
            block.getFile().delete();
        });
        this.collections.remove(collection);
        writeMetadata();
    }

    public Map<String, Object> getCollectionData(String collectionName) throws FileNotFoundException {
        CougarCollection collection = getCollection(collectionName);
        try {
            String json = this.mapper.writeValueAsString(collection);
            Map<String, Object> collectionMap = this.mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            collection.readFileBlocks(true);

            json = this.mapper.writeValueAsString(collection.getBlocks());
            List<Map<String, Object>> blockList = this.mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
            collectionMap.put("blocks", blockList);
            return collectionMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void putCollectionData(String collectionName, Map<String, Object> data) throws FileNotFoundException{
        if(getCollection(collectionName).putData(data)){
            writeMetadata();
        }
    }

    private CougarCollection getCollection(String collectionName) throws FileNotFoundException {
        Optional<CougarCollection> result = this.collections.stream().filter(collection -> collection.getCollectionName().equals(collectionName)).findFirst();
        if (result.isEmpty()){
            throw new FileNotFoundException(collectionName + " does not exist.");
        }
        return result.get();
    }

    public Map<String,Object> getDocumentByID(String collectionName, String id) throws FileNotFoundException {
        CougarCollection collection = getCollection(collectionName);
        collection.readFileBlocks(false);
        for (CollectionBlock block : collection.getBlocks()) {
            block.readData();
            Optional<Map<String, Object>> o = block.getDocumentByID(id);
            if (o.isPresent())
                return o.get();
        }
        throw new FileNotFoundException("Document not found");
    }

    public void deleteDocument(String collectionName, String id) throws FileNotFoundException {
        CougarCollection collection = getCollection(collectionName);
        collection.readFileBlocks(false);
        for (CollectionBlock block : collection.getBlocks()) {
            block.readData();
            if(block.deleteDocumentById(id)){
                return;
            }
        }
        throw new FileNotFoundException("Document not found");
    }

}
