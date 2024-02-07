package ejercicio.agentes;

import java.util.ArrayList;
import java.util.List;

public class RecomendadorEjercicios {
    private List<Ejercicio> listaEjercicios;

    public RecomendadorEjercicios(List<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

    public List<Ejercicio> recomendarEjercicios(String tipoDeseado, String dificultadDeseada) {
        List<Ejercicio> recomendaciones = new ArrayList<>();

        for (Ejercicio ejercicio : listaEjercicios) {
            // Filtra ejercicios por tipo y dificultad
            if (ejercicio.getTipo().equalsIgnoreCase(tipoDeseado) && ejercicio.getDificultad().equalsIgnoreCase(dificultadDeseada)) {
                recomendaciones.add(ejercicio);
            }
        }

        return recomendaciones;
    }

    public List<Ejercicio> getListaEjercicios() {
        return listaEjercicios;
    }

    public void setListaEjercicios(List<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }
    
    
}
