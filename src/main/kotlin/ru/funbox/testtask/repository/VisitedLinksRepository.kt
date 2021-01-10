package ru.funbox.testtask.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import ru.funbox.testtask.entity.VisitedLinksWrapper

@Repository
class VisitedLinksRepository(val redisTemplate: RedisTemplate<String, Any>) {

    fun save(visitedLinks: VisitedLinksWrapper) {
        visitedLinks.links.forEach { redisTemplate.opsForZSet().add("visited_links", it, visitedLinks.id.toDouble()) }
    }

    fun findBetween(from: Long, to: Long): MutableSet<Any> {
        return redisTemplate.opsForZSet().rangeByScore("visited_links", from.toDouble(), to.toDouble())!!
    }

    fun delete() {
        redisTemplate.delete("visited_links")
    }

}