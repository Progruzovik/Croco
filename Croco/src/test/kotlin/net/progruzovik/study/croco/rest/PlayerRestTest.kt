@file:Suppress("SpringKotlinAutowiredMembers")

package net.progruzovik.study.croco.rest

import com.fasterxml.jackson.databind.ObjectMapper
import junit.framework.TestCase.assertEquals
import net.progruzovik.study.croco.game.Role
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerRestTest {

    @Autowired lateinit var restTemplate: TestRestTemplate

    @Test fun getRole() {
        val result: ResponseEntity<String> = restTemplate.getForEntity("/api/player/role", String::class.java)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(ObjectMapper().writeValueAsString(hashMapOf("roleCode".to(Role.IDLER.ordinal))), result.body)
    }

    @Test fun postQueue() {
        assertEquals(HttpStatus.OK, restTemplate.postForEntity("/api/player/queue", null, Any::class.java).statusCode)
    }
}