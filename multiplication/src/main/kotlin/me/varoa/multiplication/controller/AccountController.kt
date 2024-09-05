package me.varoa.multiplication.controller

import me.varoa.multiplication.domain.Account
import me.varoa.multiplication.repository.AccountRepository
import me.varoa.multiplication.utils.lazyLogger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val repository: AccountRepository,
) {
    private val log by lazyLogger()

    @GetMapping("/{idList}")
    fun getUsersByIdList(
        @PathVariable("idList") idList: List<Long>,
    ): List<Account> {
        log.info("Resolving alias for accounts $idList")
        return repository.findAllByIdIn(idList)
    }
}
