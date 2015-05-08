package securitygrailexample

import com.mapaxe.security.Bitacora
import com.mapaxe.security.User
import org.joda.time.DateTime

class BitacoraService {
    def springSecurityService
    def guardarAccion(String tipoAccion, String descripcion, String tabla = '', Long tablaId = 0){
        String usuario
        if(springSecurityService.currentUser != null){
            usuario = ((User)springSecurityService.currentUser).toString()
        }else{
            usuario = 'ANONIMO'
        }
        DateTime fechaAccion = new DateTime(new Date().getTime())
        Bitacora bitacora = new Bitacora(
                fechaAccion: fechaAccion,
                usuario: usuario,
                tipoAccion: tipoAccion,
                descripcion: descripcion,
                tabla: tabla,
                tablaId: tablaId)
        bitacora.save(flush:true, failOnError: true)
    }
}
