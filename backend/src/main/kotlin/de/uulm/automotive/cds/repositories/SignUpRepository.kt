package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.SignUpToken
import org.springframework.data.repository.CrudRepository

interface SignUpRepository : CrudRepository<SignUpToken, Long> {
    fun findBySignUpToken(signUpToken: String): SignUpToken?
}