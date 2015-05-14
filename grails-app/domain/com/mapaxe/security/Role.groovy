package com.mapaxe.security

import security.bitacora.AuditLogging

class Role extends AuditLogging {
    String authority

    static auditable = [handlersOnly:true]
    static transients = ['bitacoraService']

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}


}
