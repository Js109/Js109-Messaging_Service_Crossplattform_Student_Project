package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.Topic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TopicDTOTest {

    private val topic = Topic()

    init {
        topic.id = 1
        topic.title = "test title"
        topic.binding = ""
        topic.tags = arrayListOf<String>("test tag 1", "test tag 2")
        topic.description = "test description"
    }

    private fun getTopicEntity(): Topic {
        return topic
    }

    private fun getTopicDTO(
            updatedTitle: String? = null,
            updatedTags: MutableList<String>? = null,
            updatedDescription: String? = null
    ): TopicDTO {
        return TopicDTO(
                updatedTitle ?: topic.title,
                updatedTags ?: topic.tags,
                updatedDescription ?: topic.description
        )
    }

    @Test
    fun `topicDTO to topicEntity`() {
        val expected = getTopicEntity()
        val result = getTopicDTO().toEntity()

        assertEquals(result.binding, expected.binding)
        assertEquals(result.title, expected.title)
        assertEquals(result.tags, expected.tags)
        assertEquals(result.description, expected.description)

        assertFalse(result.disabled)
        assertNull(result.id)
    }

    @Test
    fun `topicEntity to topicDTO`() {
        val expected = getTopicDTO()
        val result = TopicDTO.toDTO(getTopicEntity())

        assertEquals(result.title, expected.title)
        assertEquals(result.tags, expected.tags)
        assertEquals(result.description, expected.description)
    }

    @Test
    fun `empty or blank title results in Error`() {
        val dtos = listOf(
                getTopicDTO(updatedTitle = ""),
                getTopicDTO(updatedTitle = "      ")
        )
        dtos.forEach {
            val errors = it.getErrors()

            assertNotNull(errors)

            assertNotNull(errors!!.titleError)
            assertTrue(errors.titleError!!.isNotBlank())

            assertNull(errors.descriptionError)
        }
    }

    @Test
    fun `empty or blank description results in Error`() {
        val dtos = listOf(
                getTopicDTO(updatedDescription = ""),
                getTopicDTO(updatedDescription = "      ")
        )
        dtos.forEach {
            val errors = it.getErrors()

            assertNotNull(errors)

            assertNotNull(errors!!.descriptionError)
            assertTrue(errors.descriptionError!!.isNotBlank())

            assertNull(errors.titleError)
        }
    }

    @Test
    fun `getErrors on valid dto results in null`() {
        val dto = getTopicDTO()

        assertNull(dto.getErrors())
    }
}