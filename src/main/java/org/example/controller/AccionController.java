package org.example.controller;

import org.example.Dynamo.AccionRepository;
import org.example.Dynamo.DynamoDBConfig;
import org.example.model.Accion;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accion")
public class AccionController {
    private final AccionRepository accionRepository = new AccionRepository(new DynamoDBConfig().dynamoDbClient());

    @PostMapping("/create-table")
    public boolean crearTabla(){
        return accionRepository.creaTabla();
    }

    @GetMapping("/new")
    public Map<String, Object> leeAccionesNuevas(@RequestParam String lastEvaluatedKey, @RequestParam int limit){
        lastEvaluatedKey = verificaLEK(lastEvaluatedKey);
        return accionRepository.leeAccionesNuevosPrimero(limit, lastEvaluatedKey);
    }

    @GetMapping("/old")
    public Map<String, Object> leeAccionesViejas(@RequestParam String lastEvaluatedKey, @RequestParam int limit){
        lastEvaluatedKey = verificaLEK(lastEvaluatedKey);
        return accionRepository.leeAccionesAntiguosPrimero(limit, lastEvaluatedKey);
    }

    @GetMapping("/today")
    public Map<String, Object> leeAccionesHoy(@RequestParam String lastEvaluatedKey, @RequestParam int limit){
        lastEvaluatedKey = verificaLEK(lastEvaluatedKey);
        return accionRepository.leeAccionesHoy(limit, lastEvaluatedKey);
    }

    @GetMapping("/type/{tipo}")
    public Map<String, Object> leeAccionesTipo(@PathVariable String tipo, @RequestParam String lastEvaluatedKey, @RequestParam int limit){
        lastEvaluatedKey = verificaLEK(lastEvaluatedKey);
        return accionRepository.leeAccionTipo(tipo, limit, lastEvaluatedKey);
    }

    @GetMapping("/id/{id}")
    public Accion leeAccionId(@PathVariable int id){
        return accionRepository.leeAccionId(id);
    }

    @PostMapping("/delete/{id}")
    public boolean eliminaAccion(@PathVariable int id){
        return accionRepository.borraAccion(id);
    }

    public String verificaLEK(String lek){
        try {
            Integer.parseInt(lek);
            return lek;
        }catch (NumberFormatException e){
            return null;
        }
    }
}
