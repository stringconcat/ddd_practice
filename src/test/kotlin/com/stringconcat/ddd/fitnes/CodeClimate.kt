package com.stringconcat.ddd.fitnes

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

@AnalyzeClasses(packages = ["com.stringconcat.ddd"])
class CodeClimate {

    @ArchTest
    val `no cycle dependencies` = slices()
        .matching("com.stringconcat.ddd")
        .should()
        .beFreeOfCycles()
}