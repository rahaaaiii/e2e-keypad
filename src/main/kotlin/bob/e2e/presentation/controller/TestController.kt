package bob.e2e.presentation.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/test/a")
    fun testA(): String{
        return "Hello Spring"
    }
}
