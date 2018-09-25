package com.lumbseat.lumbseat.entities;
import java.util.Date;

public class Datos {

    private int id;
    private Date timeStamp;
    private float sensResistivoAtrasIzq;
    private float sensResistivoAtrasDer;
    private float sensResistivoAdelIzq;
    private float sensResistivoAdelDer;
    private float sensDistLumbar;
    private float sensDistCervical;
    private boolean bienSentado;

    public Datos(int id, Date timeStamp, float sensResistivoAtrasIzq, float sensResistivoAtrasDer, float sensResistivoAdelIzq, float sensResistivoAdelDer, float sensDistLumbar, float sensDistCervical, boolean bienSentado) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.sensResistivoAtrasIzq = sensResistivoAtrasIzq;
        this.sensResistivoAtrasDer = sensResistivoAtrasDer;
        this.sensResistivoAdelIzq = sensResistivoAdelIzq;
        this.sensResistivoAdelDer = sensResistivoAdelDer;
        this.sensDistLumbar = sensDistLumbar;
        this.sensDistCervical = sensDistCervical;
        this.bienSentado = bienSentado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getSensResistivoAtrasIzq() {
        return sensResistivoAtrasIzq;
    }

    public void setSensResistivoAtrasIzq(float sensResistivoAtrasIzq) {
        this.sensResistivoAtrasIzq = sensResistivoAtrasIzq;
    }

    public float getSensResistivoAtrasDer() {
        return sensResistivoAtrasDer;
    }

    public void setSensResistivoAtrasDer(float sensResistivoAtrasDer) {
        this.sensResistivoAtrasDer = sensResistivoAtrasDer;
    }

    public float getSensResistivoAdelIzq() {
        return sensResistivoAdelIzq;
    }

    public void setSensResistivoAdelIzq(float sensResistivoAdelIzq) {
        this.sensResistivoAdelIzq = sensResistivoAdelIzq;
    }

    public float getSensResistivoAdelDer() {
        return sensResistivoAdelDer;
    }

    public void setSensResistivoAdelDer(float sensResistivoAdelDer) {
        this.sensResistivoAdelDer = sensResistivoAdelDer;
    }

    public float getSensDistLumbar() {
        return sensDistLumbar;
    }

    public void setSensDistLumbar(float sensDistLumbar) {
        this.sensDistLumbar = sensDistLumbar;
    }

    public float getSensDistCervical() {
        return sensDistCervical;
    }

    public void setSensDistCervical(float sensDistCervical) {
        this.sensDistCervical = sensDistCervical;
    }

    public boolean isBienSentado() {
        return bienSentado;
    }

    public void setBienSentado(boolean bienSentado) {
        this.bienSentado = bienSentado;
    }
}
