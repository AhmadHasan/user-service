package com.mesmoray.lektora.userservice.service

import com.mesmoray.lektora.userservice.model.Country
import com.mesmoray.lektora.userservice.model.commands.LocaleSyncCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class LocaleService {

    @Autowired
    private lateinit var localeRepository: LocaleRepository

    @EventListener(LocaleSyncCommand::class)
    fun onLocaleSyncCommand(command: LocaleSyncCommand) {
        localeRepository.saveAll(command.locales)
    }

    fun getCountriesAndLanguages(): List<Country> {
        val locales = localeRepository.findAll()
        val countriesCodes = locales.map { it.countryCode }.distinct()
        val countries = countriesCodes.map { countryCode ->
            Country(
                countryCode,
                locales
                    .filter { it.countryCode == countryCode }
                    .map { it.languageCode }
            )
        }
        return countries
    }
}
