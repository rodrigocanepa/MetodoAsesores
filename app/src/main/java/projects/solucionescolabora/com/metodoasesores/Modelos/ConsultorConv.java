package projects.solucionescolabora.com.metodoasesores.Modelos;

/**
 * Created by rodrigocanepacruz on 07/11/18.
 */

public class ConsultorConv {

    private String id;
    private String nombreCompleto;
    private String especialidad;
    private double calificacion;
    private String comentarios;
    private String correo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public ConsultorConv() {
    }

    public ConsultorConv(String id, String nombreCompleto, String especialidad, double calificacion, String comentarios, String correo) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.especialidad = especialidad;
        this.calificacion = calificacion;
        this.comentarios = comentarios;
        this.correo = correo;
    }
}
