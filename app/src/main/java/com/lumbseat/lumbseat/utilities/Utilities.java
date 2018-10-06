package com.lumbseat.lumbseat.utilities;

public class Utilities {

     public static final String BASE_DATOS = "bd_datos";

     public static final String TABLA_DATOS = "datos";

     //Desde la placa se enviarían todos en el orden que aparece aca, sin el id, separados por ";"
     public static final String CAMPO_ID = "id";
     public static final String CAMPO_TIMESTAMP = "timeStamp";
     public static final String CAMPO_SENS_RESISTIVO_ATRAS_IZQ = "sensResistivoAtrasIzq";
     public static final String CAMPO_SENS_RESISTIVO_ATRAS_DER = "sensResistivoAtrasDer";
     public static final String CAMPO_SENS_RESISTIVO_ADEL_IZQ = "sensResistivoAdelIzq";
     public static final String CAMPO_SENS_RESISTIVO_ADEL_DER = "sensResistivoAdelDer";
     public static final String CAMPO_SENS_DIST_LUMBAR = "sensDistLumbar";
     public static final String CAMPO_SENS_DIST_CERVICAL = "sensDistCervical";
     public static final String CAMPO_BIEN_SENTADO = "bienSentado";
     public static final String CAMPO_MAL_SENTADO_IZQ = "sentadoIzq";
     public static final String CAMPO_MAL_SENTADO_DER = "sentadoDer";
     public static final String CAMPO_MAL_ABAJO_LEJOS = "sentadoLejosAbajo";
     public static final String CAMPO_MAL_ARRIBA_LEJOS = "sentadoLejosArriba";

    public static final String CREAR_TABLA_DATOS = "CREATE TABLE " + TABLA_DATOS +
            " ( " + CAMPO_ID + " INTEGER, " +
            CAMPO_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            CAMPO_SENS_RESISTIVO_ATRAS_IZQ + " float(8,4), " +
            CAMPO_SENS_RESISTIVO_ATRAS_DER + " float(8,4), " +
            CAMPO_SENS_RESISTIVO_ADEL_IZQ + " float(8,4), " +
            CAMPO_SENS_RESISTIVO_ADEL_DER + " float(8,4), " +
            CAMPO_SENS_DIST_LUMBAR + " float(8,4), " +
            CAMPO_SENS_DIST_CERVICAL + " float(8,4), " +
            CAMPO_BIEN_SENTADO + " bool," +
            CAMPO_MAL_SENTADO_IZQ + " bool," +
            CAMPO_MAL_SENTADO_DER + " bool," +
            CAMPO_MAL_ABAJO_LEJOS + " bool," +
            CAMPO_MAL_ARRIBA_LEJOS + " bool)";
}
