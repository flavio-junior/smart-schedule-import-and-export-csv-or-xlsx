package br.com.smart.schedule.service

import br.com.smart.schedule.entities.Person
import br.com.smart.schedule.repository.PersonRepository
import br.com.smart.schedule.utils.ConverterUtils.parseListObjects
import br.com.smart.schedule.utils.ConverterUtils.parseObject
import br.com.smart.schedule.vo.PersonRequestVO
import br.com.smart.schedule.vo.PersonVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScheduleService {

    @Autowired
    private lateinit var personRepository: PersonRepository

    fun getAllSchedules(): List<PersonVO> {
        return parseListObjects(origin = personRepository.findAll(), destination = PersonVO::class.java)
    }

    fun createNewSchedule(
        person: PersonRequestVO
    ): PersonVO {
        val personConverted = parseObject(origin = person, destination = Person::class.java)
        val personSaved = personRepository.save(personConverted)
        return parseObject(origin = personSaved, destination = PersonVO::class.java)
    }
}
