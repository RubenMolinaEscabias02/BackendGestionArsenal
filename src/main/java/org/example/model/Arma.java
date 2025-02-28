package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arma implements Comparable<Arma> {
    private int numSerie;
    private String nombre;
    private String tipo;
    private String calibre;
    private ArrayList<String> modosDeTiro;
    private int anioSalida;
    private int distanciaEfectiva;
    private boolean obsoleto;
    private String urlFoto;

    public int getNumSerie() {
        return numSerie;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCalibre() {
        return calibre;
    }

    public ArrayList<String> getModosDeTiro() {
        return modosDeTiro;
    }

    public int getAnioSalida() {
        return anioSalida;
    }

    public int getDistanciaEfectiva() {
        return distanciaEfectiva;
    }

    public boolean isObsoleto() {
        return obsoleto;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public void setModosDeTiro(ArrayList<String> modosDeTiro) {
        this.modosDeTiro = modosDeTiro;
    }

    public void setAnioSalida(int anioSalida) {
        this.anioSalida = anioSalida;
    }

    public void setDistanciaEfectiva(int distanciaEfectiva) {
        this.distanciaEfectiva = distanciaEfectiva;
    }

    public void setObsoleto(boolean obsoleto) {
        this.obsoleto = obsoleto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Override
    public int compareTo(Arma o) {
        if (anioSalida == o.getAnioSalida()) return 0;
        return anioSalida < o.getAnioSalida() ? 1 : -1;
    }
}
