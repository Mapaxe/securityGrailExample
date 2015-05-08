package com.mapaxe.security

class User {

	transient springSecurityService
    transient bitacoraService
    static auditable = [handlersOnly:true]

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<RoleGroup> getAuthorities() {
		UserRoleGroup.findAllByUser(this).collect { it.roleGroup }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
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

    @Override
    public String toString() {
        return username
    }
}
