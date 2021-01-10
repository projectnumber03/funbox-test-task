package ru.funbox.testtask.controller

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.funbox.testtask.entity.VisitedLinksWrapper
import ru.funbox.testtask.service.VisitedLinksService
import java.util.*

@RestController
class VisitedLinksController(val visitedLinksService: VisitedLinksService) {

    private val log = KotlinLogging.logger {}

    @PostMapping("/visited_links")
    fun visitedLinks(@RequestBody links: VisitedLinksWrapper): HashMap<String, HttpStatus> {
        visitedLinksService.save(links)
        val body: HashMap<String, HttpStatus> = HashMap()
        body["status"] = HttpStatus.OK
        return body
    }

    @ExceptionHandler(Throwable::class)
    fun handleException(ex: Throwable): HashMap<String, Any> {
        log.error { ex.localizedMessage }
        val body: HashMap<String, Any> = HashMap()
        body["status"] = ex.localizedMessage
        return body
    }

}