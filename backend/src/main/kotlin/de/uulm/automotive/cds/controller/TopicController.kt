package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.Topic
import de.uulm.automotive.cds.models.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
/**
 * REST-Point for reading available topics and storing new topics.
 */
class TopicController @Autowired constructor(private val topicRepository: TopicRepository) {
    /**
     * TODO
     *
     * @return TODO
     */
    @GetMapping("/topic")
    fun getTopics(): Iterable<Topic> {
        return topicRepository.findAll()
    }

    /**
     * TODO
     *
     * @param topic
     */
    @PostMapping("/topic")
    fun postTopics(@RequestBody topic: Topic) {
        topicRepository.save(topic)
    }
}