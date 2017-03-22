package net.progruzovik.study.croco.game

import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test

class LobbyTest {

    private lateinit var lobby: Lobby

    @Before fun setUp() {
        lobby = Lobby(MockPlayer("1", "Tom"), MockPlayer("2", "Carl"))
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
        assertTrue(lobby.addMessage(lobby.guessers.first(), lobby.getKeyword(lobby.painter)!!))
        assertNotNull(lobby.winner)
    }

    @Test fun addQuad() {
        assertFalse(lobby.addQuad(lobby.painter, -1, 0))
        assertFalse(lobby.addQuad(lobby.guessers.first(), 0, 0))
        assertTrue(lobby.addQuad(lobby.painter, 0, 0))
        assertEquals(lobby.quads.size, 1)
    }

    @Test fun removeQuads() {
        lobby.addQuad(lobby.painter, 0, 0)
        lobby.painter.wasRedrawn = false
        lobby.guessers.forEach { it.wasRedrawn = false }

        assertFalse(lobby.removeQuads(lobby.guessers.first()))
        assertFalse(lobby.quads.isEmpty())
        assertFalse(lobby.painter.wasRedrawn)
        assertFalse(lobby.guessers.first().wasRedrawn)

        assertTrue(lobby.removeQuads(lobby.painter))
        assertTrue(lobby.quads.isEmpty())
        assertTrue(lobby.painter.wasRedrawn)
        assertTrue(lobby.guessers.first().wasRedrawn)
    }

    @Test fun getKeyword() {
        assertNull(lobby.getKeyword(lobby.guessers.first()))
        assertNotNull(lobby.getKeyword(lobby.painter))
    }
}