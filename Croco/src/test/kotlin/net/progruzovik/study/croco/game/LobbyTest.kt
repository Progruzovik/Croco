package net.progruzovik.study.croco.game

import junit.framework.TestCase.*
import net.progruzovik.study.croco.enum.GameStatus
import net.progruzovik.study.croco.enum.Role
import org.junit.Before
import org.junit.Test

class LobbyTest {

    private val players: List<Player> = listOf(
            MockPlayer("1", "Tom"),
            MockPlayer("2", "Carl", Role.PAINTER))
    private val keyword: String = "cube"
    private lateinit var lobby: Lobby

    @Before fun setUp() {
        lobby = Lobby(players, keyword)
        players.forEach { it.gameStatus = GameStatus.ACTUAL }
    }

    @Test fun addMessage() {
        assertFalse(lobby.addMessage(players.first { it.role == Role.PAINTER }, "Hello player!"))
        assertEquals(players[0].gameStatus, GameStatus.ACTUAL)

        assertTrue(lobby.addMessage(players.first { it.role == Role.GUESSER }, "Hello painter!"))
        assertEquals(lobby.messages.size, 1)
        assertEquals(players[0].gameStatus, GameStatus.MODIFIED)
    }

    @Test fun addKeyword() {
        assertNull(players.find { it.role == Role.WINNER })
        assertTrue(lobby.addMessage(players.first { it.role == Role.GUESSER }, keyword))
        assertNotNull(players.find { it.role == Role.WINNER })
    }

    @Test fun addQuad() {
        assertFalse(lobby.addQuad(Role.GUESSER, 0, 0))
        assertFalse(lobby.addQuad(Role.PAINTER, -1, 0))
        assertEquals(players[0].gameStatus, GameStatus.ACTUAL)

        assertTrue(lobby.addQuad(Role.PAINTER, 0, 0))
        assertEquals(lobby.quads.size, 1)
        assertEquals(players[0].gameStatus, GameStatus.MODIFIED)
    }

    @Test fun removeQuads() {
        assertFalse(lobby.removeQuads(Role.GUESSER))
        assertEquals(players[0].gameStatus, GameStatus.ACTUAL)

        assertTrue(lobby.removeQuads(Role.PAINTER))
        assertEquals(players[0].gameStatus, GameStatus.REDRAWN)
    }

    @Test fun getKeyword() {
        assertNull(lobby.getKeyword(Role.GUESSER))
        assertEquals(lobby.getKeyword(Role.PAINTER), keyword)
    }
}