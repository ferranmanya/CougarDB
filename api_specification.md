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