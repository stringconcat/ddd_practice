package com.stringconcat.ddd.shop.app.fitness

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

@AnalyzeClasses(packages = ["com.stringconcat.ddd"], importOptions = [ImportOption.DoNotIncludeTests::class])
class CodeClimate {

    @ArchTest
    val `no cycle dependencies` = slices()
        .matching("com.stringconcat.ddd.(**)")
        .should()
        .beFreeOfCycles()
}