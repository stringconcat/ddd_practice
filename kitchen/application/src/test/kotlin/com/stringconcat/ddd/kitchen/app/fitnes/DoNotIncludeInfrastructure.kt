package com.stringconcat.ddd.kitchen.app.fitnes

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location

class DoNotIncludeInfrastructure : ImportOption {
    override fun includes(location: Location): Boolean {
        return !location.contains("com/stringconcat/ddd/kitchen/app/")
    }
}