package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy
import javax.servlet.http.HttpSession

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
open class SessionPlayer @Autowired constructor(
        session: HttpSession,
        private val queueService: QueueService) : Player {

    companion object {
        val logger = getLogger<SessionPlayer>()
    }

    init {
        logger.debug("Player with id = ${session.id} arrived")
    }

    override val id: String = session.id
    override var name: String = "Guest"

    @JsonIgnore override var role: Role = Role.IDLER
    override val roleCode: Int
        get() = role.ordinal

    @JsonIgnore override lateinit var lobby: Lobby

    @PreDestroy fun clear() {
        if (role == Role.QUEUED) {
            queueService.remove(this)
        }
        logger.debug("Player with id = $id gone")
    }

    override fun toQueue(): Boolean = queueService.add(this)

    override fun fromQueue(): Boolean = queueService.remove(this)
}