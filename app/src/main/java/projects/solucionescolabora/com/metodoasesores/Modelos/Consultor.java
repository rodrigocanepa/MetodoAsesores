package projects.solucionescolabora.com.metodoasesores.Modelos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigocanepacruz on 30/10/18.
 */

public class Consultor {

    private String id;
    private String nombres;
    private String apellidos;
    private String RFC;
    private String anioMentor;
    private String genero;
    private String edad;
    private List<String> especialidad;
    private String urlFoto;
    private String ubicacion;
    private String frase;
    private List<ExperienciaAcademica> ExperienciaAcademica;
    private List<ExperienciaProfesional> ExperienciaProfesional;
    private List<Reconocimientos> Reconocimientos;
    private Double valoracion;
    private String precio;
    private String correo;

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

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getAnioMentor() {
        return anioMentor;
    }

    public void setAnioMentor(String anioMentor) {
        this.anioMentor = anioMentor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public List<String> getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(List<String> especialidad) {
        this.especialidad = especialidad;
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

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public List<projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica> getExperienciaAcademica() {
        return ExperienciaAcademica;
    }

    public void setExperienciaAcademica(List<projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica> experienciaAcademica) {
        ExperienciaAcademica = experienciaAcademica;
    }

    public List<projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional> getExperienciaProfesional() {
        return ExperienciaProfesional;
    }

    public void setExperienciaProfesional(List<projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional> experienciaProfesional) {
        ExperienciaProfesional = experienciaProfesional;
    }

    public List<Reconocimientos> getReconocimientos() {
        return Reconocimientos;
    }

    public void setReconocimientos(List<Reconocimientos> reconocimientos) {
        this.Reconocimientos = reconocimientos;
    }

    public Double getValoracion() {
        return valoracion;
    }

    public void setValoracion(Double valoracion) {
        this.valoracion = valoracion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Consultor() {
    }

    public Consultor(String id, String nombres, String apellidos, String RFC, String anioMentor, String genero, String edad, List<String> especialidad, String urlFoto, String ubicacion, String frase, List<projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica> experienciaAcademica, List<projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional> experienciaProfesional, List<projects.solucionescolabora.com.metodoasesores.Modelos.Reconocimientos> reconocimientos, Double valoracion, String precio, String correo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.RFC = RFC;
        this.anioMentor = anioMentor;
        this.genero = genero;
        this.edad = edad;
        this.especialidad = especialidad;
        this.urlFoto = urlFoto;
        this.ubicacion = ubicacion;
        this.frase = frase;
        ExperienciaAcademica = experienciaAcademica;
        ExperienciaProfesional = experienciaProfesional;
        Reconocimientos = reconocimientos;
        this.valoracion = valoracion;
        this.precio = precio;
        this.correo = correo;
    }
}
