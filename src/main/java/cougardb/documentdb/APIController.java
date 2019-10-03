package cougardb.documentdb;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = {"http://localhost:8888"})
@RestController
@RequestMapping("/cougarAPI")
public class APIController {

    // TODO: Create the collection and send a response.
    @PostMapping("/collections")
    public ResponseEntity<?> createCollection(@RequestBody Map<String, Object> collectionData){
        Map<String, Object> response = new HashMap<>();
        String nom = collectionData.get("name").toString();
        response.put("message", "Collection saved successfully");
        response.put("collection", nom);
        try
        {
            Controller c = new Controller();
            c.CreateCollection(nom);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
        }
        catch (CollectionAlreadyExistsException e){
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        catch (IOException e)
        {
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO: Show data from the collection.
    @GetMapping("/collections/{collectionName}")
    public ResponseEntity<?> showCollection(@PathVariable String collectionName){
        Map<String, Object> response = new HashMap<>();
        response.put("collectionName", collectionName);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
    }

    // TODO: Delete the collection and send a response.
    @DeleteMapping("/collections/{collectionName}")
    public ResponseEntity<?> dropCollection(@PathVariable String collectionName){
        Map<String, Object> response = new HashMap<>();
        response.put("collectionName", collectionName);
        try{
            Controller c = new Controller();
            c.dropCollection(collectionName);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
        }
        catch (FileNotFoundException e)
        {
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        catch (IOException e)
        {
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}