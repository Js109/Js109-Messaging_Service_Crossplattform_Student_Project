package de.uulm.automotive.cds.models

import org.modelmapper.ModelMapper
import java.net.URL

/**
 * Basic interface for the companion object inside DTO objects
 *
 */
open class EntityConverter<T, R>(
        private val entityClass: Class<T>,
        private val dtoClass: Class<R>
) where T : Entity, R : DTO<T> {

    companion object {
        val mapper: ModelMapper = ModelMapper()

        init {
            mapper.addConverter<String, URL> { ctx -> URL(completeURL(ctx.source)) }
        }

        val regexUrl: String = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=,!]*)"
        val regexUrlWithoutProtocol: Regex = Regex("(www\\.)?$regexUrl")
        val regexUrlHttp: Regex = Regex("http?:\\/\\/(www\\.)?$regexUrl")
        val regexUrlHttps: Regex = Regex("https?:\\/\\/(www\\.)?$regexUrl")

        val regexHexColor: Regex = Regex("#[A-Fa-f0-9]{6}")

        /**
         * Checks if the given url is valid or incomplete. If it is incomplete the protocol gets
         * prepended.
         *
         * @param url string
         * @return url as string if a valid url can be created, returns null if not
         */
        fun completeURL(url: String): String? {
            if (url.contains(regexUrlWithoutProtocol)) {
                if (url.matches(regexUrlWithoutProtocol)) {
                    return "https://" + url
                } else if (url.matches(regexUrlHttp) || url.matches(regexUrlHttps)) {
                    return url
                }
            }
            return null
        }
    }

    fun toEntity(dto: R): T =
            mapper.map(dto, entityClass)

    /**
     * Maps the Message entity to the corresponding DTO object
     *
     * @param entity entity
     * @return Mapped DTO
     */
    fun toDTO(entity: T): R =
            mapper.map(entity, dtoClass)

    /**
     *
     */
    fun toNullableDTO(entity: T?): R? =
            when (entity) {
                null -> null
                else -> toDTO(entity)
            }

}