package bob.e2e.presentation.controller

import bob.e2e.domain.service.KeypadService
import bob.e2e.presentation.dto.KeypadDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/keypad")
class KeypadController(
    private val keypadService: KeypadService
) {
    @GetMapping("/generate")
    fun generateKeypad(): ResponseEntity<List<KeypadDto>> {
        val keypadDtoList = keypadService.generateKeypadImages()
        print(keypadDtoList)
        // 생성된 키패드 이미지를 JSON 형식으로 반환
        return ResponseEntity.ok(keypadDtoList)
    }
}
