package cougardb.documentdb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import cougardb.documentdb.IndexManager;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.io.*;

@JsonIgnoreProperties(value = { "mapper", "blocks" })
public class CougarCollection {

    private String collectionName;
    private Date creationDate;
    private List<CollectionBlock> blocks = new ArrayList<>();
    private double maxFileSize; // kb
    private int currentId;
    private ObjectMapper mapper = new ObjectMapper();
    private IndexManager indexManager;

    public CougarCollection(){}

    public CougarCollection(String collectionName) {
        this.collectionName = collectionName;
        this.creationDate = new Date();
        this.currentId = 0;
        this.maxFileSize = 1.;
        this.indexManager = new IndexManager(this.collectionName);

    }

    public void readFileBlocks(boolean readData){
        this.blocks = new ArrayList<>();
        for (int i = 0; i <= this.currentId; i++) {
            CollectionBlock block = new CollectionBlock(this.collectionName, i, this.maxFileSize);
            if(readData){
                block.readData();
            }
            blocks.add(block);
        }
    }

    public boolean putData(Map<String, Object> data, String id){

        UUID final_id = UUID.randomUUID();

        if(id.length() != 0){
            data.put("id", id);
            //final_id = 0;
        }
        else{
            // UUID aux = UUID.randomUUID();
            data.put("id", final_id);
            System.out.println(final_id);
        }
        readFileBlocks(false); // load block list into memory
        //TODO escriure sempre a l'última pàgina (opc. defragmentacio, repaginació whatevs)
        try {
            String json = this.mapper.writeValueAsString(data); // data to formatted json string
            final int dataLength = json.getBytes(StandardCharsets.UTF_8).length; // calculate the length of data
            Optional<CollectionBlock> result = blocks.stream().filter(block -> block.getFile().length() + dataLength < maxFileSize*1024).findFirst();
            if(result.isPresent()){ // in case there is a block with enough available space, we insert it there
                CollectionBlock block = result.get();
                block.readData();
                block.putData(data);
                int idBlock = block.getId();
                indexManager.addIndex(final_id, idBlock);
            }else{ // otherwise, we create a new block
                this.currentId++;
                CollectionBlock block = new CollectionBlock(this.collectionName, this.currentId, this.maxFileSize);
                block.putData(data);
                blocks.add(block);
                int idBlock = block.getId();
                indexManager.addIndex(final_id, idBlock);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String path = "./data/"+this.collectionName+"."+Integer.toString(this.currentId)+".cdb";


        return false;
    }

    private void writeToFile(File file, String json){
        try {
            BufferedWriter writer = Files.newBufferedWriter(file.toPath());
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CollectionBlock getBlockByID(int id){
        return getBlocks().get(id);

    }

    public double getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(double maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<CollectionBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<CollectionBlock> blocks) {
        this.blocks = blocks;
    }

    public int getCurrentId() {
        return currentId;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    public boolean equals(Object o) {
        if (!(o instanceof CougarCollection)) {
            return false;
        }
        CougarCollection other = (CougarCollection) o;
        return collectionName.equals(other.collectionName);
    }

    public int hashCode() {
        return collectionName.hashCode();
    }

    public IndexManager getIndexManager(){
        return this.indexManager;
    }
}
