package cougardb.documentdb;

import cougardb.documentdb.exceptions.CollectionAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = {"http://localhost:8888"})
@RestController
@RequestMapping("/cougarAPI")
public class APIController {

    @PostMapping("/collections")
    public ResponseEntity<?> createCollection(@RequestBody Map<String, Object> collectionData){

        Map<String, Object> response = new HashMap<>();
        String name = "";

        if(collectionData.containsKey("name")){
            name = (String)collectionData.get("name");
            response.put("collection", name);
        }else{
            response.put("error", "The name field is required");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try
        {
            Controller.getInstance().createCollection(name);
            response.put("message", "Collection saved successfully");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
        }
        catch (CollectionAlreadyExistsException e){
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/collections/{collectionName}")
    public ResponseEntity<?> putCollectionData(@PathVariable String collectionName, @RequestBody Map<String, Object> data){
        Map<String, Object> response = new HashMap<>();
        try {
            Controller.getInstance().putCollectionData(collectionName, data);
            response.put("message", "The data has been saved in the collection.");
        } catch (FileNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/collections/{collectionName}")
    public ResponseEntity<?> showCollection(@PathVariable String collectionName){
        Map<String, Object> response = new HashMap<>();
        try {
            return new ResponseEntity<Map<String,Object>>(Controller.getInstance().getCollectionData(collectionName), HttpStatus.OK);
        } catch (FileNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/collections/{collectionName}")
    public ResponseEntity<?> dropCollection(@PathVariable String collectionName){
        Map<String, Object> response = new HashMap<>();
        try{
            Controller.getInstance().dropCollection(collectionName);
            response.put("message", "Collection deleted.");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
        }
        catch (FileNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/collections/{collectionName}/{documentID}")
    public ResponseEntity<?> getDocument(@PathVariable String collectionName, @PathVariable String documentID){
        Map<String, Object> response = new HashMap<>();
        try {
            return new ResponseEntity<Map<String,Object>>(Controller.getInstance().getDocumentByID(collectionName, documentID), HttpStatus.OK);
        } catch (FileNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/collections/{collectionName}/{documentID}")
    public ResponseEntity<?> deleteDocument(@PathVariable String collectionName, @PathVariable String documentID){
        Map<String, Object> response = new HashMap<>();
        try {
            Controller.getInstance().deleteDocument(collectionName, documentID);
            response.put("message", "Document deleted.");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/collections/{collectionName}/{documentID}")
    public ResponseEntity<?> updateDocument(@PathVariable String collectionName, @PathVariable String documentID, @RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            Controller.getInstance().updateDocumentByID(collectionName, data, documentID);
            response.put("message", "The data has been saved in the collection.");
            return new ResponseEntity<Map<String,Object>>(Controller.getInstance().getCollectionData(collectionName), HttpStatus.OK);
        } catch (FileNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }



}