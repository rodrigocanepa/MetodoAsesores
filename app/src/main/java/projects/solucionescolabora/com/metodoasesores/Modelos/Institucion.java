package projects.solucionescolabora.com.metodoasesores.Modelos;

/**
 * Created by rodrigocanepacruz on 30/10/18.
 */

public class Institucion {

    private String id;
    private String nombre;
    private String descripcion;
    private String urlLogo;
    private String ubicacion;
    private String rfc;
    private String plan;
    private String reportes;
    private String anuncios;
    private String fechaPago;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getReportes() {
        return reportes;
    }

    public void setReportes(String reportes) {
        this.reportes = reportes;
    }

    public String getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(String anuncios) {
        this.anuncios = anuncios;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Institucion() {
    }

    public Institucion(String id, String nombre, String descripcion, String urlLogo, String ubicacion, String rfc, String plan, String reportes, String anuncios, String fechaPago) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlLogo = urlLogo;
        this.ubicacion = ubicacion;
        this.rfc = rfc;
        this.plan = plan;
        this.reportes = reportes;
        this.anuncios = anuncios;
        this.fechaPago = fechaPago;
    }
}
