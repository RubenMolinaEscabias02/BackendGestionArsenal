package org.example.Dynamo;

import org.example.model.Accion;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class AccionMapper {

    public static Accion fromItem(Map<String, AttributeValue> item) {
        return new Accion(
                Integer.parseInt(item.get("id").n()),
                LocalDateTime.parse(item.get("fechaRealizacion").s()),
                Integer.parseInt(item.get("idArma").n()),
                Integer.parseInt(item.get("idUsuario").n()),
                item.get("tipoAccion").s()
        );
    }

}
