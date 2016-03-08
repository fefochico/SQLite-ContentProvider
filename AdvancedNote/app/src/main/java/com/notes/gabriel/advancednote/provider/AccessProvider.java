package com.notes.gabriel.advancednote.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ysuarez on 4/3/16.
 */
public class AccessProvider {
    public static final String AUTHORITY ="com.notes.gabriel.advancednote";

    //Nombre de la tabla de la base de datos
    public static final String NOTES_TABLE = "notes";

    public static Uri CONTENT_URI= Uri.parse("content://" + AUTHORITY + "/" + NOTES_TABLE);

    //Código para URIs de multiples registros
    public static final int ALLROWS= 1;
    //Código para URIs de un solo registro
    public static final int SINGLE_ROW= 2;

    //Comparador de URIs de contenido
    public static final UriMatcher uriMatcher;
    //Asignación de URIs
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NOTES_TABLE, ALLROWS);
        uriMatcher.addURI(AUTHORITY, NOTES_TABLE + "/#", SINGLE_ROW);
    }


    //Estructura de la tabla
    public static final class Columnas implements BaseColumns {
        private Columnas(){

        }
        public static final String ID = "id";
        public static final String DATE= "date";
        public static final String TITLE= "title";
        public static final String BODY= "body";
    }

    //Tipo MIME que retorna la consulta de una sola fila
    public final static String SINGLE_MIME=
            "vnd.android.cursor.dir/vnd." + AUTHORITY;

    //Tipo MIME que retorna la consulta de CONTENT_URI
    public final static String MULTIPLE_MIME=
            "vnd.android.cursor.dir/vnd." + AUTHORITY;


}
