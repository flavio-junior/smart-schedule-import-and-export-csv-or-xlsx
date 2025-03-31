package br.com.smart.schedule.file.importer.contract

import br.com.smart.schedule.entities.Person
import java.io.InputStream

interface FileImporter {
    fun importFile(inputStream: InputStream?): List<Person?>?
}
