package projects.solucionescolabora.com.metodoasesores.Modelos;

/**
 * Created by rodrigocanepacruz on 30/10/18.
 */

public class Reconocimientos {

    private String anio;
    private String otorgadoPor;
    private String descripcion;

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getOtorgadoPor() {
        return otorgadoPor;
    }

    public void setOtorgadoPor(String otorgadoPor) {
        this.otorgadoPor = otorgadoPor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Reconocimientos() {
    }

    public Reconocimientos(String anio, String otorgadoPor, String descripcion) {
        this.anio = anio;
        this.otorgadoPor = otorgadoPor;
        this.descripcion = descripcion;
    }
}
