package bob.e2e.presentation.controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import bob.e2e.presentation.dto.KeyResponseDto
import bob.e2e.domain.service.KeyService

@RestController
class KeypadController {
    @GetMapping("key/{id}")
    fun getKey(
        @PathVariable id: String,
    ): KeyResponseDto {
        return KeyService.getKey(id)
    }

}