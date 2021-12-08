package com.stringconcat.ddd.shop.app.fitness

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location

class DoNotIncludeInfrastructure : ImportOption {
    override fun includes(location: Location): Boolean {
        return !location.contains("com/stringconcat/ddd/shop/app/")
    }
}