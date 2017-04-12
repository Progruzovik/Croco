package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.data.Role
import net.progruzovik.study.croco.getLogger
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy
import javax.servlet.http.HttpSession

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
open class SessionPlayer(session: HttpSession) : Player {

    override val id: String = session.id
    override var name: String = "Guest"

    @JsonIgnore override var role = Role.IDLER
    @JsonIgnore override var lobby: Lobby? = null
    @JsonIgnore override var isQuadsRemoved: Boolean = false

    companion object {
        private val logger = getLogger<SessionPlayer>()
    }

    init {
        logger.debug("Player with id = ${session.id} arrived")
    }

    @PreDestroy fun clear(queue: Queue) {
        if (role == Role.QUEUED) {
            queue.removePlayer(this)
        }
        logger.debug("Player with id = $id gone")
    }

    override fun addMessage(text: String): Boolean {
        return lobby?.addMessage(this, text) ?: false
    }

    override fun markMessage(number: Int, isMarked: Boolean?): Boolean {
        return lobby?.markMessage(this, number, isMarked) ?: false
    }

    override fun addQuad(number: Int, color: Int): Boolean {
        return lobby?.addQuad(this, number, color) ?: false
    }

    override fun removeQuad(number: Int): Boolean {
        return lobby?.removeQuad(this, number) ?: false
    }

    override fun removeQuads(): Boolean {
        return lobby?.removeQuads(this) ?: false
    }

    override fun requestKeyword(): String? = lobby?.requestKeyword(this)
}