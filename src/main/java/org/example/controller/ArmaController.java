package org.example.controller;

import org.example.Dynamo.AccionRepository;
import org.example.Dynamo.ArmaRepository;
import org.example.Dynamo.DynamoDBConfig;
import org.example.model.Accion;
import org.example.model.Arma;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/arma")
public class ArmaController {
    private final ArmaRepository armaRepository = new ArmaRepository(new DynamoDBConfig().dynamoDbClient());
    private final AccionRepository accionRepository = new AccionRepository(new DynamoDBConfig().dynamoDbClient());

    @PostMapping("/create-table")
    public boolean crearTabla(){
        return armaRepository.creaTabla();
    }

    @PostMapping("/add")
    public boolean aniadeArma(@RequestBody Arma arma, @RequestParam int idUsuario){
        try {
            boolean b = armaRepository.guardaArma(arma);
            if (b)accionRepository.guardaAccion(new Accion(accionRepository.generaId(), LocalDateTime.now(), arma.getNumSerie(), idUsuario, "Insercion"));
            return b;
        }catch (Exception e){
            return false;
        }
    }

    @GetMapping("/all")
    public List<Arma> leeArmas(){
        return armaRepository.leeArmas();
    }

    @GetMapping("/old")
    public List<Arma> leeArmasAnioMenorMayor(){
        return armaRepository.leeArmasAnioMenorMayor();
    }

    @GetMapping("/new")
    public List<Arma> leeArmasAnioMayorMenor(){
        return armaRepository.leeArmasAnioMayorMenor();
    }

    @GetMapping("/id/{id}")
    public Arma leeArmaId(@PathVariable int id){
        return armaRepository.leeArmaId(id);
    }
    @GetMapping("/id/")
    public List<Arma> leeArmasId2(){
        return List.of();
    }

    @GetMapping("/obsolete/{obsoleto}")
    public List<Arma> leeArmasObsoleto(@PathVariable boolean obsoleto){
        return armaRepository.leeArmasObsoleto(obsoleto);
    }
    @GetMapping("/obsolete/")
    public List<Arma> leeArmasObsoleto2(){
        return List.of();
    }

    @GetMapping("/type/{tipo}")
    public List<Arma> leeArmasTipo(@PathVariable String tipo){
        return armaRepository.leeArmasTipo(tipo);
    }
    @GetMapping("/type/")
    public List<Arma> leeArmasTipo2(){
        return List.of();
    }

    @GetMapping("/name/{nombre}")
    public List<Arma> leeArmasNombre(@PathVariable String nombre){
        return armaRepository.leeArmasNombre(nombre);
    }

    @GetMapping("/name/")
    public List<Arma> leeArmasNombre2(){
        return List.of();
    }

    @GetMapping("/caliber/{calibre}")
    public List<Arma> leeArmasCalibre(@PathVariable String calibre){
        return armaRepository.leeArmasCalibre(calibre);
    }
    @GetMapping("/caliber/")
    public List<Arma> leeArmasCalibre2(){
        return List.of();
    }

    @PutMapping("/modify")
    public boolean modificaArma(@RequestBody Arma arma, @RequestParam int idUsuario){
        boolean b = armaRepository.actualizaArma(arma);
        if (b) accionRepository.guardaAccion(new Accion(accionRepository.generaId(), LocalDateTime.now(), arma.getNumSerie(), idUsuario, "Modificacion"));
        return b;
    }

    @PostMapping("/delete/{id}")
    public boolean eliminaArma(@PathVariable int id, @RequestParam int idUsuario){
        boolean b = armaRepository.borraArma(id);
        if (b) accionRepository.guardaAccion(new Accion(accionRepository.generaId(), LocalDateTime.now(), id, idUsuario, "Borrado"));
        return b;
    }
    @PostMapping("/delete/")
    public boolean eliminaArma2(@RequestParam Object idUsuario){
        return false;
    }
}
