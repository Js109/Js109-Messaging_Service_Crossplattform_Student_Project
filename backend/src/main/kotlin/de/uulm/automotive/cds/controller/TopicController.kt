package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Topic
import de.uulm.automotive.cds.models.dtos.TopicDTO
import de.uulm.automotive.cds.models.dtos.TopicDisableDTO
import de.uulm.automotive.cds.models.dtos.TopicUpdateDTO
import de.uulm.automotive.cds.models.errors.TopicBadRequestInfo
import de.uulm.automotive.cds.repositories.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@CrossOrigin("*")
@RestController
@RequestMapping("/topic")
/**
 * REST-Endpoint for reading available topics and storing new topics.
 */
class TopicController @Autowired constructor(private val topicRepository: TopicRepository) {
    /**
     * REST-Endpoint to get all available topics in the system.
     * See swagger definition of GET /topic for more details.
     *
     * @return List of Topic elements stored via jpa
     */
    @GetMapping
    fun getTopics(@RequestParam showDisabledTopics: Boolean = false): Iterable<Topic> {
        if (showDisabledTopics)
            return topicRepository.findAllByOrderByTitleAscIdAsc()
        return topicRepository.findAllByDisabledOrderByTitleAscIdAsc(false)
    }

    /**
     * REST-Endpoint for storing a new topic.
     * See swagger definition of POST /topic for more details.
     *
     * @param topicDto DTO of the topic
     * @return Response Entity Containing Error Object in case of an invalid DTO
     */
    @PostMapping
    fun postTopics(@RequestBody topicDto: TopicDTO): ResponseEntity<TopicBadRequestInfo> {
        val errors = topicDto.getErrors()
        if (errors != null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors)
        }
        if (topicRepository.findByTitle(topicDto.title) != null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(TopicBadRequestInfo(titleError = "Topic title has to be unique."))
        }

        topicRepository.save(TopicDTO.toEntity(topicDto).apply { binding = "topic/${title}"})
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    /**
     * REST-Endpoint to update the description of a topic
     *
     * @param topicId
     * @param topicUpdateDto
     * @return
     */
    @PatchMapping("/{topicId}")
    fun updateTopic(@PathVariable topicId: Long, @RequestBody topicUpdateDto: TopicUpdateDTO): ResponseEntity<Any>{
        var resp = ResponseEntity<Any>(HttpStatus.NOT_FOUND)

        val topic: Topic? = topicRepository.findById(topicId).orElse(null)
        topic?.let {
            if(topic.disabled){
                resp = ResponseEntity(HttpStatus.LOCKED)
            } else {
                it.description = topicUpdateDto.description
                resp = ResponseEntity.status(HttpStatus.OK).body(topicRepository.save(it))
            }
        }

        return resp
    }

    @PutMapping("/{id}")
    fun putPropertyEnable(@PathVariable id: Long, @RequestBody topicDisableDto: TopicDisableDTO) {
        val topic = topicRepository.findById(id)

        if (topic.isEmpty)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find property with id $id.")

        topic.ifPresent {
            it.disabled = topicDisableDto.disabled
            topicRepository.save(it)
        }
    }
}