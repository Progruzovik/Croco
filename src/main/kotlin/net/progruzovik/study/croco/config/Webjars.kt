package net.progruzovik.study.croco.config

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.webjars.RequireJS

@Controller
class Webjars {

    @GetMapping(value = "/js/webjars.js", produces = arrayOf("application/javascript"))
    @ResponseBody fun getWebjarsJs(): String {
        return RequireJS.getSetupJavaScript("/webjars/")
    }
}