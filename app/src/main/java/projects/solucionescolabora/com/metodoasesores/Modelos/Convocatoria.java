package projects.solucionescolabora.com.metodoasesores.Modelos;

import java.util.List;
import java.util.Map;

/**
 * Created by rodrigocanepacruz on 07/11/18.
 */

public class Convocatoria {

    private String id;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private String locacion;
    private int asistentes;
    private String urlFoto1;
    private String urlFoto2;
    private String urlFoto3;
    private List<ConsultorConv> ConsultorConvs;
    private String comentarios;
    private List<String> especialidades;
    private String diaConfirmar;
    private String diaEvento;
    private String status;
    private String idInstitucion;
    private String urlFotoInstitucion;
    private String nombreInstitucion;
    private List<ConsultorConv> Postulados;
    private String precio;

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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLocacion() {
        return locacion;
    }

    public void setLocacion(String locacion) {
        this.locacion = locacion;
    }

    public int getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(int asistentes) {
        this.asistentes = asistentes;
    }

    public String getUrlFoto1() {
        return urlFoto1;
    }

    public void setUrlFoto1(String urlFoto1) {
        this.urlFoto1 = urlFoto1;
    }

    public String getUrlFoto2() {
        return urlFoto2;
    }

    public void setUrlFoto2(String urlFoto2) {
        this.urlFoto2 = urlFoto2;
    }

    public String getUrlFoto3() {
        return urlFoto3;
    }

    public void setUrlFoto3(String urlFoto3) {
        this.urlFoto3 = urlFoto3;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }

    public String getDiaConfirmar() {
        return diaConfirmar;
    }

    public void setDiaConfirmar(String diaConfirmar) {
        this.diaConfirmar = diaConfirmar;
    }

    public String getDiaEvento() {
        return diaEvento;
    }

    public void setDiaEvento(String diaEvento) {
        this.diaEvento = diaEvento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public String getUrlFotoInstitucion() {
        return urlFotoInstitucion;
    }

    public void setUrlFotoInstitucion(String urlFotoInstitucion) {
        this.urlFotoInstitucion = urlFotoInstitucion;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public List<ConsultorConv> getConsultorConvs() {
        return ConsultorConvs;
    }

    public void setConsultorConvs(List<ConsultorConv> consultorConvs) {
        ConsultorConvs = consultorConvs;
    }

    public List<ConsultorConv> getPostulados() {
        return Postulados;
    }

    public void setPostulados(List<ConsultorConv> postulados) {
        Postulados = postulados;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Convocatoria() {
    }


    public Convocatoria(String id, String titulo, String descripcion, String ubicacion, String locacion, int asistentes, String urlFoto1, String urlFoto2, String urlFoto3, List<ConsultorConv> consultorConvs, String comentarios, List<String> especialidades, String diaConfirmar, String diaEvento, String status, String idInstitucion, String urlFotoInstitucion, String nombreInstitucion, List<ConsultorConv> postulados, String precio) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.locacion = locacion;
        this.asistentes = asistentes;
        this.urlFoto1 = urlFoto1;
        this.urlFoto2 = urlFoto2;
        this.urlFoto3 = urlFoto3;
        ConsultorConvs = consultorConvs;
        this.comentarios = comentarios;
        this.especialidades = especialidades;
        this.diaConfirmar = diaConfirmar;
        this.diaEvento = diaEvento;
        this.status = status;
        this.idInstitucion = idInstitucion;
        this.urlFotoInstitucion = urlFotoInstitucion;
        this.nombreInstitucion = nombreInstitucion;
        Postulados = postulados;
        this.precio = precio;
    }
}
