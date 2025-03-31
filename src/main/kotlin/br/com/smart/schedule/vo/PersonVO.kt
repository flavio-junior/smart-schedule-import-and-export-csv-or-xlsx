package br.com.smart.schedule.vo

import br.com.smart.schedule.utils.GENDER

data class PersonVO(
    var id: Long? = null,
    var name: String? = null,
    var surname: String? = null,
    var gender: GENDER? = null,
    var email: String? = null,
    var date: String? = null
)
