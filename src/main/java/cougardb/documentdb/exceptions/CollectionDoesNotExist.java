package cougardb.documentdb.exceptions;

public class CollectionDoesNotExist extends Exception{
    public CollectionDoesNotExist(String errorMessage){
        super(errorMessage);
    }
}