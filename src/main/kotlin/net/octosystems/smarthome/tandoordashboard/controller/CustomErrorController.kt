package net.octosystems.smarthome.tandoordashboard.controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/error")
class CustomErrorController:ErrorController {
}