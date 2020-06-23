package de.uulm.automotive.cds.repositories

import de.uulm.automotive.cds.entities.Token
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Repository for the SignUp Tokens.
 *
 */
interface SignUpRepository : CrudRepository<Token, Long> {
    fun findBySignUpToken(signUpToken: UUID): Token?
}