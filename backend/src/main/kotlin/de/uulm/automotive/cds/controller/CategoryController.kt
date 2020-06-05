package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.Category
import de.uulm.automotive.cds.models.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryController @Autowired constructor(private val categoryRepository: CategoryRepository) {
    @GetMapping("/categories")
    fun getCategories(): Iterable<Category> {
        return categoryRepository.findAll()
    }

    @PostMapping("/categories")
    fun postCategories(): String {
        val category = Category()
        category.bindings.add("test/moar")
        category.bindings.add("test/moar/categories")
        category.description = "Testdescription für die Testkategorie"
        category.tags.add("test")
        category.tags.add("testing")
        categoryRepository.save(category)
        return "successfully saved"
    }
}