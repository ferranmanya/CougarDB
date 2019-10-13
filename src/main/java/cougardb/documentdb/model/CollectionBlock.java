package cougardb.documentdb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@JsonIgnoreProperties(value = { "mapper" })
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
        this.file = new File(collectionName+"."+id+".cdb");
    }

    public void restoreData(){
        try {
            if(this.file.exists()){
                byte[] jsonData = Files.readAllBytes(Paths.get(this.file.getPath()));
                Map<String, Object> map = this.mapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {});
                this.data = (List<Map<String, Object>>)map.get("data");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putData(Map<String, Object> json){
        try {
            this.data.add(json);
            this.mapper.writeValue(this.file, this);
        } catch (IOException e) {
            e.printStackTrace();
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
        for (Map<String, Object> document : this.data) {
            if (document.get("id").equals(id)) {
                return Optional.of(document);
            }
        }
        return Optional.empty();
    }
}