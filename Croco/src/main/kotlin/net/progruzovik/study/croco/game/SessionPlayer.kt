package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.getLogger
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PreDestroy
import javax.servlet.http.HttpSession

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
open class SessionPlayer(session: HttpSession) : Player {

    companion object {
        private val logger = getLogger<SessionPlayer>()
        private val queuedPlayers: MutableSet<SessionPlayer> = LinkedHashSet()
    }

    init {
        logger.debug("Player with id = ${session.id} arrived")
    }

    override val id: String = session.id
    override var name: String = "Guest"
    override val roleCode: Int
        get() = role.ordinal

    @JsonIgnore override var role: Role = Role.IDLER
    @JsonIgnore override lateinit var lobby: Lobby
    override val keyword: String?
        @JsonIgnore get() = lobby.requestKeyword(role)

    @PreDestroy fun clear() {
        if (role == Role.QUEUED) {
            removeFromQueue()
        }
        logger.debug("Player with id = $id gone")
    }

    override fun addToQueue(): Boolean {
        if ((role == Role.IDLER || role == Role.WINNER) && queuedPlayers.add(this)) {
            role = Role.QUEUED
            if (queuedPlayers.size >= Lobby.SIZE) {
                val players: List<SessionPlayer> = queuedPlayers.take(Lobby.SIZE)
                queuedPlayers.removeAll(players)
                val lobby = Lobby(players, "куб")
                players.forEachIndexed { i, player ->
                    player.role = if (i == 1) Role.PAINTER else Role.PLAYER
                    player.lobby = lobby
                }
            }
            return true
        }
        return false
    }

    override fun removeFromQueue(): Boolean {
        if (queuedPlayers.remove(this)) {
            role = Role.IDLER
            return true
        }
        return false
    }

    override fun say(text: String): Boolean = lobby.addMessage(this, text)

    override fun paint(quad: Quad): Boolean = lobby.addQuad(role, quad)
}