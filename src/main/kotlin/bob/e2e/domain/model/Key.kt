package bob.e2e.domain.model

import java.time.Instant

data class Key(
    val keypadId: String,
    var timestamp: Instant,
    val hash: String
)
