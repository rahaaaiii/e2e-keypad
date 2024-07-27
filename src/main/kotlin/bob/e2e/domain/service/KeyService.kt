package bob.e2e.domain.service

import bob.e2e.domain.model.Key
import bob.e2e.presentation.dto.KeyResponseDto
import bob.e2e.presentation.dto.KeyRequestDto
import bob.e2e.data.repository.KeyJdbcRepository
import bob.e2e.data.repository.KeyRepository
import bob.e2e.domain.service.KeyService


class KeyService {
    private val keyJdbcRepository: KeyRepository
    ){
        var id=3

        fun getKey(id:String): KeyResponseDto{
            val key: Key = KeyJdbcRepository.selectBy(id)
            return KeyResponseDto.from(key)
        }
    }
    fun createCar(carRequestDto: KeyRequestDto): KeyResponseDto {
        val car: Key = carRequestDto.toEntity(id++.toString())
        KeyJdbcRepository.insert(car)
        return KeyResponseDto.from(car)
    }

    fun updateAirConditionerStatus(
        id: String,
        status: Boolean,
    ): KeyResponseDto {
        val car: Car = carJdbcRepository.selectBy(id)

        if (car.acStatus == status) throw IllegalStateException("acStatus is already $status.")

        Key.acStatus = status
        KeyJdbcRepository.update(car)
        return KeyResponseDto.from(car)
    }
}