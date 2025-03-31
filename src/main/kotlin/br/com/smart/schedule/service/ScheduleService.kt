package br.com.smart.schedule.service

import br.com.smart.schedule.entities.Person
import br.com.smart.schedule.exception.FileStorageException
import br.com.smart.schedule.file.importer.contract.FileImporter
import br.com.smart.schedule.file.importer.factory.FileImporterFactory
import br.com.smart.schedule.repository.PersonRepository
import br.com.smart.schedule.utils.ConverterUtils.parseListObjects
import br.com.smart.schedule.utils.ConverterUtils.parseObject
import br.com.smart.schedule.vo.PersonRequestVO
import br.com.smart.schedule.vo.PersonResponseVO
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ScheduleService {

    @Autowired
    private lateinit var personRepository: PersonRepository

    @Autowired
    private lateinit var importer: FileImporterFactory

    fun getAllSchedules(): List<PersonResponseVO> {
        return parseListObjects(origin = personRepository.findAll(), destination = PersonResponseVO::class.java)
    }

    fun createNewSchedule(
        person: PersonRequestVO
    ): PersonResponseVO {
        val personConverted = parseObject(origin = person, destination = Person::class.java)
        val personSaved = personRepository.save(personConverted)
        return parseObject(origin = personSaved, destination = PersonResponseVO::class.java)
    }

    fun uploadSpreadsheet(
        file: MultipartFile
    ): List<PersonResponseVO>? {
        if (file.isEmpty) throw BadRequestException("Please set a Valid File!")
        try {
            file.inputStream.use { inputStream ->
                val filename = Optional.ofNullable(file.originalFilename)
                    .orElseThrow { BadRequestException("File name cannot be null") }
                val importer: FileImporter = importer.getImporter(filename)
                val entities: List<Person>? = importer.importFile(inputStream)?.stream()
                    ?.map { person ->
                        personRepository.save(parseObject(person, Person::class.java))
                    }
                    ?.toList()
                return entities?.stream()?.map { person: Person? ->
                    parseObject(person, PersonResponseVO::class.java)
                }?.toList()
            }
        } catch (e: Exception) {
            throw FileStorageException("Error processing the file!")
        }
    }
}
