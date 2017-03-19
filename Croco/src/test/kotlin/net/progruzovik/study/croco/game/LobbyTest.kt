package net.progruzovik.study.croco.game

import junit.framework.TestCase.*
import net.progruzovik.study.croco.enum.GameStatus
import org.junit.Before
import org.junit.Test

class LobbyTest {

    private lateinit var lobby: Lobby

    @Before fun setUp() {
        lobby = Lobby(listOf(MockPlayer("1", "Tom"), MockPlayer("2", "Carl")))
    }

    @Test fun addMessage() {
        assertFalse(lobby.addMessage(lobby.painter, "Hello player!"))
        assertEquals(GameStatus.ACTUAL, lobby.painter.gameStatus)
        assertEquals(GameStatus.ACTUAL, lobby.guessers.first().gameStatus)

        assertTrue(lobby.addMessage(lobby.guessers.first(), "Hello painter!"))
        assertEquals(1, lobby.messages.size)
        assertEquals(GameStatus.MODIFIED, lobby.painter.gameStatus)
        assertEquals(GameStatus.MODIFIED, lobby.guessers.first().gameStatus)
    }

    @Test fun addKeyword() {
        assertNull(lobby.winner)
        assertTrue(lobby.addMessage(lobby.guessers.first(), lobby.getKeyword(lobby.painter)!!))
        assertNotNull(lobby.winner)
    }

    @Test fun addQuad() {
        assertFalse(lobby.addQuad(lobby.painter, -1, 0))
        assertFalse(lobby.addQuad(lobby.guessers.first(), 0, 0))
        assertEquals(GameStatus.ACTUAL, lobby.painter.gameStatus)
        assertEquals(GameStatus.ACTUAL, lobby.guessers.first().gameStatus)

        assertTrue(lobby.addQuad(lobby.painter, 0, 0))
        assertEquals(lobby.quads.size, 1)
        assertEquals(GameStatus.MODIFIED, lobby.painter.gameStatus)
        assertEquals(GameStatus.MODIFIED, lobby.guessers.first().gameStatus)
    }

    @Test fun removeQuads() {
        lobby.addQuad(lobby.painter, 0, 0)
        assertFalse(lobby.removeQuads(lobby.guessers.first()))
        assertFalse(lobby.quads.isEmpty())
        assertEquals(GameStatus.MODIFIED, lobby.painter.gameStatus)
        assertEquals(GameStatus.MODIFIED, lobby.guessers.first().gameStatus)

        assertTrue(lobby.removeQuads(lobby.painter))
        assertTrue(lobby.quads.isEmpty())
        assertEquals(GameStatus.REDRAWN, lobby.painter.gameStatus)
        assertEquals(GameStatus.REDRAWN, lobby.guessers.first().gameStatus)
    }

    @Test fun getKeyword() {
        assertNull(lobby.getKeyword(lobby.guessers.first()))
        assertNotNull(lobby.getKeyword(lobby.painter))
    }
}