package org.example.Dynamo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.model.Arma;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ArmaMapper {

    public static Arma fromItem(Map<String, AttributeValue> item) {
        return new Arma(
                Integer.parseInt(item.get("numSerie").n()),
                item.get("nombre").s(),
                item.get("tipo").s(),
                item.get("calibre").s(),
                new ArrayList<>(item.get("modosDeTiro").ss()),
                Integer.parseInt(item.get("anioSalida").n()),
                Integer.parseInt(item.get("distanciaEfectiva").n()),
                item.get("obsoleto").bool(),
                item.get("urlFoto").s()
        );
    }
}
