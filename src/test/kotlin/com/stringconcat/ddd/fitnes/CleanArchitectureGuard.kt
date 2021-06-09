package com.stringconcat.ddd.fitnes

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures

@AnalyzeClasses(packages = ["com.stringconcat.ddd"])
class CleanArchitectureGuard {

    @ArchTest
    val `onion architecture should be followed for shop` =
        Architectures.onionArchitecture()
            .domainModels("com.stringconcat.ddd.shop.domain..")
            .domainServices("com.stringconcat.ddd.shop.domain..")
            .applicationServices("com.stringconcat.ddd.shop.usecase..")
            .adapter("persistence", "com.stringconcat.ddd.shop.persistence..")
            .adapter("telnet", "com.stringconcat.ddd.shop.telnet..")
            .adapter("web", "com.stringconcat.ddd.shop.web..")

    @ArchTest
    val `onion architecture should be followed for kitchen` =
        Architectures.onionArchitecture()
            .domainModels("com.stringconcat.ddd.kitchen.domain..")
            .domainServices("com.stringconcat.ddd.kitchen.domain..")
            .applicationServices("com.stringconcat.ddd.kitchen.usecase..")
            .adapter("persistence", "com.stringconcat.ddd.kitchen.persistence..")

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

    @ArchTest
    val `shop business logic should depends only on approved packages` = ArchRuleDefinition.classes()
        .that().resideInAnyPackage("com.stringconcat.ddd.shop.domain..")
        .should().onlyDependOnClassesThat()
        .resideInAnyPackage(
            "com.stringconcat.ddd.shop.domain..",
            "com.stringconcat.ddd.common..",
            "kotlin..",
            "java..",
            "org.jetbrains.annotations..",
            "arrow.core.."
        )
}