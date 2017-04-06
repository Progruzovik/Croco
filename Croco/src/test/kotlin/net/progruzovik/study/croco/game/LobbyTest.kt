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
        assertFalse(lobby.removeQuad(lobby.painter, -1))
        assertFalse(lobby.removeQuad(lobby.guessers.first(), 0))
        assertTrue(lobby.removeQuad(lobby.painter, 0))
        assertTrue(lobby.quads.isEmpty())
        assertTrue(lobby.removeQuad(lobby.painter, 1))
    }

    @Test fun removeQuads() {
        lobby.addQuad(lobby.painter, 0, 0)
        lobby.painter.isQuadsRedrawn = false
        lobby.guessers.forEach { it.isQuadsRedrawn = false }

        assertFalse(lobby.removeQuads(lobby.guessers.first()))
        assertFalse(lobby.quads.isEmpty())
        assertFalse(lobby.painter.isQuadsRedrawn)
        assertFalse(lobby.guessers.first().isQuadsRedrawn)

        assertTrue(lobby.removeQuads(lobby.painter))
        assertTrue(lobby.quads.isEmpty())
        assertTrue(lobby.painter.isQuadsRedrawn)
        assertTrue(lobby.guessers.first().isQuadsRedrawn)
    }

    @Test fun getKeyword() {
        assertNull(lobby.getKeyword(lobby.guessers.first()))
        assertEquals(keyword, lobby.getKeyword(lobby.painter))
    }
}