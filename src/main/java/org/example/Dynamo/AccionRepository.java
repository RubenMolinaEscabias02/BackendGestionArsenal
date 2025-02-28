package org.example.Dynamo;

import org.example.model.Accion;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AccionRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "Accion";

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
        return 1;
    }

    public AccionRepository(DynamoDbClient dynamoDbClient) {
            this.dynamoDbClient = dynamoDbClient;
        }
        public boolean creaTabla() {
            try {
                CreateTableRequest request = CreateTableRequest.builder()
                        .tableName(TABLE_NAME)
                        .keySchema(
                                KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build()
                        )
                        .attributeDefinitions(
                                AttributeDefinition.builder().attributeName("id").attributeType(ScalarAttributeType.N).build()
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

        public boolean guardaAccion(Accion accion) {
            if (verificarTablaExistente() == 1) {creaTabla();}
            try {
                Map<String, AttributeValue> item = new HashMap<>();
                item.put("id", AttributeValue.builder().n(String.valueOf(accion.getId())).build());
                item.put("fechaRealizacion", AttributeValue.builder().s(accion.getFechaRealizacion().toString()).build());
                item.put("idArma", AttributeValue.builder().n(String.valueOf(accion.getIdArma())).build());
                item.put("idUsuario", AttributeValue.builder().n(String.valueOf(accion.getIdUsuario())).build());
                item.put("tipoAccion", AttributeValue.builder().s(accion.getTipoDeAccion()).build());

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

    public Map<String, Object> leeAccionesNuevosPrimero(int limit, String lastEvaluatedKey) {
        if (verificarTablaExistente() == 1) {
            creaTabla();
        }

        ScanRequest.Builder requestBuilder = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .limit(limit);

        if (lastEvaluatedKey != null) {
            Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("id", AttributeValue.builder().n(lastEvaluatedKey).build());
            requestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        ScanResponse response = dynamoDbClient.scan(requestBuilder.build());

        List<Accion> acciones = response.items().stream()
                .map(AccionMapper::fromItem)
                .collect(Collectors.toList());

        Collections.sort(acciones);
        Map<String, Object> result = new HashMap<>();
        result.put("acciones", acciones);
        result.put("lastEvaluatedKey", response.hasLastEvaluatedKey() ? response.lastEvaluatedKey().get("id").n() : null);

        return result;
    }


    public Map<String, Object> leeAccionesAntiguosPrimero(int limit, String lastEvaluatedKey) {
        if (verificarTablaExistente() == 1) {
            creaTabla();
        }

        ScanRequest.Builder requestBuilder = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .limit(limit);

        if (lastEvaluatedKey != null) {
            Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("id", AttributeValue.builder().n(lastEvaluatedKey).build());
            requestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        ScanResponse response = dynamoDbClient.scan(requestBuilder.build());

        List<Accion> acciones = response.items().stream()
                .map(AccionMapper::fromItem)
                .collect(Collectors.toList());

        Collections.sort(acciones);
        Collections.reverse(acciones);

        Map<String, Object> result = new HashMap<>();
        result.put("acciones", acciones);
        result.put("lastEvaluatedKey", response.hasLastEvaluatedKey() ? response.lastEvaluatedKey().get("id").n() : null);

        return result;
    }

        public Accion leeAccionId(int id){
            if (verificarTablaExistente() == 1) {creaTabla();}
            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":idValor", AttributeValue.builder().n(String.valueOf(id)).build());

            ScanRequest request = ScanRequest.builder()
                    .tableName(TABLE_NAME)
                    .filterExpression("id = :idValor")
                    .expressionAttributeValues(expressionValues)
                    .build();

            List<Map<String, AttributeValue>> items = dynamoDbClient.scan(request).items();

            return items.isEmpty() ? null : AccionMapper.fromItem(items.get(0));
        }

    public Map<String, Object> leeAccionTipo(String tipo, int limit, String lastEvaluatedKey){
        if (verificarTablaExistente() == 1) {
            creaTabla();
        }
        Map<String, AttributeValue> expressionValues = new HashMap<>();

        expressionValues.put(":tipo", AttributeValue.builder().s(tipo).build());

        ScanRequest.Builder requestBuilder = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("tipoAccion = :tipo")
                .expressionAttributeValues(expressionValues)
                .limit(limit);

        if (lastEvaluatedKey != null) {
            Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("id", AttributeValue.builder().n(lastEvaluatedKey).build());
            requestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        ScanResponse response = dynamoDbClient.scan(requestBuilder.build());

        List<Accion> acciones = response.items().stream()
                .map(AccionMapper::fromItem)
                .collect(Collectors.toList());

        Collections.sort(acciones);
        Map<String, Object> result = new HashMap<>();
        result.put("acciones", acciones);
        result.put("lastEvaluatedKey", response.hasLastEvaluatedKey() ? response.lastEvaluatedKey().get("id").n() : null);

        return result;
    }

    public Map<String, Object> leeAccionesHoy(int limit, String lastEvaluatedKey){
        if (verificarTablaExistente() == 1) {
            creaTabla();
        }
        Map<String, AttributeValue> expressionValues = new HashMap<>();

        String hoy = LocalDateTime.now().toString();
        expressionValues.put(":hoy", AttributeValue.builder().s(hoy).build());

        ScanRequest.Builder requestBuilder = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("begins_with(fechaRealizacion, :hoy)")
                .expressionAttributeValues(expressionValues)
                .limit(limit);

        if (lastEvaluatedKey != null) {
            Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("id", AttributeValue.builder().n(lastEvaluatedKey).build());
            requestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        ScanResponse response = dynamoDbClient.scan(requestBuilder.build());

        List<Accion> acciones = response.items().stream()
                .map(AccionMapper::fromItem)
                .collect(Collectors.toList());

        Collections.sort(acciones);
        Map<String, Object> result = new HashMap<>();
        result.put("acciones", acciones);
        result.put("lastEvaluatedKey", response.hasLastEvaluatedKey() ? response.lastEvaluatedKey().get("id").n() : null);

        return result;

    }

        public boolean borraAccion(int id) {
            if (verificarTablaExistente() == 1) {creaTabla();}
            try {
                Map<String, AttributeValue> key = new HashMap<>();
                key.put("id", AttributeValue.builder().n(String.valueOf(id)).build());

                DeleteItemRequest request = DeleteItemRequest.builder()
                        .tableName(TABLE_NAME)
                        .key(key)
                        .build();

                dynamoDbClient.deleteItem(request);
                return true;
            }catch (Exception e) {
                return false;
            }
        }
        public int generaId(){
            int numero;
            do {
                numero = (int) (Math.random() * 999999999);
            }while (leeAccionId(numero) != null || numero < 100000000);
            return numero;
        }

}
