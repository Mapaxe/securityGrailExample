package envers

import com.mapaxe.security.User
import com.mapaxe.security.UserRevisionEntity
import org.hibernate.envers.RevisionListener

class SpringSecurityRevisionListener implements RevisionListener {
    def springSecurityService
    public void newRevision(Object entity) {
        UserRevisionEntity revisionEntity = (UserRevisionEntity) entity
        User user = springSecurityService?.currentUser
        revisionEntity.currentUser = user
    }
}
