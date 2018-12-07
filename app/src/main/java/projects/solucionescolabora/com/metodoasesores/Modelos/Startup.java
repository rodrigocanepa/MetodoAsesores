package projects.solucionescolabora.com.metodoasesores.Modelos;

import java.util.List;

/**
 * Created by rodrigocanepacruz on 30/10/18.
 */

public class Startup {

    private String id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String urlFoto;
    private String ubicacion;
    private List<String> intereses;
    private String nombreStartup;
    private String fechaInicio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<String> getIntereses() {
        return intereses;
    }

    public void setIntereses(List<String> intereses) {
        this.intereses = intereses;
    }

    public String getNombreStartup() {
        return nombreStartup;
    }

    public void setNombreStartup(String nombreStartup) {
        this.nombreStartup = nombreStartup;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Startup(String id, String nombres, String apellidos, String correo, String telefono, String urlFoto, String ubicacion, List<String> intereses, String nombreStartup, String fechaInicio) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.urlFoto = urlFoto;
        this.ubicacion = ubicacion;
        this.intereses = intereses;
        this.nombreStartup = nombreStartup;
        this.fechaInicio = fechaInicio;
    }

    public Startup() {
    }
}
