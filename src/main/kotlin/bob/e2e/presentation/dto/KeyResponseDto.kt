package bob.e2e.presentation.dto

import bob.e2e.domain.model.Key
import java.time.Instant

data class KeyResponseDto(
    val keypadId: String,
    var timestamp: Instant,
    val hash: String,
){
    companion object{
        fun from(key : Key) =
            KeyResponseDto(
                keypadId = key.keypadId,
                timestamp = Instant.now(),
                hash= key.hash
            )
    }
}
