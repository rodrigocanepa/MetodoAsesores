package projects.solucionescolabora.com.metodoasesores.Modelos;

/**
 * Created by rodrigocanepacruz on 08/11/18.
 */

public class Temas {

    private String tema;
    private String subtemas;
    private String duracion;
    private String materiales;

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getSubtemas() {
        return subtemas;
    }

    public void setSubtemas(String subtemas) {
        this.subtemas = subtemas;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getMateriales() {
        return materiales;
    }

    public void setMateriales(String materiales) {
        this.materiales = materiales;
    }

    public Temas() {
    }

    public Temas(String tema, String subtemas, String duracion, String materiales) {
        this.tema = tema;
        this.subtemas = subtemas;
        this.duracion = duracion;
        this.materiales = materiales;
    }
}
