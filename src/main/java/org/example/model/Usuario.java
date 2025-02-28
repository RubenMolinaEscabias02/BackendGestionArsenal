package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Usuario {
    private int id;
    private String nombre;
    private String password;
    private boolean admin;

    public Usuario(int id, String nombre, String password, boolean admin) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

}
