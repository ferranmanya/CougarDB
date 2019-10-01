package cougardb.documentdb;

public class CollectionAlreadyExistsException extends Exception{
    public CollectionAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
} 
