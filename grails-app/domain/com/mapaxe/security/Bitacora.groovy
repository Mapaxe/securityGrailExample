package com.mapaxe.security

import org.joda.time.DateTime
import org.joda.time.contrib.hibernate.PersistentDateTime

class Bitacora {
    String usuario
    String tipoAccion
    DateTime fechaAccion
    String descripcion
    String tabla
    Long tablaId

    static constraints = {
        usuario nullable: false
        tipoAccion nullable: false
        tabla nullable: true
        tablaId nullable: true
    }

    static mapping = {
        descripcion  sqlType: 'clob'
        fechaAccion type: PersistentDateTime
    }
}
