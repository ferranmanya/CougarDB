package cougardb.documentdb;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;

public class Controller {
        public static final String METADATA = "metadata.json";
        
        public void CreateCollection(String CollectionName) throws CollectionAlreadyExistsException, IOException {
            // 0.BUSCAR SI EXISTE YA EL CollectionName EN EL FICHERO METADATA  --El caso de error se lanzaria un CollectionAlreadyExistsException
                //Dejo el stream abierto ya que escribire en metadata.json al final del metodo
            
            Gson gson = new Gson();

            String pathname = CollectionName + ".cdb";
            java.util.Date date = new Date();
            CougarCollection newCollection = new CougarCollection(CollectionName, date, new File(pathname));

            this.writeCollection(newCollection, new ArrayList<HashMap<String, Object>>());

            // 3.ESCRIBIR el objeto CougarCollection en el fichero METADATA
            

        }

        public void writeCollection(CougarCollection cougarCollection, ArrayList<HashMap<String, Object>> data) throws IOException {
            FileOutputStream fos= new FileOutputStream(cougarCollection.getPath());
            ObjectOutputStream os = new ObjectOutputStream (fos);
            os.writeObject(data);
            fos.close();
            os.close();
        }

}
