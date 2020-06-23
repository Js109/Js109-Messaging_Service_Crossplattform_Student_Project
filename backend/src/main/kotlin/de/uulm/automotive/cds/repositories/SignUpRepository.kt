package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.SignUpToken
import org.springframework.data.repository.CrudRepository

/**
 * Repository for the SignUp Tokens.
 *
 */
interface SignUpRepository : CrudRepository<SignUpToken, Long> {
    fun findBySignUpToken(signUpToken: String): SignUpToken?
}