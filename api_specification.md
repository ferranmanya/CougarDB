#### Create Collection
```
POST /cougarAPI/collections/ 
Host: localhost:8080
Content-Type: application/json
Accept: application/json

{
    "name": "collection name",
    ...
}
```
#### Get Collection
```
GET /cougarAPI/collections/<collection name>
Host: localhost:8080
Content-Type: application/json
```

#### Drop Collection
```
DELETE /cougarAPI/collections/<collection name>
Host: localhost:8080
Content-Type: application/json
```
#### Insert Document
```
POST /cougarAPI/collections/<collection name> 
Host: localhost:8080
Content-Type: application/json
Accept: application/json

{
    "field": "value",
    ...
}
```

#### Get Document
```
GET /cougarAPI/collections/<collection name>/<document id>/
Host: localhost:8080
Content-Type: application/json
```

#### Search Document
```
POST /cougarAPI/collections/<collection name>/search/
Host: localhost:8080
Content-Type: application/json
Accept: application/json

{
    "query": [
    	{
    		"field": "price",
    		"condition": "<=",
    		"type": "number",
    		"value": "10"
    	},
    	{
    		"field": "category",
    		"condition": "==",
    		"type": "string",
    		"value": "fiction"
    	}
    ]
}
```