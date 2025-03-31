package br.com.smart.schedule.entities

import br.com.smart.schedule.utils.GENDER
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "tb_person")
data class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String? = null,
    var surname: String? = null,
    @Enumerated(EnumType.STRING)
    var gender: GENDER? = null,
    var email: String? = null,
    var date: LocalDate? = null
)
