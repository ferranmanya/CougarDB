package cougardb.documentdb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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


        File directory = new File("data");
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }

        this.file = new File("data/"+collectionName+"."+id+".cdb");
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
}
