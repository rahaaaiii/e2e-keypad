package bob.e2e.data.repository

import bob.e2e.domain.model.Key
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository

class KeyJdbcRepository {
}


@Repository
class CarJdbcRepository(
    private val jdbcOperations: NamedParameterJdbcOperations,
) : KeyRepository {
    override fun insert(car: Key) {
        jdbcOperations.update(
            "insert into car (id, ac_status, model) values(:id, :acStatus, :model)",
            MapSqlParameterSource()
                .addValue("acStatus", car.acStatus)
                .addValue("model", car.model)
                .addValue("id", car.id),
        )
    }

    override fun selectBy(id: String): Key {
        return jdbcOperations.queryForObject(
            "select * from car where id = :id",
            mapOf("id" to id),
        ) { rs, _ ->
            Key(
                id = rs.getString("id"),
                acStatus = rs.getBoolean("ac_status"),
                model = rs.getString("model"),
            )
        }!!
    }

    override fun update(key: Key) {
        jdbcOperations.update(
            "update car set ac_status = :acStatus, model = :model where id = :id",
            MapSqlParameterSource()
                .addValue("acStatus", key.acStatus)
                .addValue("model", key.model)
                .addValue("id", key.id),
        )
    }

    @Suppress("ktlint:standard:function-naming")
    fun jdbc에는_있고_redis에는_없는_메서드() {}
}
