package cougardb.documentdb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public CougarCollection(){}

    public CougarCollection(String collectionName) {
        this.collectionName = collectionName;
        this.creationDate = new Date();
        this.currentId = 0;
        this.maxFileSize = 1.;
    }

    public synchronized void readFileBlocks(boolean readData){
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
        if(id.length() != 0){
            data.put("id", id);
        }
        else{
            data.put("id", UUID.randomUUID());
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

            }else{ // otherwise, we create a new block
                this.currentId++;
                CollectionBlock block = new CollectionBlock(this.collectionName, this.currentId, this.maxFileSize);

                block.putData(data);
                blocks.add(block);

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void loadMap(){
        this.indexManager = new IndexManager(collectionName);
        this.blocks = new ArrayList<>();
        for (int i = 0; i <= this.currentId; i++) {
            CollectionBlock block = new CollectionBlock(this.collectionName, i, this.maxFileSize);
            if(block.reloadData()!=null ) {
                for(i=0; i < block.reloadData().size(); i++) {
                    UUID reload = UUID.fromString((String) block.reloadData().get(i).get("id"));
                    indexManager.getIndex().put(reload, block.getId());
                }
            }
        }
        System.out.println(indexManager.getIndex().size());
    }
}
