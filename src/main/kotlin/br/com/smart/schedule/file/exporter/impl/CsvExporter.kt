package br.com.smart.schedule.file.exporter.impl

import br.com.smart.schedule.entities.Person
import br.com.smart.schedule.file.exporter.contract.FileExporter
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

@Component
class CsvExporter : FileExporter {

    override fun exportFile(people: List<Person?>?): Resource? {
        val outputStream = ByteArrayOutputStream()
        val writer = OutputStreamWriter(outputStream, StandardCharsets.UTF_8)
        val csvFormat = CSVFormat.Builder.create()
            .setHeader("Id", "Name", "Surname", "Gender", "Email", "Date")
            .setSkipHeaderRecord(false)
            .build()
        CSVPrinter(writer, csvFormat).use { csvPrinter ->
            if (people != null) {
                for (person in people) {
                    csvPrinter.printRecord(
                        person?.id,
                        person?.name,
                        person?.surname,
                        person?.gender,
                        person?.name,
                        person?.date
                    )
                }
            }
        }
        return ByteArrayResource(outputStream.toByteArray())
    }
}
