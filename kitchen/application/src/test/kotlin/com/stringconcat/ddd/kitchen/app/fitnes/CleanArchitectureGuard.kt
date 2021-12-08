package com.stringconcat.ddd.kitchen.app.fitnes

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures

@AnalyzeClasses(packages = ["com.stringconcat.ddd"],
    importOptions = [DoNotIncludeTests::class, DoNotIncludeInfrastructure::class])
class CleanArchitectureGuard {

    @ArchTest
    val `onion architecture should be followed for kitchen` =
        Architectures.onionArchitecture()
            .domainModels("com.stringconcat.ddd.kitchen.domain..")
            .domainServices("com.stringconcat.ddd.kitchen.domain..")
            .applicationServices("com.stringconcat.ddd.kitchen.usecase..")
            .adapter("persistence", "com.stringconcat.ddd.kitchen.persistence..")
            .adapter("rest", "com.stringconcat.ddd.kitchen.rest..")

    @ArchTest
    val `kitchen business logic should depends only on approved packages` = ArchRuleDefinition.classes()
        .that().resideInAnyPackage("com.stringconcat.ddd.kitchen.domain..")
        .should().onlyDependOnClassesThat()
        .resideInAnyPackage(
            "com.stringconcat.ddd.kitchen.domain..",
            "com.stringconcat.ddd.common..",
            "kotlin..",
            "java..",
            "org.jetbrains.annotations..",
            "arrow.core.."
        )
}