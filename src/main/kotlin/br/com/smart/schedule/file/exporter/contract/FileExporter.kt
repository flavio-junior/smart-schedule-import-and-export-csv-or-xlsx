package br.com.smart.schedule.file.exporter.contract

import br.com.smart.schedule.entities.Person
import org.springframework.core.io.Resource

interface FileExporter {
    fun exportFile(people: List<Person?>?): Resource?
}
