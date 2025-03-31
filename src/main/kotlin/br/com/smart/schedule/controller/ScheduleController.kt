package br.com.smart.schedule.controller

import br.com.smart.schedule.file.exporter.impl.MediaTypes
import br.com.smart.schedule.service.ScheduleService
import br.com.smart.schedule.vo.PersonRequestVO
import br.com.smart.schedule.vo.PersonResponseVO
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/smart/schedule/v1")
class ScheduleController {

    @Autowired
    private lateinit var scheduleService: ScheduleService

    @GetMapping
    fun getAllSchedules(): ResponseEntity<List<PersonResponseVO>> {
        return ResponseEntity.ok(scheduleService.getAllSchedules())
    }

    @PostMapping
    fun createNewSchedule(
        @RequestBody personVO: PersonRequestVO
    ): ResponseEntity<PersonResponseVO> {
        val scheduleSaved: PersonResponseVO =
            scheduleService.createNewSchedule(person = personVO)
        val uri: URI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(scheduleSaved.id).toUri()
        return ResponseEntity.created(uri).body(scheduleSaved)
    }

    @PostMapping(
        value = ["/upload/spreadsheet"]
    )
    fun uploadSpreadsheet(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<List<PersonResponseVO>> {
        return ResponseEntity.ok(scheduleService.uploadSpreadsheet(file = file))
    }

    @GetMapping(
        value = ["/export/spreadsheet"],
        produces = [MediaTypes.APPLICATION_XLSX_VALUE, MediaTypes.APPLICATION_CSV_VALUE]
    )
    fun exportSpreadsheet(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "12") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String?,
        request: HttpServletRequest
    ): ResponseEntity<Resource> {
        val sortDirection = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"))
        val acceptHeader = request.getHeader(HttpHeaders.ACCEPT)
        val file: Resource? = scheduleService.exportSpreadsheet(pageable, acceptHeader)
        val contentType = acceptHeader ?: "application/octet-stream"
        val fileExtension =
            if (acceptHeader.equals(MediaTypes.APPLICATION_XLSX_VALUE, ignoreCase = true)) ".xlsx" else ".csv"
        val filename = "people_exported$fileExtension"
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
            .body(file)
    }
}
