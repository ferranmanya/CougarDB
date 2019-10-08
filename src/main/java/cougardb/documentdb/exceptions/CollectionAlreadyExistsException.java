package cougardb.documentdb.exceptions;

public class CollectionAlreadyExistsException extends Exception{
    public CollectionAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
} 
