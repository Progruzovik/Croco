package net.progruzovik.study.croco.game

import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QueueServiceTest {

    @Autowired lateinit var queueService: QueueService

    @Test fun addPlayer() {
        val painter = MockPlayer("1", "Tom")
        val guesser = MockPlayer("2", "Steve")
        assertNull(painter.lobby)
        assertEquals(Role.IDLER, painter.role)
        assertNull(guesser.lobby)
        assertEquals(Role.IDLER, guesser.role)

        assertTrue(queueService.addPlayer(painter))
        assertEquals(Role.QUEUED, painter.role)
        assertTrue(queueService.addPlayer(guesser))

        assertNotNull(painter.lobby)
        assertEquals(Role.PAINTER, painter.role)
        assertNotNull(guesser.lobby)
        assertEquals(Role.GUESSER, guesser.role)
    }

    @Test fun removePlayer() {
        val player = MockPlayer("1", "Carl")
        assertFalse(queueService.removePlayer(player))
        queueService.addPlayer(player)
        assertEquals(Role.QUEUED, player.role)
        assertTrue(queueService.removePlayer(player))
        assertEquals(Role.IDLER, player.role)
    }
}