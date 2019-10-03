package cougardb.documentdb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Controller {

    private static final String METADATA = "metadata.json";

    public void CreateCollection(String CollectionName) throws CollectionAlreadyExistsException, IOException {
        CougarCollection newCollection = new CougarCollection(CollectionName);
        ArrayList<CougarCollection> collections = this.readMetadata();
        if(collections.contains(newCollection)){
            throw new CollectionAlreadyExistsException("");
        }
        this.writeCollection(newCollection, new ArrayList<HashMap<String, Object>>());
        this.writeMetadata(collections);
    }

    private void writeMetadata(ArrayList<CougarCollection> collections) throws IOException {
        Gson gson = new Gson();
        gson.toJson(collections, new FileWriter(METADATA));
    }


    private ArrayList<CougarCollection> readMetadata() throws FileNotFoundException {
        Gson gson = new Gson();
        Type collectionList = new TypeToken<ArrayList<CougarCollection>>(){}.getType();
        return gson.fromJson(new FileReader(METADATA), collectionList);
    }

    private void writeCollection(CougarCollection cougarCollection, ArrayList<HashMap<String, Object>> data) throws IOException {
        FileOutputStream fos= new FileOutputStream(cougarCollection.getPath());
        ObjectOutputStream os = new ObjectOutputStream (fos);
        os.writeObject(data);
        fos.close();
        os.close();
    }

    public void dropCollection(String CollectionName) throws IOException
    {
        File f = new File(CollectionName+".cdb");
        if(!f.exists())
        {
            throw new FileNotFoundException(CollectionName + ".cdb does not exist.");
        }
        else
        {
            f.delete();
            ArrayList<CougarCollection> metadata = readMetadata();
            metadata.remove(new CougarCollection(CollectionName));
            writeMetadata(metadata);
        }
    }

}
