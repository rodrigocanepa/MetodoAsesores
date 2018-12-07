package projects.solucionescolabora.com.metodoasesores.Modelos;

/**
 * Created by rodrigocanepacruz on 30/10/18.
 */

public class ExperienciaAcademica {

    private String grado;
    private String materia;
    private String escuela;
    private String generacion;

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getEscuela() {
        return escuela;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }

    public String getGeneracion() {
        return generacion;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }

    public ExperienciaAcademica(String grado, String materia, String escuela, String generacion) {
        this.grado = grado;
        this.materia = materia;
        this.escuela = escuela;
        this.generacion = generacion;
    }

    public ExperienciaAcademica() {
    }
}
