package bob.e2e.data.repository
import bob.e2e.domain.model.Key
interface KeyRepository {
    fun insert(key: Key)

    fun selectBy(id: String): Key

    fun update(key: Key)
}
