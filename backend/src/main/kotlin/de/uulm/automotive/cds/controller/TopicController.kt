package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Topic
import de.uulm.automotive.cds.models.dtos.TopicDTO
import de.uulm.automotive.cds.models.errors.TopicBadRequestInfo
import de.uulm.automotive.cds.repositories.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin("*")
@RestController
@RequestMapping("/topic")
/**
 * REST-Point for reading available topics and storing new topics.
 */
class TopicController @Autowired constructor(private val topicRepository: TopicRepository) {
    /**
     * REST-point to get all available topics in the system.
     * See swagger definition of GET /topic for more details.
     *
     * @return List of Topic elements stored via jpa
     */
    @GetMapping
    fun getTopics(): Iterable<Topic> {
        return topicRepository.findAll()
    }

    /**
     * REST-point for storing a new topic.
     * See swagger definition of POST /topic for more details.
     *
     * @param topicDto DTO object of the topic
     * @return Response Entity Containing Error Object in case of an invalid DTO
     */
    @PostMapping
    fun postTopics(@RequestBody topicDto: TopicDTO): ResponseEntity<TopicBadRequestInfo> {
        val errors = topicDto.getErrors()
        if (errors != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
        }

        topicRepository.save(topicDto.toEntity())
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}