package eu.hornerproject.humulus

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser

import server.data.ObjectSystemData
import server.global.Conf
import server.global.ConfThreadLocal
import server.global.Constants
import server.*

/**
 *
 */
class UserService {

    def springSecurityService
    def healthService

    Boolean isSuperuser(User user){
        def auth = springSecurityService.authentication 
        def authorities = auth.authorities
        return authorities.find{
            log.debug("authority: ${it.authority}")
            it.authority?.equals(Constants.GROUP_SUPERUSERS)
        } != null
    }

    User getUser(){
        def principal = springSecurityService.getPrincipal();
//        log.debug("principal: $principal")
        if(principal instanceof GrailsUser){
            return User.get(principal.getId())
        }
        return null
    }

    void addUserToUsersGroup(User user){
        def usersGroup = Group.findByName(Constants.GROUP_USERS)
        if(! usersGroup){
            usersGroup = healthService.createGroup(Constants.GROUP_USERS)
        }
        GroupUser gu = new GroupUser(user, usersGroup)
        user.addToGroupUsers(gu)
        usersGroup.addToGroupUsers(gu)
        gu.save()
    }

    /**
     * Transfer all objects from one user to another.
     * This includes GroupUser, Folder and OSD objects.
     * Sessions of the source user will be simply removed.
     * The goal is to remove everything which prevents the source user account from being deleted.
     * TransferAssets may be incompatible with legal requirements for record keeping. (But not
     * all systems require full accountability.)
     * @param source current owner
     * @param target new owner of assets
     */
    void transferAssets(User source, User target){
        transferGroupMembership(source, target)
        // refreshAclCache for target?
        transferFolderOwnership(source, target)
        transferOsdOwnership(source,target)
        removeSessions(source)
    }

    Boolean transferAssetsAllowed(String repositoryName){
        Conf conf = ConfThreadLocal.getConf()
        String xpath = 'repositories/repository[name="'+repositoryName+'"]/security/transferAssetsAllowed'
        return conf.getField(xpath, 'false').equals('true')
    }

    Boolean deleteUserAllowed(String repositoryName){
        Conf conf = ConfThreadLocal.getConf()
        String xpath = 'repositories/repository[name="'+repositoryName+'"]/security/deleteUserAllowed'
        return conf.getField(xpath, 'false').equals('true')
    }

    void transferGroupMembership(User source, User target){
        def groupUsers = GroupUser.findAllByUser(source)
        groupUsers.each{gu ->
            if(GroupUser.findByUserAndGroup(target, gu.group)){
                // simply delete old user's GroupUser object as both
                // old and new are in the same group
                gu.delete()
            }
            else{
                // take over groupUser from other user.
                log.debug("transfer group ${gu.group.name} to user ${target.name}")
                gu.user = target
            }
        }
    }

    void transferFolderOwnership(User source, User target){
        Folder.findAllByOwner(source).each{folder ->
            log.debug("transfer folder ownership of ${folder.name} to user ${target.name}")
            folder.owner = target
        }
    }

    Boolean userHasAssets(User user){
        if( ObjectSystemData.findByOwner(user) ||
            ObjectSystemData.findByModifier(user) ||
            ObjectSystemData.findByCreator(user) ||
                ObjectSystemData.findByLocked_by(user)
        ){
            return true
        }
        if(Folder.findByOwner(user)){
            return true
        }
        if(GroupUser.findByUser(user)){
            return true
        }
        return false
    }

    /**
     * Replace all references to a given user with references to a target user.
     * @param source The original owner / modifier / creator / lock owner.
     * @param target The new user who replaces the source user in all cases.
     */
    void transferOsdOwnership(User source, User target){
        ObjectSystemData.findAllByOwner(source).each{osd ->
            log.debug("transfer object ownership of #${osd.id} to user ${target.name}")
            osd.owner = target
        }
        ObjectSystemData.findAllByModifier(source).each{osd ->
            log.debug("transfer modifier label of #${osd.id} to user ${target.name}")
            osd.modifier = target
        }
        ObjectSystemData.findAllByCreator(source).each{osd ->
            log.debug("transfer creator label of #${osd.id} to user ${target.name}")
            osd.creator = target
        }
        ObjectSystemData.findAllByLocked_by(source).each{osd ->
            log.debug("transfer lock_owner label of #${osd.id} to user ${target.name}")
            osd.locked_by = target
        }
    }

    void removeSessions(User source){
        log.debug("remove sessions of user ${source.name}")
        Session.findAllByUser(source).each {userSession ->
            userSession.delete()
        }
    }

    void delete(User user){
        user.delete()
    }
}
