package net.progruzovik.study.croco.game

import junit.framework.TestCase.*
import net.progruzovik.study.croco.enum.GameStatus
import net.progruzovik.study.croco.enum.Role
import org.junit.Before
import org.junit.Test

class LobbyTest {

    private val players: List<Player> = listOf(
            MockPlayer("1", "Tom"),
            MockPlayer("2", "John", Role.PAINTER))
    private val keyword: String = "cube"
    private lateinit var lobby: Lobby

    @Before fun setUp() {
        lobby = Lobby(players, keyword)
        players.forEach { it.gameStatus = GameStatus.ACTUAL }
    }

    @Test fun addMessage() {
        assertTrue(lobby.addMessage(players.first { it.role == Role.PLAYER }, "Hello painter!"))
        assertFalse(lobby.addMessage(players.first { it.role == Role.PAINTER }, "Hello player!"))
        assertEquals(lobby.messages.size, 1)
        assertEquals(players[0].gameStatus, GameStatus.MODIFIED)
    }

    @Test fun addKeyword() {
        assertNull(players.find { it.role == Role.WINNER })
        assertTrue(lobby.addMessage(players.first { it.role == Role.PLAYER }, keyword))
        assertNotNull(players.find { it.role == Role.WINNER })
    }
}