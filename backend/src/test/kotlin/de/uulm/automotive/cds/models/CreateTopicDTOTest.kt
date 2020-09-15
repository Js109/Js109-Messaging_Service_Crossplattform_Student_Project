package de.uulm.automotive.cds.models

import de.uulm.automotive.cds.entities.Topic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CreateTopicDTOTest {

    private val topic = Topic()

    init {
        topic.id = 1
        topic.title = "test title"
        topic.binding = "test binding"
        topic.tags = arrayListOf<String>("test tag 1", "test tag 2")
        topic.description = "test description"
    }

    private fun getTopicEntity(): Topic {
        return topic
    }

    private fun getTopicDTO(updatedBinding: String? = null,
                            updatedTitle: String? = null,
                            updatedTags: MutableList<String>? = null,
                            updatedDescription: String? = null): CreateTopicDTO {
        return CreateTopicDTO(updatedBinding ?: topic.binding,
                updatedTitle ?: topic.title,
                updatedTags ?: topic.tags,
                updatedDescription ?: topic.description)
    }

    @Test
    fun `topicDTO to topicEntity`() {
        val expected = getTopicEntity()
        val result = getTopicDTO().toEntity()

        assertEquals(result.binding, expected.binding)
        assertEquals(result.title, expected.title)
        assertEquals(result.tags, expected.tags)
        assertEquals(result.description, expected.description)
        assertEquals(result.isDeleted, false)

        assertNull(result.id)
    }

    @Test
    fun `topicEntity to topicDTO`() {
        val expected = getTopicDTO()
        val result = CreateTopicDTO.toDTO(getTopicEntity())

        assertEquals(result.binding, expected.binding)
        assertEquals(result.title, expected.title)
        assertEquals(result.tags, expected.tags)
        assertEquals(result.description, expected.description)
    }

    @Test
    fun `valid dto results in valid`() {
        val topicDTO = getTopicDTO()

        assertTrue(topicDTO.isValid())
    }

    @Test
    fun `empty title results in not valid`() {
        val topicDTO = getTopicDTO(updatedTitle = "")

        assertFalse(topicDTO.isValid())
    }

    @Test
    fun `whitespace only title results in not valid`() {
        val topicDTO = getTopicDTO(updatedTitle = "       ")

        assertFalse(topicDTO.isValid())
    }

    @Test
    fun `empty binding results in not valid`() {
        val topicDTO = getTopicDTO(updatedBinding = "")

        assertFalse(topicDTO.isValid())
    }

    @Test
    fun `whitespace only binding results in not valid`() {
        val topicDTO = getTopicDTO(updatedBinding = "       ")

        assertFalse(topicDTO.isValid())
    }

    @Test
    fun `empty description results in not valid`() {
        val topicDTO = getTopicDTO(updatedDescription = "")

        assertFalse(topicDTO.isValid())
    }

    @Test
    fun `whitespace only description results in not valid`() {
        val topicDTO = getTopicDTO(updatedDescription = "       ")

        assertFalse(topicDTO.isValid())
    }
}