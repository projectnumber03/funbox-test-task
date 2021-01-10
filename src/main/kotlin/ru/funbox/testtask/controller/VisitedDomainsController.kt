package ru.funbox.testtask.controller

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.funbox.testtask.service.VisitedLinksService
import java.util.*
import kotlin.collections.HashMap

@RestController
class VisitedDomainsController(val visitedLinksService: VisitedLinksService) {

    private val log = KotlinLogging.logger {}

    @GetMapping("/visited_domains")
    fun visitedDomains(@RequestParam from: Long, @RequestParam to: Long): HashMap<String, Any> {
        val links: Set<String> = visitedLinksService.findBetween(from, to)
        val body: HashMap<String, Any> = HashMap()
        body["domains"] = links
        body["status"] = HttpStatus.OK
        return body
    }

    @ExceptionHandler(Throwable::class)
    fun handleException(ex: Throwable): HashMap<String, Any> {
        log.error { ex.localizedMessage }
        val body: HashMap<String, Any> = HashMap()
        body["domains"] = Collections.EMPTY_SET
        body["status"] = ex.localizedMessage
        return body
    }

}