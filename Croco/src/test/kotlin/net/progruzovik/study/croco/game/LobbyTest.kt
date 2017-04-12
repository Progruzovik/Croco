package net.progruzovik.study.croco.game

import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LobbyTest {

    @Autowired lateinit var queue: Queue

    private val painter = MockPlayer("1", "Tom")
    private val guesser = MockPlayer("2", "Carl")

    private lateinit var lobby: Lobby

    @Before fun setUp() {
        queue.addPlayer(painter)
        queue.addPlayer(guesser)
        lobby = painter.lobby!!
    }

    @Test fun addGuesser() {
        assertFalse(lobby.addGuesser(painter))
        assertFalse(lobby.addGuesser(guesser))
        assertTrue(lobby.addGuesser(MockPlayer("3", "John")))
        assertEquals(2, lobby.guessers.size)
    }

    @Test fun addMessage() {
        assertFalse(lobby.addMessage(painter, "Hello player!"))
        assertTrue(lobby.addMessage(guesser, "Hello painter!"))
        assertEquals(1, lobby.messages.size)
    }

    @Test fun addKeyword() {
        assertNull(lobby.winner)
        assertTrue(lobby.addMessage(guesser, painter.requestKeyword()!!))
        assertEquals(lobby.winner, guesser)
    }

    @Test fun markMessage() {
        assertFalse(lobby.markMessage(painter, 0, true))
        lobby.addMessage(guesser, "Word")
        assertFalse(lobby.markMessage(guesser, 0, true))

        assertNull(lobby.messages[0].isMarked)
        assertTrue(lobby.markMessage(painter, 0, true))
        assertNotNull(lobby.messages[0].isMarked)
    }

    @Test fun addQuad() {
        assertFalse(lobby.addQuad(painter, -1, 0))
        assertFalse(lobby.addQuad(guesser, 0, 0))
        assertTrue(lobby.addQuad(painter, 0, 0))
        assertEquals(1, lobby.quads.size)
    }

    @Test fun removeQuad() {
        lobby.addQuad(painter, 0, 0)
        lobby.addQuad(painter, 1, 1)
        painter.isQuadsRemoved = false
        guesser.isQuadsRemoved = false

        assertFalse(lobby.removeQuad(painter, -1))
        assertFalse(lobby.removeQuad(guesser, 0))
        assertEquals(2, lobby.quads.size)
        assertFalse(painter.isQuadsRemoved)
        assertFalse(guesser.isQuadsRemoved)

        assertTrue(lobby.removeQuad(painter, 0))
        assertEquals(1, lobby.quads.size)
        assertTrue(painter.isQuadsRemoved)
        assertTrue(guesser.isQuadsRemoved)

        assertTrue(lobby.removeQuad(painter, 1))
        assertTrue(lobby.quads.isEmpty())
        assertTrue(lobby.removeQuad(painter, 2))
    }

    @Test fun removeQuads() {
        lobby.addQuad(painter, 0, 0)
        lobby.addQuad(painter, 1, 1)
        painter.isQuadsRemoved = false
        guesser.isQuadsRemoved = false

        assertFalse(lobby.removeQuads(guesser))
        assertFalse(lobby.quads.isEmpty())
        assertFalse(painter.isQuadsRemoved)
        assertFalse(guesser.isQuadsRemoved)

        assertTrue(lobby.removeQuads(painter))
        assertTrue(lobby.quads.isEmpty())
        assertTrue(painter.isQuadsRemoved)
        assertTrue(guesser.isQuadsRemoved)
    }

    @Test fun requestKeyword() {
        assertNull(lobby.requestKeyword(guesser))
    }
}