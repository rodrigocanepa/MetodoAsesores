package projects.solucionescolabora.com.metodoasesores.Modelos;

public class Anuncios {

    private String id;
    private String titulo;
    private String descripcion;
    private String urlInstitucion;
    private String link;
    private String nombreInstitucion;

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

    public String getUrlInstitucion() {
        return urlInstitucion;
    }

    public void setUrlInstitucion(String urlInstitucion) {
        this.urlInstitucion = urlInstitucion;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public Anuncios(String id, String titulo, String descripcion, String urlInstitucion, String link, String nombreInstitucion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlInstitucion = urlInstitucion;
        this.link = link;
        this.nombreInstitucion = nombreInstitucion;
    }

    public Anuncios() {
    }


}
