package org.example.Dynamo;

import org.example.model.Arma;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ArmaRepository {
    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "Arma";

    public ArmaRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public int verificarTablaExistente() {
        try {
            DescribeTableRequest describeTableRequest = DescribeTableRequest.builder()
                    .tableName(TABLE_NAME)
                    .build();

            DescribeTableResponse describeTableResponse = dynamoDbClient.describeTable(describeTableRequest);
            return 0;
        } catch (DynamoDbException e) {
            if ("ResourceNotFoundException".equals(e.awsErrorDetails().errorCode())) {
                return 1;
            }
        } catch (SdkException e) {
            return -1;
        }
        return -1;
    }

    public boolean creaTabla() {
        try {
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(TABLE_NAME)
                    .keySchema(
                            KeySchemaElement.builder().attributeName("numSerie").keyType(KeyType.HASH).build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder().attributeName("numSerie").attributeType(ScalarAttributeType.N).build()
                    )
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build();

            CreateTableResponse response = dynamoDbClient.createTable(request);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean guardaArma(Arma arma) {
        if (verificarTablaExistente() == 1) creaTabla();
        try {
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("numSerie", AttributeValue.builder().n(String.valueOf(arma.getNumSerie())).build());
            item.put("nombre", AttributeValue.builder().s(arma.getNombre()).build());
            item.put("tipo", AttributeValue.builder().s(arma.getTipo()).build());
            item.put("calibre", AttributeValue.builder().s(arma.getCalibre()).build());
            item.put("modosDeTiro", AttributeValue.builder().ss(arma.getModosDeTiro()).build());
            item.put("anioSalida", AttributeValue.builder().n(String.valueOf(arma.getAnioSalida())).build());
            item.put("distanciaEfectiva", AttributeValue.builder().n(String.valueOf(arma.getDistanciaEfectiva())).build());
            item.put("obsoleto", AttributeValue.builder().bool(arma.isObsoleto()).build());
            item.put("urlFoto", AttributeValue.builder().s(arma.getUrlFoto()).build());

            PutItemRequest request = PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(item)
                    .build();

            dynamoDbClient.putItem(request);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public List<Arma> leeArmas() {
        if (verificarTablaExistente() == 1) creaTabla();
        ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME).build();
        return dynamoDbClient.scan(request).items().stream()
                .map(ArmaMapper::fromItem)
                .collect(Collectors.toList());
    }
    public List<Arma> leeArmasCalibre(String calibre) {
        if (verificarTablaExistente() == 1) creaTabla();
        Map<String, AttributeValue> expressionValues = new HashMap<>();

        expressionValues.put(":calibreValor", AttributeValue.builder().s(calibre).build());

        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("calibre = :calibreValor")
                .expressionAttributeValues(expressionValues)
                .build();
        return dynamoDbClient.scan(request).items().stream()
                .map(ArmaMapper::fromItem)
                .collect(Collectors.toList());
    }

    public List<Arma> leeArmasNombre(String nombre) {
        if (verificarTablaExistente() == 1) creaTabla();
        Map<String, AttributeValue> expressionValues = new HashMap<>();

        expressionValues.put(":nombreValor", AttributeValue.builder().s(nombre).build());

        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("contains(nombre, :nombreValor)")
                .expressionAttributeValues(expressionValues)
                .build();
        return dynamoDbClient.scan(request).items().stream()
                .map(ArmaMapper::fromItem)
                .collect(Collectors.toList());
    }

    public List<Arma> leeArmasAnioMayorMenor() {
        if (verificarTablaExistente() == 1) creaTabla();
        ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME).build();
        List<Arma> armas = dynamoDbClient.scan(request).items().stream()
                .map(ArmaMapper::fromItem)
                .collect(Collectors.toList());
        Collections.sort(armas);
        return armas;
    }
    public List<Arma> leeArmasAnioMenorMayor() {
        if (verificarTablaExistente() == 1) creaTabla();
        ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME).build();
        List<Arma> armas = dynamoDbClient.scan(request).items().stream()
                .map(ArmaMapper::fromItem)
                .collect(Collectors.toList());
        Collections.sort(armas);
        Collections.reverse(armas);
        return armas;
    }

    public List<Arma> leeArmasTipo(String tipo) {
        if (verificarTablaExistente() == 1) creaTabla();
        Map<String, AttributeValue> expressionValues = new HashMap<>();

        expressionValues.put(":tipoValor", AttributeValue.builder().s(tipo).build());

        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("tipo = :tipoValor")
                .expressionAttributeValues(expressionValues)
                .build();
        return dynamoDbClient.scan(request).items().stream()
                .map(ArmaMapper::fromItem)
                .collect(Collectors.toList());
    }

    public List<Arma> leeArmasObsoleto(boolean obsoleto) {
        if (verificarTablaExistente() == 1) creaTabla();
        Map<String, AttributeValue> expressionValues = new HashMap<>();

        expressionValues.put(":obsoletoValor", AttributeValue.builder().bool(obsoleto).build());

        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("obsoleto = :obsoletoValor")
                .expressionAttributeValues(expressionValues)
                .build();
        return dynamoDbClient.scan(request).items().stream()
                .map(ArmaMapper::fromItem)
                .collect(Collectors.toList());
    }

    public Arma leeArmaId(int id){
        if (verificarTablaExistente() == 1) creaTabla();
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":numSerieValor", AttributeValue.builder().n(String.valueOf(id)).build());

        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("numSerie = :numSerieValor")
                .expressionAttributeValues(expressionValues)
                .build();

        List<Map<String, AttributeValue>> items = dynamoDbClient.scan(request).items();

        return items.isEmpty() ? null : ArmaMapper.fromItem(items.get(0));
    }
    public boolean borraArma(int numSerie) {
        if (verificarTablaExistente() == 1) creaTabla();
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("numSerie", AttributeValue.builder().n(String.valueOf(numSerie)).build());

            DeleteItemRequest request = DeleteItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(key)
                    .build();

            dynamoDbClient.deleteItem(request);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean actualizaArma(Arma arma) {
        if (verificarTablaExistente() == 1) creaTabla();
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("numSerie", AttributeValue.builder().n(String.valueOf(arma.getNumSerie())).build());

            Map<String, AttributeValueUpdate> updates = new HashMap<>();
            updates.put("nombre", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(arma.getNombre()).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("tipo", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(arma.getTipo()).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("calibre", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(arma.getCalibre()).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("anioSalida", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().n(String.valueOf(arma.getAnioSalida())).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("distanciaEfectiva", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().n(String.valueOf(arma.getDistanciaEfectiva())).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("obsoleto", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().bool(arma.isObsoleto()).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("urlFoto", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(arma.getUrlFoto()).build())
                    .action(AttributeAction.PUT)
                    .build());

            UpdateItemRequest request = UpdateItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(key)
                    .attributeUpdates(updates)
                    .build();

            dynamoDbClient.updateItem(request);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
