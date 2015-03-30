package com.mapaxe.security



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class RoleGroupController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond RoleGroup.list(params), model:[roleGroupInstanceCount: RoleGroup.count()]
    }

    def show(RoleGroup roleGroupInstance) {
        respond roleGroupInstance
    }

    def create() {
        respond new RoleGroup(params)
    }

    @Transactional
    def save(RoleGroup roleGroupInstance) {
        if (roleGroupInstance == null) {
            notFound()
            return
        }

        if (roleGroupInstance.hasErrors()) {
            respond roleGroupInstance.errors, view:'create'
            return
        }

        roleGroupInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'roleGroup.label', default: 'RoleGroup'), roleGroupInstance.id])
                redirect roleGroupInstance
            }
            '*' { respond roleGroupInstance, [status: CREATED] }
        }
    }

    def edit(RoleGroup roleGroupInstance) {
        respond roleGroupInstance
    }

    @Transactional
    def update(RoleGroup roleGroupInstance) {
        if (roleGroupInstance == null) {
            notFound()
            return
        }

        if (roleGroupInstance.hasErrors()) {
            respond roleGroupInstance.errors, view:'edit'
            return
        }

        roleGroupInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'RoleGroup.label', default: 'RoleGroup'), roleGroupInstance.id])
                redirect roleGroupInstance
            }
            '*'{ respond roleGroupInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(RoleGroup roleGroupInstance) {

        if (roleGroupInstance == null) {
            notFound()
            return
        }

        roleGroupInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'RoleGroup.label', default: 'RoleGroup'), roleGroupInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'roleGroup.label', default: 'RoleGroup'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
