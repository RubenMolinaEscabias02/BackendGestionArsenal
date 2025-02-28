package org.example.controller;

import org.example.Dynamo.DynamoDBConfig;
import org.example.Dynamo.UsuarioRepository;
import org.example.model.Usuario;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository = new UsuarioRepository(
            new DynamoDBConfig().dynamoDbClient()
    );

    @PostMapping("/register")
    public Usuario registro(@RequestBody Usuario usuario){
        Usuario u = usuarioRepository.registro(usuario);
        System.out.println(u.toString());
        return u;
    }

    @GetMapping("/login")
    public Usuario login(@RequestParam String usuario, @RequestParam String password){
        return usuarioRepository.login(usuario, password);
    }

    @GetMapping("/all")
    public List<Usuario> leeUsuarios(){
        return usuarioRepository.leeUsuarios();
    }

    @GetMapping("/id/{id}")
    public Usuario leeUsuarioId(@PathVariable int id){
        return usuarioRepository.leeUsuarioId(id);
    }

    @PutMapping("/modify")
    public boolean modificaUsuario(@RequestBody Usuario usuario){
        return usuarioRepository.actualizaUsuario(usuario);
    }

    @PostMapping("/delete/{id}")
    public boolean eliminaUsuario(@PathVariable int id){
        return usuarioRepository.borraUsuario(id);
    }
}
