package ru.funbox.testtask.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import ru.funbox.testtask.entity.VisitedLinksWrapper

@Repository
class VisitedLinksRepository(val redisTemplate: RedisTemplate<String, Any>) {

    fun save(visitedLinks: VisitedLinksWrapper) {
        visitedLinks.links.forEach { redisTemplate.opsForZSet().add(getKey(), it, visitedLinks.id.toDouble()) }
    }

    fun findBetween(from: Long, to: Long): MutableSet<Any> {
        return redisTemplate.opsForZSet().rangeByScore(getKey(), from.toDouble(), to.toDouble())!!
    }

    fun delete() {
        redisTemplate.delete(getKey())
    }

    fun getKey(): String {
        return "visited_links"
    }

}