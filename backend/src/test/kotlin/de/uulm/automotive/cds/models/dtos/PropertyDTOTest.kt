package de.uulm.automotive.cds.models.dtos

import de.uulm.automotive.cds.entities.Property
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PropertyDTOTest {

    private val property = Property()

    init {
        property.id = 1
        property.name = "test name"
        property.binding = ""
        property.disabled = false
    }

    private fun getPropertyEntity(): Property {
        return property
    }

    private fun getPropertyDTO(
            updatedName: String? = null
    ): PropertyDTO {
        return PropertyDTO(
                updatedName ?: property.name
        )
    }

    @Test
    fun `propertyDTO to propertyEntity`() {
        val expected = getPropertyEntity()
        val result = getPropertyDTO().toEntity()

        assertEquals(result.name, expected.name)
        assertEquals(result.binding, expected.binding)

        assertFalse(result.disabled)
        assertNull(result.id)
    }

    @Test
    fun `propertyEntity to propertyDTO`() {
        val expected = getPropertyDTO()
        val result = PropertyDTO.toDTO(getPropertyEntity())

        assertEquals(result.name, expected.name)
    }

    @Test
    fun `empty or blank name results in Error`() {
        val dtos = listOf(
                getPropertyDTO(updatedName = ""),
                getPropertyDTO(updatedName = "     ")
        )

        dtos.forEach {
            val errors = it.getErrors()

            assertNotNull(errors)

            assertNotNull(errors!!.nameError)
            assertTrue(errors.nameError!!.isNotBlank())
        }
    }

    @Test
    fun `getErrors on valid dto results in null`() {
        val dto = getPropertyDTO()

        assertNull(dto.getErrors())
    }
}