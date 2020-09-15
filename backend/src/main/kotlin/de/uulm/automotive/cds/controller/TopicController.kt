package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Topic
import de.uulm.automotive.cds.models.CreateTopicDTO
import de.uulm.automotive.cds.repositories.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
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
     * @param topic Topic to be stored
     */
    @PostMapping
    fun postTopics(@RequestBody topic: CreateTopicDTO) {
        topicRepository.save(topic.toEntity())
    }
}