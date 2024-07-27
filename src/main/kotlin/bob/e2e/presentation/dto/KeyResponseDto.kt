package bob.e2e.presentation.dto

import bob.e2e.domain.model.Key
data class KeyResponseDto(
    val id: String,
    var acStatus: Boolean,
    val model: String,
) {
    companion object {
        fun from(key: Key) =
            KeyResponseDto(
                id = key.id,
                acStatus = key.acStatus,
                model = key.model,
            )
    }
}
