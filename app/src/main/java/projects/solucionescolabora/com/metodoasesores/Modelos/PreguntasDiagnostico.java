package projects.solucionescolabora.com.metodoasesores.Modelos;

public class PreguntasDiagnostico {

    private String titulo;
    private String pregunta;
    private boolean estaRespondida;
    private String respuesta;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public boolean isEstaRespondida() {
        return estaRespondida;
    }

    public void setEstaRespondida(boolean estaRespondida) {
        this.estaRespondida = estaRespondida;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public PreguntasDiagnostico(String titulo, String pregunta, boolean estaRespondida, String respuesta) {
        this.titulo = titulo;
        this.pregunta = pregunta;
        this.estaRespondida = estaRespondida;
        this.respuesta = respuesta;
    }
}
