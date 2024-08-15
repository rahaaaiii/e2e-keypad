package bob.e2e.domain.service

import bob.e2e.presentation.dto.KeypadDto
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.Color


@Service
class KeypadService {

    private val log = LoggerFactory.getLogger(KeypadService::class.java)
    private val keys = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private val globalKeyMappingTable: MutableMap<String, List<String>> = mutableMapOf()
    private var combinedBase64: String = ""

    private fun getBase64FromImage(image: BufferedImage): String {
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            ImageIO.write(image, "png", byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(imageBytes)
        } catch (e: IOException) {
            log.error("Failed to encode image to Base64", e)
            ""
        }
    }

    private fun loadImage(classPathResource: String): BufferedImage? {
        return try {
            val keyImage: File = ClassPathResource(classPathResource).file
            ImageIO.read(keyImage) // 이미지 읽기
        } catch (e: IOException) {
            log.error("Failed to read image file: $classPathResource", e)
            null
        }
    }

    fun generateKeypadImages(): ArrayList<KeypadDto> {
        val keypadDtoList = ArrayList<KeypadDto>(12) // 10개의 숫자 + 2개의 블랭크
        val randomStringList = generateRandomStrings(12) // 각 키에 대해 랜덤 문자열 생성

        val hashedKeyList = hashKeysWithSha256(keys.mapIndexed { index, key -> key + randomStringList[index] })

        for (i in keys.indices) { // 0-9 숫자와 블랭크 처리
            val bufferedImage = if (keys[i] == "blank") {
                loadImage("keypad-img/_blank.png")
            } else {
                loadImage("keypad-img/_${keys[i]}.png")
            } ?: continue

            val dto = KeypadDto(
                hashedValue = hashedKeyList[i],
                b64String = getBase64FromImage(bufferedImage)
            )
            keypadDtoList.add(dto)
        }

        // 키패드 버튼의 순서를 무작위로 섞음
        keypadDtoList.shuffle()

        // 셔플된 이미지를 하나의 이미지로 결합
        val combinedImage = combineImages(keypadDtoList.map { loadImageFromBase64(it.b64String) })
        combinedBase64 = getBase64FromImage(combinedImage)

        // 결합된 이미지를 최종 리스트에 추가
        return ArrayList(keypadDtoList.map { it.copy(b64String = combinedBase64) })
    }

    private fun generateRandomStrings(count: Int): List<String> {
        val random = Random()
        return List(count) {
            val randomString = ByteArray(16)
            random.nextBytes(randomString)
            Base64.getEncoder().encodeToString(randomString)
        }
    }


    private fun combineImages(images: List<BufferedImage?>): BufferedImage {
        val cols = 4 // 열 개수
        val rows = (images.size + cols - 1) / cols // 행 개수 계산
        val imageWidth = images[0]?.width ?: 0
        val imageHeight = images[0]?.height ?: 0

        val combinedImage = BufferedImage(imageWidth * cols, imageHeight * rows, BufferedImage.TYPE_INT_ARGB)
        val g: Graphics2D = combinedImage.createGraphics()
        g.color = Color.WHITE
        g.fillRect(0, 0, combinedImage.width, combinedImage.height)

        for ((index, image) in images.withIndex()) {
            val x = (index % cols) * imageWidth
            val y = (index / cols) * imageHeight
            g.drawImage(image, x, y, null)
        }
        g.dispose()
        return combinedImage
    }

    private fun loadImageFromBase64(base64: String): BufferedImage? {
        return try {
            val imageBytes = Base64.getDecoder().decode(base64)
            val byteArrayInputStream = ByteArrayInputStream(imageBytes)
            ImageIO.read(byteArrayInputStream)
        } catch (e: IOException) {
            log.error("Failed to load image from Base64", e)
            null
        }
    }

    private fun hashKeysWithSha256(input: List<String>): List<String> {
        val hashArray = mutableListOf<String>()

        try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.reset()
            for (hashNumber in input) {
                digest.update(hashNumber.toByteArray(Charsets.UTF_8))
                hashArray.add(String.format("%064x", BigInteger(1, digest.digest())))
            }
        } catch (e: Exception) {
            log.error("Error while hashing keys with SHA-256", e)
        }

        return hashArray
    }
}
