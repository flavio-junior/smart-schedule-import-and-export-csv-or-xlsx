package br.com.smart.schedule.controller

import br.com.smart.schedule.service.ScheduleService
import br.com.smart.schedule.vo.PersonRequestVO
import br.com.smart.schedule.vo.PersonVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/smart/schedule/v1")
class ScheduleController {

    @Autowired
    private lateinit var scheduleService: ScheduleService

    @GetMapping
    fun getAllSchedules(): ResponseEntity<List<PersonVO>> {
        return ResponseEntity.ok(scheduleService.getAllSchedules())
    }

    @PostMapping
    fun createNewSchedule(
        @RequestBody personVO: PersonRequestVO
    ): ResponseEntity<PersonVO> {
        val scheduleSaved: PersonVO =
            scheduleService.createNewSchedule(person = personVO)
        val uri: URI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(scheduleSaved.id).toUri()
        return ResponseEntity.created(uri).body(scheduleSaved)
    }
}
