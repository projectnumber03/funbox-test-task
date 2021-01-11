package ru.funbox.testtask

import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import ru.funbox.testtask.entity.VisitedLinksWrapper
import ru.funbox.testtask.repository.VisitedLinksRepository
import ru.funbox.testtask.service.VisitedLinksService


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestTaskApplicationTests(@Autowired val restTemplate: TestRestTemplate, @Autowired var service: VisitedLinksService) {

    @SpykBean
    lateinit var repository: VisitedLinksRepository

    @LocalServerPort
    private val port = 0

    val links = setOf("https://ya.ru", "https://ya.ru?q=123", "funbox.ru", "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor")
    val domains = setOf("funbox.ru", "ya.ru", "stackoverflow.com")
    val currentTime = System.currentTimeMillis()

    @BeforeEach
    fun before() {
        every { repository.getKey() } returns "visited_links_test"
    }

    @AfterEach
    fun after() {
        service.delete()
    }

    @Test
    fun postLinksAndShouldReturnCorrectDomains() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity<VisitedLinksWrapper>(VisitedLinksWrapper(currentTime, ArrayList(links)), headers)
        val result = restTemplate.postForEntity("http://localhost:$port/visited_links", request, HashMap::class.java)
        Assertions.assertEquals(HttpStatus.OK.reasonPhrase, result.body!!["status"])
        Assertions.assertEquals(this.domains, service.findBetween(currentTime, currentTime + 10))
    }

    @Test
    fun shouldGetCorrectDomains() {
        service.save(VisitedLinksWrapper(currentTime, ArrayList(links)))
        val result = restTemplate.getForEntity("http://localhost:$port/visited_domains?from=$currentTime&to=${currentTime + 10}", HashMap::class.java)
        Assertions.assertEquals(HttpStatus.OK.reasonPhrase, result.body!!["status"])
        Assertions.assertEquals(this.domains.toList(), result.body!!["domains"])
    }

}
