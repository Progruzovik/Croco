package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
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
    @JsonIgnore override var isQuadsRedrawn: Boolean = false

    companion object {
        private val logger = getLogger<SessionPlayer>()

        private var queuedPlayer: Player? = null
        private var lastLobby: Lobby? = null
    }

    init {
        logger.debug("Player with id = ${session.id} arrived")
    }

    @PreDestroy fun clear() {
        if (role == Role.QUEUED) {
            removeFromQueue()
        }
        logger.debug("Player with id = $id gone")
    }

    override fun addToQueue() {
        if (lastLobby?.addGuesser(this) != true) {
            val painter: Player? = queuedPlayer
            if (painter == null) {
                queuedPlayer = this
                lastLobby = null
                role = Role.QUEUED
            } else {
                queuedPlayer = null
                lastLobby = Lobby(this, painter)
            }
        }
    }

    override fun removeFromQueue(): Boolean {
        if (queuedPlayer == this) {
            queuedPlayer = null
            role = Role.IDLER
            return true
        }
        return false
    }

    override fun say(text: String): Boolean {
        return lobby?.addMessage(this, text) ?: false
    }

    override fun markMessage(number: Int, isMarked: Boolean?): Boolean {
        return lobby?.markMessage(this, number, isMarked) ?: false
    }

    override fun paint(number: Int, color: Int): Boolean {
        return lobby?.addQuad(this, number, color) ?: false
    }

    override fun clearCanvas(): Boolean {
        return lobby?.removeQuads(this) ?: false
    }

    override fun requestKeyword(): String? = lobby?.getKeyword(this)
}