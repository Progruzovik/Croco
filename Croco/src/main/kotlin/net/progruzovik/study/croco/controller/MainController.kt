package net.progruzovik.study.croco.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("")
class MainController {

    @GetMapping("/")
    fun getIndex(): ModelAndView {
        return ModelAndView("shared/layout")
    }
}