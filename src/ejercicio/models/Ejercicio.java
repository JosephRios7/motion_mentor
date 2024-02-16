
package ejercicio.models;

/**
 *
 * @author leo23
 */
public class Ejercicio {

    private int id;
    private String nombre;
    private String tipo;
    private String duracion;
    private String repeticiones;
    private String series;
    private String dificultad;
    private String descripcion;
    private String instrucciones;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    @Override
    public String toString() {
        return "Ejercicio{"
                + "id=" + id
                + ", nombre='" + nombre + '\''
                + ", tipo='" + tipo + '\''
                + ", duracion='" + duracion + '\''
                + ", repeticiones='" + repeticiones + '\''
                + ", series='" + series + '\''
                + ", dificultad='" + dificultad + '\''
                 + ", descripcion='" + descripcion + '\''
                 + ", instrucciones='" + instrucciones + '\''
                + '}';
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(String repeticiones) {
        this.repeticiones = repeticiones;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Ejercicio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

}
