package envers

import grails.plugin.springsecurity.SpringSecurityService


class SpringSecurityServiceHolder {

    static SpringSecurityService springSecurityService

    SpringSecurityServiceHolder(SpringSecurityService springSecurityService) {
        this.springSecurityService = springSecurityService
    }
}