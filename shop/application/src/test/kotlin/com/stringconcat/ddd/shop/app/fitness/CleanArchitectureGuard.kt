package com.stringconcat.ddd.shop.app.fitness

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures

@AnalyzeClasses(packages = ["com.stringconcat.ddd"],
    importOptions = [DoNotIncludeTests::class, DoNotIncludeInfrastructure::class])
class CleanArchitectureGuard {

    @ArchTest
    val `onion architecture should be followed for shop` =
        Architectures.onionArchitecture()
            .domainModels("com.stringconcat.ddd.shop.domain..")
            .domainServices("com.stringconcat.ddd.shop.domain..")
            .applicationServices("com.stringconcat.ddd.shop.usecase..")
            .adapter("persistence", "com.stringconcat.ddd.shop.persistence..")
            .adapter("telnet", "com.stringconcat.ddd.shop.telnet..")
            .adapter("rest", "com.stringconcat.ddd.shop.rest..")
            .adapter("crm", "com.stringconcat.ddd.shop.crm..")

    @ArchTest
    val `shop business logic should depends only on approved packages` = ArchRuleDefinition.classes()
        .that()
        .resideInAnyPackage("com.stringconcat.ddd.shop.domain..")
        .and()
        .doNotHaveSimpleName("FixturesKt")
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage(
            "com.stringconcat.ddd.common..",
            "com.stringconcat.ddd.shop.domain..",
            "kotlin..",
            "java..",
            "org.jetbrains.annotations..",
            "arrow.core.."
        )
}