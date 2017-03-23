package net.progruzovik.study.croco.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("")
class MainController {

    @GetMapping("/") fun getIndex(): String {
        return "Welcome to Croco!"
    }
}