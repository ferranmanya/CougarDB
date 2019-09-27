package cougardb.documentdb;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        response.put("message", "Collection saved successfully");
        response.put("collection", collectionData);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
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
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
    }
}