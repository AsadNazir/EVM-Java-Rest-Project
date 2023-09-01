package com.example.evm_2.commons;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbOperations {
    public final AmazonDynamoDB Db;

    public DbOperations() {
        Db = DynamoDb.getInstance();
    }

    public boolean tableExists(String tableName) {
        Table table = new DynamoDB(Db).getTable(tableName);

        try {
            table.describe();
        } catch (ResourceNotFoundException e) {
            return false;
        }

        return true;
    }


    public boolean deleteItem(String primaryKey, String primaryKeyVal, String tableName) {
        DynamoDB dynamoDB = new DynamoDB(Db);
        Table table = dynamoDB.getTable(tableName);

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(primaryKey, primaryKeyVal); // Assuming the primary key is named "primaryKey"
        try {
            DeleteItemOutcome deleteItem = table.deleteItem(deleteItemSpec);
            System.out.println(deleteItem.toString());
            return true; // Item deleted successfully
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Item deletion failed
        }
    }

    public boolean deleteItem(String primaryKey, String primaryKeyVal, String tableName, String rangeKey, String rangeKeyVal) {
        DynamoDB dynamoDB = new DynamoDB(Db);
        Table table = dynamoDB.getTable(tableName);

        PrimaryKey primaryKeyToDelete = new PrimaryKey(primaryKey, primaryKeyVal, rangeKey, rangeKeyVal);

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(primaryKeyToDelete);

        try {
            table.deleteItem(deleteItemSpec);
            System.out.println("Item deleted successfully.");
            return true; // Item deleted successfully
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Item deletion failed: " + e.getMessage());
            return false; // Item deletion failed
        }
    }

    public boolean deleteTable(String TableName) {
        DeleteTableRequest request = new DeleteTableRequest()
                .withTableName(TableName);

        Table table = new DynamoDB(Db).getTable(TableName);
        table.delete();
        try {
            table.waitForDelete();
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean putData(String tableName, List<Item> itemList) {
        Table table = new DynamoDB(Db).getTable(tableName);

        for (Item item : itemList) {
            table.putItem(item);
        }

        return true;
    }

    public boolean deleteAllEntries(String TableName, String primarKey) {
        Table table = new DynamoDB(Db).getTable(TableName);

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(TableName);

        ScanResult result = Db.scan(scanRequest);

        result.getItems().forEach(item -> {
            table.deleteItem(primarKey, item.get(primarKey));
        });

        return true;
    }

    //Write the code to update the values of dynamo db item

    public void updateTableItem(
            String tableName,
            String key,
            String keyVal,
            String name,
            String updateVal) {

        DynamoDB dynamoDB = new DynamoDB(Db);
        Table table = dynamoDB.getTable(tableName);

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey(key, keyVal)
                .withUpdateExpression("set #attrName = :attrValue")
                .withNameMap(new NameMap().with("#attrName", name))
                .withValueMap(new ValueMap().with(":attrValue", updateVal));

        try {
            table.updateItem(updateItemSpec);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public Item getItemFromTable(String primaryKey, String tableName, String primaryKeyName) {
        DynamoDB dynamoDB = new DynamoDB(Db);
        Table table = dynamoDB.getTable(tableName);
        return table.getItem(primaryKeyName, primaryKey);
    }

    public List<Item> getAllEntries(String tableName) {
        List<Item> itemList = new ArrayList<>();

        Map<String, AttributeValue> lastKeyEvaluated = null;
        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(tableName)
                    .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = Db.scan(scanRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {
                Item newItem = new Item(); // Create a new Item instance

                // Populate the new item with attribute values from the retrieved item map
                for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
                    String attributeName = entry.getKey();
                    AttributeValue attributeValue = entry.getValue();

                    // Handle different types of attribute values, e.g., strings, numbers, etc.
                    if (attributeValue.getS() != null) {
                        newItem.withString(attributeName, attributeValue.getS());
                    } else if (attributeValue.getN() != null) {
                        newItem.withNumber(attributeName, new BigDecimal(attributeValue.getN()));
                    }
                    // Add more cases as needed for other attribute types
                }

                itemList.add(newItem); // Add the populated item to the list
            }

            lastKeyEvaluated = result.getLastEvaluatedKey();
        } while (lastKeyEvaluated != null);

        return itemList;
    }


    public boolean CreateTable(List<KeySchemaElement> keySchemaElements, List<AttributeDefinition> attributeDefinitions, String TableName) {
        CreateTableRequest request = new CreateTableRequest()
                .withTableName(TableName)
                .withKeySchema(keySchemaElements)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(10L));

        Table table = new DynamoDB(Db).createTable(request);
        try {
            table.waitForActive();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return table.getDescription().getTableStatus().equals("ACTIVE");
    }
}
