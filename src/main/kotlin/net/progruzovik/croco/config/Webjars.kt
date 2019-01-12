package net.progruzovik.croco.config

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.webjars.RequireJS

@RestController
class Webjars {

    @GetMapping(value = "/js/webjars.js", produces = arrayOf("application/javascript"))
    fun getWebjarsJs(): String {
        return RequireJS.getSetupJavaScript("/webjars/")
            .replace("axios/0.16.1/index", "axios/0.16.1/dist/axios")
    }
}
