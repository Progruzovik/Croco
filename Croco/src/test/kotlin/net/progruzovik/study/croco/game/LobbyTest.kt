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
        assertEquals(GameStatus.ACTUAL, players[0].gameStatus)

        assertTrue(lobby.addMessage(players.first { it.role == Role.GUESSER }, "Hello painter!"))
        assertEquals(1, lobby.messages.size)
        assertEquals(GameStatus.MODIFIED, players[0].gameStatus)
    }

    @Test fun addKeyword() {
        assertNull(players.find { it.role == Role.WINNER })
        assertTrue(lobby.addMessage(players.first { it.role == Role.GUESSER }, keyword))
        assertNotNull(players.find { it.role == Role.WINNER })
    }

    @Test fun addQuad() {
        assertFalse(lobby.addQuad(Role.GUESSER, 0, 0))
        assertFalse(lobby.addQuad(Role.PAINTER, -1, 0))
        assertEquals(GameStatus.ACTUAL, players[0].gameStatus)

        assertTrue(lobby.addQuad(Role.PAINTER, 0, 0))
        assertEquals(lobby.quads.size, 1)
        assertEquals(GameStatus.MODIFIED, players[0].gameStatus)
    }

    @Test fun removeQuads() {
        assertFalse(lobby.removeQuads(Role.GUESSER))
        assertEquals(GameStatus.ACTUAL, players[0].gameStatus)

        assertTrue(lobby.removeQuads(Role.PAINTER))
        assertEquals(GameStatus.REDRAWN, players[0].gameStatus)
    }

    @Test fun getKeyword() {
        assertNull(lobby.getKeyword(Role.GUESSER))
        assertEquals(keyword, lobby.getKeyword(Role.PAINTER))
    }
}