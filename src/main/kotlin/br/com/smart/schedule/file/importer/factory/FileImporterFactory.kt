package br.com.smart.schedule.file.importer.factory

import br.com.smart.schedule.file.importer.contract.FileImporter
import br.com.smart.schedule.file.importer.impl.CsvImporter
import br.com.smart.schedule.file.importer.impl.XlsxImporter
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class FileImporterFactory {

    @Autowired
    private lateinit var context: ApplicationContext

    fun getImporter(fileName: String): FileImporter {
        return if (fileName.endsWith(suffix = ".xlsx")) {
            context.getBean(XlsxImporter::class.java)
        } else if (fileName.endsWith(suffix = ".csv")) {
            context.getBean(CsvImporter::class.java)
        } else {
            throw BadRequestException("Invalid File Format!")
        }
    }
}
