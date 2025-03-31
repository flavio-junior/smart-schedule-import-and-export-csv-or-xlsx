package br.com.smart.schedule.file.importer.impl

import br.com.smart.schedule.entities.Person
import br.com.smart.schedule.file.importer.contract.FileImporter
import br.com.smart.schedule.utils.GENDER
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Component
import java.io.InputStream
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Component
class XlsxImporter : FileImporter {

    override fun importFile(
        inputStream: InputStream?
    ): List<Person?>? {
        XSSFWorkbook(inputStream).use { workbook ->
            val sheet = workbook.getSheetAt(0)
            val rowIterator: Iterator<Row> = sheet.iterator()
            if (rowIterator.hasNext()) rowIterator.next()
            return parseRowsToPersonList(rowIterator)
        }
    }

    private fun parseRowsToPersonList(
        rowIterator: Iterator<Row>
    ): List<Person> {
        val people: MutableList<Person> = ArrayList()

        while (rowIterator.hasNext()) {
            val row = rowIterator.next()
            if (isRowValid(row)) {
                people.add(parseRowToPerson(row))
            }
        }
        return people
    }

    private fun parseRowToPerson(
        row: Row
    ): Person {
        val person = Person()
        person.name = row.getCell(0).stringCellValue
        person.surname = row.getCell(1).stringCellValue
        person.gender = try {
            GENDER.valueOf(row.getCell(2).stringCellValue.uppercase(Locale.getDefault()))
        } catch (e: IllegalArgumentException) {
            null
        }
        person.email = row.getCell(3).stringCellValue
        try {
            val dateCell = row.getCell(4)
            person.date = when (dateCell.cellType) {
                CellType.STRING -> {
                    val inputDate = dateCell.stringCellValue.trim()
                    LocalDate.parse(inputDate)
                }
                CellType.NUMERIC -> {
                    if (DateUtil.isCellDateFormatted(dateCell)) {
                        val date = dateCell.dateCellValue
                        date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    } else {
                        null
                    }
                }
                else -> null
            }
        } catch (e: Exception) {
            person.date = null
        }
        return person
    }

    companion object {
        private fun isRowValid(row: Row): Boolean {
            return row.getCell(0) != null && row.getCell(0).cellType != CellType.BLANK
        }
    }
}
