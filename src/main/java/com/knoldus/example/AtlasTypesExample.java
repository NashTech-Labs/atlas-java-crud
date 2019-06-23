package com.knoldus.example;

import com.google.common.base.Preconditions;
import org.apache.atlas.AtlasClient;
import org.apache.atlas.AtlasServiceException;
import org.apache.atlas.model.typedef.AtlasBaseTypeDef;
import org.apache.atlas.type.AtlasType;
import org.apache.atlas.v1.model.instance.Referenceable;
import org.apache.atlas.v1.model.typedef.AttributeDefinition;
import org.apache.atlas.v1.model.typedef.ClassTypeDefinition;
import org.apache.atlas.v1.model.typedef.EnumTypeDefinition;
import org.apache.atlas.v1.model.typedef.Multiplicity;
import org.apache.atlas.v1.model.typedef.StructTypeDefinition;
import org.apache.atlas.v1.model.typedef.TraitTypeDefinition;
import org.apache.atlas.v1.model.typedef.TypesDef;
import org.apache.atlas.v1.typesystem.types.utils.TypesUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AtlasTypesExample {
    
    static final String DATABASE_TYPE = "DB_v11";
    
    private static final String[] TYPES =
            {DATABASE_TYPE, "JdbcAccess_v11"};
    
    private final AtlasClient atlasClient;
    
    public AtlasTypesExample() {
        atlasClient = new AtlasClient(new String[]{"http://localhost:21000"}, new String[]{"admin", "admin"});
    }
    
    public static void main(String[] args) throws Exception {
        AtlasTypesExample atlasTypesDemo = new AtlasTypesExample();
        atlasTypesDemo.run();
    }
    
    private void run() throws Exception {
        listTypes();
        createTypes();
        verifyTypesCreated();
        createEntity();
        
    }
    
    private void verifyTypesCreated() throws AtlasServiceException {
        List<String> types = atlasClient.listTypes();
        for (String type : TYPES) {
            assert types.contains(type);
        }
    }
    
    private void listTypes() throws AtlasServiceException {
        System.out.println("Types registered with Atlas:");
        List<String> types = atlasClient.listTypes();
        for (String type : types) {
            System.out.println("Type: " + type);
        }
    }
    
    private void createTypes() throws Exception {
        TypesDef typesDef = createTypeDefinitions();
        
        String typesAsJSON = AtlasType.toV1Json(typesDef);
        System.out.println("typesAsJSON = " + typesAsJSON);
        atlasClient.createType(typesAsJSON);
        
        // verify types created
        listTypes();
    }
    
    TypesDef createTypeDefinitions() throws Exception {
        ClassTypeDefinition dbClsDef = TypesUtil
                .createClassTypeDef(DATABASE_TYPE, DATABASE_TYPE, null,
                        TypesUtil.createUniqueRequiredAttrDef("name", AtlasBaseTypeDef.ATLAS_TYPE_STRING),
                        attrDef("description", AtlasBaseTypeDef.ATLAS_TYPE_STRING), attrDef("locationUri", AtlasBaseTypeDef.ATLAS_TYPE_STRING),
                        attrDef("owner", AtlasBaseTypeDef.ATLAS_TYPE_STRING), attrDef("createTime", AtlasBaseTypeDef.ATLAS_TYPE_LONG));
        
        
        
        TraitTypeDefinition jdbcTraitDef = TypesUtil.createTraitTypeDef("JdbcAccess_v11", "JdbcAccess Trait", null);
        
        return new TypesDef(Collections.<EnumTypeDefinition>emptyList(), Collections.<StructTypeDefinition>emptyList(),
                Arrays.asList(jdbcTraitDef),
                Arrays.asList(dbClsDef));
    }
    
    AttributeDefinition attrDef(String name, String dT) {
        return attrDef(name, dT, Multiplicity.OPTIONAL, false, null);
    }
    
    AttributeDefinition attrDef(String name, String dT, Multiplicity m, boolean isComposite,
                                String reverseAttributeName) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(dT);
        return new AttributeDefinition(name, dT, m, isComposite, reverseAttributeName);
    }
    
    private String createEntity()
            throws AtlasServiceException {
        
        Referenceable referenceable = new Referenceable("DB_v11");
        referenceable.set("name", "testt-schema-v2");
        referenceable.set("description", "desc");
        referenceable.set("owner", "himani");
        referenceable.set("locationUri", "test-schema-v2");
        
        String entityJSON = AtlasType.toV1Json(referenceable);
        System.out.println("Submitting new entity= " + entityJSON);
        List<String> entitiesCreated = atlasClient.createEntity(entityJSON);
        
        System.out.println("created instance for type " + "avro" + ", guid: " + entitiesCreated);
        for (String entity : entitiesCreated) {
            System.out.println("Entity created: " + entity);
        }
        return entitiesCreated.get(entitiesCreated.size() - 1);
    }
}

