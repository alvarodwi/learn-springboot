package me.varoa.multiplication.repository

import me.varoa.multiplication.domain.Account
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface AccountRepository : CrudRepository<Account, Long> {
    fun findByAlias(alias: String): Optional<Account>

    fun findAllByIdIn(idList: List<Long>): List<Account>
}
