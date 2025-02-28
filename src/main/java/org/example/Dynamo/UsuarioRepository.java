package org.example.Dynamo;

import org.example.model.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsuarioRepository {
    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "Usuario";
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioRepository(
            DynamoDbClient dynamoDbClient
    )
    {
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

    public String cifrar(String password) {
        return encoder.encode(password);
    }

    public boolean verificar(String password, String hash) {
        return encoder.matches(password, hash);
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

    public Usuario registro(Usuario usuario) {
        if (verificarTablaExistente() == 1) creaTabla();
        try {
            usuario.setPassword(cifrar(usuario.getPassword()));
            usuario.setId(generaId());
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("id", AttributeValue.builder().n(String.valueOf(usuario.getId())).build());
            item.put("nombre", AttributeValue.builder().s(usuario.getNombre()).build());
            item.put("password", AttributeValue.builder().s(usuario.getPassword()).build());
            item.put("admin", AttributeValue.builder().bool(usuario.isAdmin()).build());

            PutItemRequest request = PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(item)
                    .build();

            dynamoDbClient.putItem(request);
            return usuario;
        }catch (Exception e) {
            return null;
        }
    }

    public Usuario login(String usuario, String password) {
        if (verificarTablaExistente() == 1) creaTabla();
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":nombreValor", AttributeValue.builder().s(usuario).build());

        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("nombre = :nombreValor")
                .expressionAttributeValues(expressionValues)
                .build();

        List<Map<String, AttributeValue>> items = dynamoDbClient.scan(request).items();

        Usuario u = items.isEmpty() ? null : UsuarioMapper.fromItem(items.get(0));
        if (u == null || !verificar(password, u.getPassword())) return null;
        return u;
    }

    public List<Usuario> leeUsuarios() {
        if (verificarTablaExistente() == 1) creaTabla();
        ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME).build();
        return dynamoDbClient.scan(request).items().stream()
                .map(UsuarioMapper::fromItem)
                .collect(Collectors.toList());
    }

    public Usuario leeUsuarioId(int id){
        if (verificarTablaExistente() == 1) creaTabla();
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":idValor", AttributeValue.builder().n(String.valueOf(id)).build());

        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("id = :idValor")
                .expressionAttributeValues(expressionValues)
                .build();

        List<Map<String, AttributeValue>> items = dynamoDbClient.scan(request).items();

        return items.isEmpty() ? null : UsuarioMapper.fromItem(items.get(0));
    }
    public boolean borraUsuario(int id) {
        if (verificarTablaExistente() == 1) creaTabla();
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("id", AttributeValue.builder().n(String.valueOf(id)).build());

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

    public boolean actualizaUsuario(Usuario usuario) {
        if (verificarTablaExistente() == 1) creaTabla();
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("id", AttributeValue.builder().n(String.valueOf(usuario.getId())).build());

            Map<String, AttributeValueUpdate> updates = new HashMap<>();
            updates.put("nombre", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(usuario.getNombre()).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("password", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(usuario.getPassword()).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("admin", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().bool(usuario.isAdmin()).build())
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
    public int generaId(){
        int id;
        do {
            id = (int) (Math.random() * 99999999);
        }while (id < 10000000 || leeUsuarioId(id) != null);
        return id;
    }
}
