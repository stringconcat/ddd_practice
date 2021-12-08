package com.stringconcat.ddd.kitchen.app.fitnes

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

@AnalyzeClasses(packages = ["com.stringconcat.ddd"], importOptions = [DoNotIncludeTests::class])
class CodeClimate {

    @ArchTest
    val `no cycle dependencies` = slices()
        .matching("com.stringconcat.ddd.(**)")
        .should()
        .beFreeOfCycles()
}