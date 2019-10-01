package cougardb.documentdb;

import java.util.Date;
import java.io.*;

public class CougarCollection {
    private String CollectionName;
    private Date creationDate;
    private File path;

    CougarCollection(String CollectionName, Date creationDate, File path){
        this.CollectionName = CollectionName;
        this.creationDate = creationDate;
        this.path = path;
    }

    public String getCollectionName() {
        return CollectionName;
    }

    public void setCollectionName(String collectionName) {
        CollectionName = collectionName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    @Override
    public String toString(){
        return "{" + "\"Name\" : \"" + this.CollectionName + "\","
                + "CreationDate : \"" + this.creationDate.toString() +"\"," 
                + "FilePath : \"" + this.path.toString() + "\"}";
    }
    

}
