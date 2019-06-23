# APACHE ATLAS CRUD APPLICATION
This Apache Atlas CRUD application helps in building your own Java APIs to interact with Apache Atlas Server and create,
 list or delete new Entities and Types.

 ## HOW TO RUN

 ### Prerequisite: Apache atlas Server must be up and running. The Application assumes the Server is running on its
 default port 21000 and authenticates with username:password, admin:admin

  ## Create new Types in Atlas Server

  To create new Types in Atlas, use the main class com.knoldus.example.AtlasTypesExample

  mvn exec:java -Dexec.mainClass="com.knoldus.example.AtlasTypesExample"

  ## Create new Enities in Atlas Server

  To create new Entites within registered Types in Atlas, use the main class com.knoldus.example.AtlasEnitiyExample

  mvn exec:java -Dexec.mainClass="com.knoldus.example.AtlasEnitiyExample"


