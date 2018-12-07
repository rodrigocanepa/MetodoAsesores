package projects.solucionescolabora.com.metodoasesores.Modelos;

/**
 * Created by rodrigocanepacruz on 30/10/18.
 */

public class ExperienciaProfesional {

    private String nombreEmpresa;
    private String periodo;
    private String puesto;
    private String descripcion;
    private String ubicacion;

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
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

    public ExperienciaProfesional(String nombreEmpresa, String periodo, String puesto, String descripcion, String ubicacion) {
        this.nombreEmpresa = nombreEmpresa;
        this.periodo = periodo;
        this.puesto = puesto;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
    }

    public ExperienciaProfesional() {
    }
}
