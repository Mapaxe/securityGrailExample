package com.mapaxe.security

class Role {
    transient bitacoraService
    static auditable = [handlersOnly:true]

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
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
        withNewSession {
            bitacoraService.guardarAccion('Crear', descripcion, this.domainClass.getName(), identificador)
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
        withNewSession {
            bitacoraService.guardarAccion('Eliminar', descripcion, this.domainClass.getName(), identificador)
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
        withNewSession {
            bitacoraService.guardarAccion('Editar', descripcion, this.domainClass.getName(), identificador)
        }
    }
}
