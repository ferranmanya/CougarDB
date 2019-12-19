package cougardb.documentdb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@JsonIgnoreProperties(value = { "mapper", "file" })
public class CollectionBlock {

    private int id;
    private String collectionName;
    private File file;
    private double maxFileSize; // kb
    private Date creationDate;
    private List<Map<String, Object>> data = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();

    public CollectionBlock(){}

    public CollectionBlock(String collectionName, int id, double maxFileSize) {
        this.id = id;
        this.collectionName = collectionName;
        this.creationDate = new Date();
        this.maxFileSize = maxFileSize;
        this.file = new File("./data/"+collectionName+"."+id+".cdb");
    }

    public void readData(ReentrantReadWriteLock lock){
        lock.readLock().lock();
        try {
            if(this.file.exists()){
                byte[] jsonData = Files.readAllBytes(Paths.get(this.file.getPath()));
                Map<String, Object> map = this.mapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {});
                this.data = (List<Map<String, Object>>) map.get("data");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void putData(Map<String, Object> json, ReentrantReadWriteLock lock){
        lock.writeLock().lock();
        try {
            this.data.add(json);
            this.mapper.writeValue(this.file, this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public double getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(double maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean equals(Object o) {
        if (!(o instanceof CollectionBlock)) {
            return false;
        }
        CollectionBlock other = (CollectionBlock) o;
        String name = collectionName+""+id;
        return name.equals(other.collectionName+""+id);
    }

    public int hashCode() {
        String name = collectionName+""+id;
        return name.hashCode();
    }

    public Optional<Map<String, Object>> getDocumentByID(String id) {
        return this.data.stream().filter(doc -> doc.get("id").equals(id)).findFirst();
    }

    public boolean deleteDocumentById(String id){
        try {
            Optional<Map<String, Object>> result = this.data.stream().filter(doc -> doc.get("id").equals(id)).findFirst();
            if(result.isPresent()){
                this.data.remove(result.get());
                this.mapper.writeValue(this.file, this);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
