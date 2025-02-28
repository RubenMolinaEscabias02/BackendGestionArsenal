package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Accion implements Comparable<Accion> {
    private int id;
    private LocalDateTime fechaRealizacion;
    private int idArma;
    private int idUsuario;
    private String tipoDeAccion;

    public LocalDateTime getFechaRealizacion() {
        return fechaRealizacion;
    }

    public String getTipoDeAccion() {
        return tipoDeAccion;
    }

    public int getId() {
        return id;
    }

    public Accion(int id, LocalDateTime fechaRealizacion, int idArma, int idUsuario, String tipoDeAccion) {
        this.id = id;
        this.fechaRealizacion = fechaRealizacion;
        this.idArma = idArma;
        this.idUsuario = idUsuario;
        this.tipoDeAccion = tipoDeAccion;
    }

    public int getIdArma() {
        return idArma;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFechaRealizacion(LocalDateTime fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdArma(int idArma) {
        this.idArma = idArma;
    }

    public void setTipoDeAccion(String tipoDeAccion) {
        this.tipoDeAccion = tipoDeAccion;
    }

    @Override
    public int compareTo(Accion a) {
        if (a.getFechaRealizacion().isEqual(fechaRealizacion)) return 0;
        return a.getFechaRealizacion().isBefore(fechaRealizacion) ? -1 : 1;
    }
}
