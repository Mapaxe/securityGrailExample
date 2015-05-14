package security.bitacora

import securitygrailexample.BitacoraService

/**
 * Created with IntelliJ IDEA.
 * User: JA Verde.
 * Date: 13/05/15
 * Time: 16:09
 */
abstract class AuditLogging {
    BitacoraService bitacoraService

    private BitacoraService getBitacoraservice () {
        if (!bitacoraService)
            bitacoraService = new BitacoraService()

        bitacoraService
    }

    def onSave = {newState ->
        String descripcion = ''
        Long identificador = 0
        newState.each{key, value ->
            descripcion = descripcion + ' * ' + key + " : " + value + '\n'
            if(key.equals('id')){
                identificador = value
            }
        }
        this.withNewSession {
            getBitacoraservice().guardarAccion('Crear', descripcion, this.domainClass.getName(), identificador)
        }
    }

    def onDelete = {oldState ->
        String descripcion = ''
        Long identificador = 0
        oldState.each{key, value ->
            descripcion = descripcion + ' * ' + key + " : " + value + '\n'
            if(key.equals('id')){
                identificador = value
            }
        }
        this.withNewSession {
            getBitacoraservice().guardarAccion('Eliminar', descripcion, this.domainClass.getName(), identificador)
        }
    }

    def onChange = { oldMap,newMap ->
        String descripcion = ''
        Long identificador = 0
        oldMap.each({ key, value ->
            if(value != newMap[key]) {

                descripcion = descripcion + ' * ' + key + ' cambio de ' + value + ' a ' + newMap[key] + '\n'
            }
            if(newMap['id']){
                identificador = value
            }
        })
        this.withNewSession {
            getBitacoraservice().guardarAccion('Editar', descripcion, this.domainClass.getName(), identificador)
        }
    }
}
