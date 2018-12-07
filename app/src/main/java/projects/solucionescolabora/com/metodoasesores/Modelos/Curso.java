package projects.solucionescolabora.com.metodoasesores.Modelos;

import java.util.List;

/**
 * Created by rodrigocanepacruz on 08/11/18.
 */

public class Curso {

    private String id;
    private String titulo;
    private String descripcion;
    private String duracion;
    private String tipo;
    private String requerimientos;
    private String objetivos;
    private List<Temas> temas;
    private String idConsultor;
    private String nombreConsultor;
    private String urlFotoConsultor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRequerimientos() {
        return requerimientos;
    }

    public void setRequerimientos(String requerimientos) {
        this.requerimientos = requerimientos;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public List<Temas> getTemas() {
        return temas;
    }

    public void setTemas(List<Temas> temas) {
        this.temas = temas;
    }

    public String getIdConsultor() {
        return idConsultor;
    }

    public void setIdConsultor(String idConsultor) {
        this.idConsultor = idConsultor;
    }

    public String getNombreConsultor() {
        return nombreConsultor;
    }

    public void setNombreConsultor(String nombreConsultor) {
        this.nombreConsultor = nombreConsultor;
    }

    public String getUrlFotoConsultor() {
        return urlFotoConsultor;
    }

    public void setUrlFotoConsultor(String urlFotoConsultor) {
        this.urlFotoConsultor = urlFotoConsultor;
    }

    public Curso() {
    }

    public Curso(String id, String titulo, String descripcion, String duracion, String tipo, String requerimientos, String objetivos, List<Temas> temas, String idConsultor, String nombreConsultor, String urlFotoConsultor) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.tipo = tipo;
        this.requerimientos = requerimientos;
        this.objetivos = objetivos;
        this.temas = temas;
        this.idConsultor = idConsultor;
        this.nombreConsultor = nombreConsultor;
        this.urlFotoConsultor = urlFotoConsultor;
    }
}
