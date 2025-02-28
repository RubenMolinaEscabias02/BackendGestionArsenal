package org.example.Dynamo;

import org.example.model.Accion;
import org.example.model.Usuario;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.util.Map;

public class UsuarioMapper {
    public static Usuario fromItem(Map<String, AttributeValue> item) {
        return new Usuario(
                Integer.parseInt(item.get("id").n()),
                item.get("nombre").s(),
                item.get("password").s(),
                item.get("admin").bool()
        );
    }
}
