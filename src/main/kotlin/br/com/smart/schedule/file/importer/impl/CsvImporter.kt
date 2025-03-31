package br.com.smart.schedule.file.importer.impl

import br.com.smart.schedule.entities.Person
import br.com.smart.schedule.file.importer.contract.FileImporter
import br.com.smart.schedule.utils.GENDER
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Component
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@Component
class CsvImporter : FileImporter {

    override fun importFile(inputStream: InputStream?): List<Person?>? {
        val format = CSVFormat.Builder.create()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .build()
        val records: Iterable<CSVRecord> = format.parse(inputStream?.let { InputStreamReader(it) })
        return parseRecordsToPersons(records)
    }

    private fun parseRecordsToPersons(records: Iterable<CSVRecord>): List<Person> {
        val people: MutableList<Person> = ArrayList()

        for (record in records) {
            val person = Person()
            person.name = record["Name"]
            person.surname = record["Surname"]
            person.gender = try {
                GENDER.valueOf(record["Gender"].uppercase(Locale.getDefault()))
            } catch (e: IllegalArgumentException) {
                null
            }
            person.email = record["Email"]
            try {
                val inputDate = record["Date"].trim()
                person.date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (e: Exception) {
                person.date = null
            }
            people.add(person)
        }
        return people
    }
}
