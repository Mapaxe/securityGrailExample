import com.mapaxe.security.RequestMap
import com.mapaxe.security.Role
import com.mapaxe.security.RoleGroup
import com.mapaxe.security.RoleGroupRole
import com.mapaxe.security.User
import com.mapaxe.security.UserRoleGroup
import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.DefaultGrailsControllerClass

class BootStrap {

    def dataSource
    def grailsApplication

    def init = { servletContext ->

        def scriptSQL = servletContext.getRealPath('/scriptSQL/') + '/'
        try {
            println "###########Loading RequestMap Sql###########"

            InputStream is = new FileInputStream(scriptSQL.toString()+'requestMap.sql')
            Sql db = new Sql(dataSource)

            is.eachLine { line ->
                db.executeInsert(line)
            }
            println "###########Loaded RequestMap Sql###########"
        } catch (Exception ex) {
            ex.printStackTrace()
        }
        println "###########Loading RequestMap Dynamic Controller###########"
            createDinamicRequestMap("com.mapaxe.")
        println "###########Loaded RequestMap Dynamic Controller###########"

        println "########### Start Create AdminGroup ###########"
        def adminGrup = new RoleGroup(name: 'ROLE_ADMIN').save(flush: true)
        setAllActions(adminGrup)
        println "########### End Create AdminGroup ###########"

        println "########### Start Create UserAdminGroup ###########"
        def userAdminGroup = new RoleGroup(name: 'ROLE_USER_ADMIN').save(flush: true)
        setAllActionByController(userAdminGroup,'USER')
        println "########### End Create UserAdminGroup ###########"

        println "########### Start Create RoleAdminGroup ###########"
        def roleAdminGrup = new RoleGroup(name: 'ROLE_ROLE_ADMIN').save(flush: true)
        setAllActionByController(roleAdminGrup,'ROLE')
        println "########### End Create RoleAdminGroup ###########"

        println "########### Start Create RoleGroupAdminGroup ###########"
        def roleGroupAdminGrup = new RoleGroup(name: 'ROLE_ROLEGROUP_ADMIN').save(flush: true)
        setAllActionByController(roleGroupAdminGrup,'ROLEGROUP')
        println "########### End Create RoleGroupAdminGroup ###########"



        println "########### Start Create Admin User ###########"
        def adminUser = new User(username: 'admin', password: 'admin').save(flush: true)
        UserRoleGroup.create(adminUser,adminGrup,true)
        println "########### End Create Admin User ###########"

        println "########### Start Create UsersAdmin User ###########"
        def usersAdmin = new User(username: 'users_admin', password: 'admin').save(flush: true)
        UserRoleGroup.create(usersAdmin,userAdminGroup,true)
        println "########### End Create UsersAdmin User ###########"

        println "########### Start Create RolsAdmin User ###########"
        def rolsAdmin = new User(username: 'rols_admin', password: 'admin').save(flush: true)
        UserRoleGroup.create(rolsAdmin,roleAdminGrup,true)
        println "########### End Create RolsAdmin User ###########"

        println "########### Start Create RoleGrpAdmin User ###########"
        def roleGroupsAdmin = new User(username: 'role_grp_admin', password: 'admin').save(flush: true)
        UserRoleGroup.create(roleGroupsAdmin,roleGroupAdminGrup,true)
        println "########### End Create RoleGrpAdmin User ###########"

        println "########### Start Create AdminUser ###########"
        def adminUser2 = new User(username: 'admin2', password: 'admin').save(flush: true)
        UserRoleGroup.create(adminUser2,userAdminGroup,true)
        UserRoleGroup.create(adminUser2,roleAdminGrup,true)
        UserRoleGroup.create(adminUser2,roleGroupAdminGrup,true)
        println "########### End Create AdminUser ###########"

    }
    def destroy = {
    }
    def setAllActionByController(RoleGroup roleGroup, String controllerName){
        def roles = Role.findAllByAuthorityLike('ROLE_'+controllerName+'%')
        roles.each {
            RoleGroupRole.create(roleGroup,it,true)
        }
    }
    def setAllActions(RoleGroup roleGroup){
        def roles = Role.findAll()
        roles.each {
            RoleGroupRole.create(roleGroup,it,true)
        }
    }
    def createDinamicRequestMap(String packageName){
        grailsApplication.controllerClasses.each {DefaultGrailsControllerClass controllerClass ->

            if (controllerClass.packageName.startsWith(packageName)){
                def actions = controllerClass.getURIs().collect{ uri ->
                    controllerClass.getMethodActionName(uri)
                }.unique().sort()
                String controllerName = controllerClass.getName()[0].toLowerCase() +
                        controllerClass.getName().substring(1)
                String urlBase = '/'+controllerName
                new Role(authority: 'ROLE_' + controllerName.toUpperCase() + '_ALL').save(flush: true)
                new RequestMap(
                        url: urlBase+'/**',
                        configAttribute: 'ROLE_' + controllerName.toUpperCase() + '_ALL'
                ).save(flush: true)
                actions.each {
                    new Role(authority: 'ROLE_' + controllerName.toUpperCase()+'_'+it.toUpperCase()).save(flush: true)
                    new RequestMap(
                            url: urlBase+'/'+it+'/*',
                            configAttribute: 'ROLE_' + controllerName.toUpperCase()+'_'+it.toUpperCase()
                    ).save(flush: true)
                }
                println "Controller Name:" + controllerName
                println "Controller:" + controllerClass.getClazz()
                println "Actions: " + actions
//                new RequestMap(url: )
            }
        }
    }
}
