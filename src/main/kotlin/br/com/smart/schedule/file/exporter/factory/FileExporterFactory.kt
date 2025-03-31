package br.com.smart.schedule.file.exporter.factory

import br.com.smart.schedule.file.exporter.contract.FileExporter
import br.com.smart.schedule.file.exporter.impl.CsvExporter
import br.com.smart.schedule.file.exporter.impl.MediaTypes
import br.com.smart.schedule.file.exporter.impl.XlsxExporter
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class FileExporterFactory {

    @Autowired
    private lateinit var context: ApplicationContext

    @Throws(Exception::class)
    fun getExporter(acceptHeader: String): FileExporter {
        return if (acceptHeader.equals(MediaTypes.APPLICATION_XLSX_VALUE, ignoreCase = true)) {
            context.getBean(XlsxExporter::class.java)
        } else if (acceptHeader.equals(MediaTypes.APPLICATION_CSV_VALUE, ignoreCase = true)) {
            context.getBean(CsvExporter::class.java)
        } else {
            throw BadRequestException("Invalid File Format!")
        }
    }
}
