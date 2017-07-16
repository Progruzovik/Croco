@file:Suppress("SpringKotlinAutowiredMembers")

package net.progruzovik.croco.api

import com.fasterxml.jackson.databind.ObjectMapper
import junit.framework.TestCase.assertEquals
import net.progruzovik.croco.data.Role
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PlayerApiTest {

    @Autowired lateinit var restTemplate: TestRestTemplate

    @Test fun getRole() {
        val result: ResponseEntity<String> = restTemplate.getForEntity("/api/player/role", String::class.java)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(ObjectMapper().writeValueAsString(hashMapOf("roleCode".to(Role.IDLER.ordinal))), result.body)
    }
}