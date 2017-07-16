package net.progruzovik.croco.game

import junit.framework.TestCase.*
import net.progruzovik.croco.data.Role
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class QueueTest {

    @Autowired lateinit var queue: Queue

    @Test fun addPlayer() {
        val painter = MockPlayer("1", "Tom")
        val guesser = MockPlayer("2", "Steve")
        assertNull(queue.queuedPlayer)
        assertNull(painter.lobby)
        assertEquals(Role.IDLER, painter.role)
        assertNull(guesser.lobby)
        assertEquals(Role.IDLER, guesser.role)

        assertTrue(queue.addPlayer(painter))
        assertEquals(painter, queue.queuedPlayer)
        assertEquals(Role.QUEUED, painter.role)
        assertTrue(queue.addPlayer(guesser))

        assertNull(queue.queuedPlayer)
        assertNotNull(painter.lobby)
        assertEquals(Role.PAINTER, painter.role)
        assertNotNull(guesser.lobby)
        assertEquals(Role.GUESSER, guesser.role)
    }

    @Test fun removePlayer() {
        val player = MockPlayer("1", "Carl")
        assertNull(queue.queuedPlayer)
        assertFalse(queue.removePlayer(player))

        assertTrue(queue.addPlayer(player))
        assertEquals(player, queue.queuedPlayer)
        assertEquals(Role.QUEUED, player.role)

        assertTrue(queue.removePlayer(player))
        assertNull(queue.queuedPlayer)
        assertEquals(Role.IDLER, player.role)
    }
}