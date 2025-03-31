package br.com.smart.schedule.vo

import br.com.smart.schedule.utils.GENDER
import java.time.LocalDate
import java.util.*

data class PersonRequestVO(
    var name: String? = null,
    var surname: String? = null,
    var gender: GENDER? = null,
    var email: String? = null,
    var date: LocalDate? = null
)
