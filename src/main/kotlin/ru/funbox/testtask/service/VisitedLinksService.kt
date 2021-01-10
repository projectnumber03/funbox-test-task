package ru.funbox.testtask.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.funbox.testtask.entity.VisitedLinksWrapper
import ru.funbox.testtask.repository.VisitedLinksRepository
import java.net.URL

@Service
class VisitedLinksService(private val visitedLinksRepository: VisitedLinksRepository) {

    private val log = KotlinLogging.logger {}

    fun findBetween(from: Long, to: Long): Set<String> {
        return visitedLinksRepository.findBetween(from, to)
                .map { if (it.toString().startsWith("http")) URL(it.toString()).host else URL("http://$it").host }
                .toSet()
    }

    fun save(links: VisitedLinksWrapper) {
        log.debug { "saving links with id ${links.id}..." }
        visitedLinksRepository.save(links)
    }

    fun delete() {
        visitedLinksRepository.delete()
    }

}