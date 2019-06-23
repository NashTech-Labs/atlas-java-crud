package com.knoldus.example;

import org.apache.atlas.AtlasClient;
import org.apache.atlas.AtlasServiceException;
import org.apache.atlas.type.AtlasType;
import org.apache.atlas.v1.model.instance.Referenceable;

import java.util.List;

public class AtlasEntityExample {
    
    private final AtlasClient atlasClient;
    
    AtlasEntityExample() {
        atlasClient = new AtlasClient(new String[]{"http://localhost:21000"}, new String[]{"admin", "admin"});
    }
    
    public static void main(String[] args) throws AtlasServiceException {
        AtlasEntityExample atlasTypesDemo = new AtlasEntityExample();
        atlasTypesDemo.run();
    }
    
    private void run() throws AtlasServiceException {
        listTypes();
        
        String localTableId = createAvroEntity();
        retrieveEntity(localTableId);
        System.out.println("Deleting entity with guid: "+localTableId);
        deleteEntity(localTableId);
    }
    
    private void listTypes() throws AtlasServiceException {
        System.out.println("Types registered with Atlas:");
        List<String> types = atlasClient.listTypes();
        for (String type : types) {
            System.out.println("Type: " + type);
        }
    }
    
    
    private void retrieveEntity(String guid) throws AtlasServiceException {
        System.out.println("Retrieving entity with GUID: " + guid);
        Referenceable entity = atlasClient.getEntity(guid);
        String entityJson = AtlasType.toV1Json(entity);
        System.out.println(entityJson);
    }
    
    private String createAvroEntity()
            throws AtlasServiceException {
        
        Referenceable referenceable = new Referenceable("avro_schema");
        referenceable.set("name", "testt-schema-v1");
        referenceable.set("description", "desc");
        referenceable.set("owner", "himani");
        referenceable.set("locationUri", "test-schema-v1");
        referenceable.set("type", "avro");
        referenceable.set("namespace", "com.knoldus");
        referenceable.set("createTime", System.currentTimeMillis());
        referenceable.set("qualifiedName", "test-avro-schema-v1");
        referenceable.set("avro_notation", "{ \"type\": \"record\", \"name\": \"testKey\", \"namespace\": \"key.test\", \"fields\": [{ \"name\": \"name\", \"type\": { \"type\": \"string\", \"logicalType\": \"CHARACTER\", \"dbColumnName\": \"name\", \"length\": 2 }, \"default\": \"\" }] }");
    
        String entityJSON = AtlasType.toV1Json(referenceable);
        System.out.println("Submitting new entity= " + entityJSON);
        List<String> entitiesCreated = atlasClient.createEntity(entityJSON);
        
        System.out.println("created instance for type " + "avro" + ", guid: " + entitiesCreated);
        for (String entity : entitiesCreated) {
            System.out.println("Entity created: " + entity);
        }
        return entitiesCreated.get(entitiesCreated.size() - 1);
    }
    
    private List<String> deleteEntity(final String... guids) throws AtlasServiceException {
        return atlasClient.deleteEntities(guids).getDeletedEntities();
    }
}
