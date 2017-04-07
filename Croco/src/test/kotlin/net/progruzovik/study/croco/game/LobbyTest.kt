package net.progruzovik.study.croco.game

import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test

class LobbyTest {

    private lateinit var lobby: Lobby
    private val keyword: String = "cube"

    @Before fun setUp() {
        lobby = Lobby(MockPlayer("1", "Tom"), MockPlayer("2", "Carl"), keyword)
    }

    @Test fun addGuesser() {
        assertFalse(lobby.addGuesser(lobby.painter))
        assertFalse(lobby.addGuesser(lobby.guessers.first()))
        assertTrue(lobby.addGuesser(MockPlayer("3", "John")))
        assertEquals(2, lobby.guessers.size)
    }

    @Test fun addMessage() {
        assertFalse(lobby.addMessage(lobby.painter, "Hello player!"))
        assertTrue(lobby.addMessage(lobby.guessers.first(), "Hello painter!"))
        assertEquals(1, lobby.messages.size)
    }

    @Test fun addKeyword() {
        assertNull(lobby.winner)
        assertTrue(lobby.addMessage(lobby.guessers.first(), keyword))
        assertEquals(lobby.winner, lobby.guessers.first())
    }

    @Test fun markMessage() {
        assertFalse(lobby.markMessage(lobby.painter, 0, true))
        lobby.addMessage(lobby.guessers.first(), "Word")
        assertFalse(lobby.markMessage(lobby.guessers.first(), 0, true))

        assertNull(lobby.messages[0].isMarked)
        assertTrue(lobby.markMessage(lobby.painter, 0, true))
        assertNotNull(lobby.messages[0].isMarked)
    }

    @Test fun addQuad() {
        assertFalse(lobby.addQuad(lobby.painter, -1, 0))
        assertFalse(lobby.addQuad(lobby.guessers.first(), 0, 0))
        assertTrue(lobby.addQuad(lobby.painter, 0, 0))
        assertEquals(1, lobby.quads.size)
    }

    @Test fun removeQuad() {
        lobby.addQuad(lobby.painter, 0, 0)
        lobby.addQuad(lobby.painter, 1, 1)
        lobby.painter.isQuadsRemoved = false
        lobby.guessers.forEach { it.isQuadsRemoved = false }

        assertFalse(lobby.removeQuad(lobby.painter, -1))
        assertFalse(lobby.removeQuad(lobby.guessers.first(), 0))
        assertEquals(2, lobby.quads.size)
        assertFalse(lobby.painter.isQuadsRemoved)
        assertFalse(lobby.guessers.first().isQuadsRemoved)

        assertTrue(lobby.removeQuad(lobby.painter, 0))
        assertEquals(1, lobby.quads.size)
        assertTrue(lobby.painter.isQuadsRemoved)
        assertTrue(lobby.guessers.first().isQuadsRemoved)

        assertTrue(lobby.removeQuad(lobby.painter, 1))
        assertTrue(lobby.quads.isEmpty())
        assertTrue(lobby.removeQuad(lobby.painter, 2))
    }

    @Test fun removeQuads() {
        lobby.addQuad(lobby.painter, 0, 0)
        lobby.addQuad(lobby.painter, 1, 1)
        lobby.painter.isQuadsRemoved = false
        lobby.guessers.forEach { it.isQuadsRemoved = false }

        assertFalse(lobby.removeQuads(lobby.guessers.first()))
        assertFalse(lobby.quads.isEmpty())
        assertFalse(lobby.painter.isQuadsRemoved)
        assertFalse(lobby.guessers.first().isQuadsRemoved)

        assertTrue(lobby.removeQuads(lobby.painter))
        assertTrue(lobby.quads.isEmpty())
        assertTrue(lobby.painter.isQuadsRemoved)
        assertTrue(lobby.guessers.first().isQuadsRemoved)
    }

    @Test fun getKeyword() {
        assertNull(lobby.requestKeyword(lobby.guessers.first()))
        assertEquals(keyword, lobby.requestKeyword(lobby.painter))
    }
}