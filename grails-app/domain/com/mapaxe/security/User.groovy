package com.mapaxe.security

import security.bitacora.AuditLogging

class User extends AuditLogging {
	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    transient springSecurityService
    static auditable = [handlersOnly:true]
	static transients = ['springSecurityService', 'bitacoraService']

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

    @Override
    public String toString() {
        return username
    }
}