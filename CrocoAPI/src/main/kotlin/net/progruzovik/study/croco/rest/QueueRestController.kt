package net.progruzovik.study.croco.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/queue")
class QueueRestController {

    @GetMapping("")
    fun getQueue(): String {
        return "Hello world!"
    }
}