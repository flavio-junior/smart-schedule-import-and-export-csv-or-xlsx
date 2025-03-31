package br.com.smart.schedule.file.exporter.impl

import br.com.smart.schedule.entities.Person
import br.com.smart.schedule.file.exporter.contract.FileExporter
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class XlsxExporter : FileExporter {

    override fun exportFile(people: List<Person?>?): Resource? {
        XSSFWorkbook().use { workbook ->
            val sheet: Sheet = workbook.createSheet("Schedule of Day")
            val headerRow = sheet.createRow(0)
            val headers = arrayOf("Id", "Name", "Surname", "Gender", "Email", "Date")
            for (i in headers.indices) {
                val cell = headerRow.createCell(i)
                cell.setCellValue(headers[i])
                cell.cellStyle = createHeaderCellStyle(workbook)
            }

            var rowIndex = 1
            if (people != null) {
                for (person in people) {
                    val row = sheet.createRow(rowIndex++)
                    row.createCell(0).setCellValue(person?.id.toString())
                    row.createCell(1).setCellValue(person?.name)
                    row.createCell(2).setCellValue(person?.surname)
                    row.createCell(3).setCellValue(person?.gender?.name)
                    row.createCell(4).setCellValue(person?.name)
                    row.createCell(5).setCellValue(person?.date)
                }
            }
            for (i in headers.indices) {
                sheet.autoSizeColumn(i)
            }
            val outputStream = ByteArrayOutputStream()
            workbook.write(outputStream)
            return ByteArrayResource(outputStream.toByteArray())
        }
    }

    private fun createHeaderCellStyle(workbook: Workbook): CellStyle {
        val style = workbook.createCellStyle()
        val font = workbook.createFont()
        font.bold = true
        style.setFont(font)
        style.alignment = HorizontalAlignment.CENTER
        return style
    }
}
