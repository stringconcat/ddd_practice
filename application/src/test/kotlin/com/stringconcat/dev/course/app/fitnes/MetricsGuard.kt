package com.stringconcat.dev.course.app.fitnes

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.JavaPackage
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.metrics.ArchitectureMetrics
import com.tngtech.archunit.library.metrics.MetricsComponents

@AnalyzeClasses(packages = ["com.stringconcat.ddd"])
class MetricsGuard {

    val SHOP_OVERALL = "com.stringconcat.ddd.shop"
    val SHOP_DOMAIN = "com.stringconcat.ddd.shop.domain"
    val SHOP_PERSISTANCE = "com.stringconcat.ddd.shop.persistence"
    val SHOP_USECASE = "com.stringconcat.ddd.shop.usecase"
    val SHOP_WEB = "com.stringconcat.ddd.shop.rest"

    @ArchTest
    fun `uncle bob metrics for shop`(importedClasses: JavaClasses) {
        val packages: Set<JavaPackage> = importedClasses.getPackage("com.stringconcat.ddd.shop").getSubpackages()
        val components: MetricsComponents<JavaClass> = MetricsComponents.fromPackages(packages)

        val metrics = ArchitectureMetrics.componentDependencyMetrics(components)

        println("---------DOMAIN---------")
        println("Ca (incoming): " + metrics.getAfferentCoupling(SHOP_DOMAIN))
        println("Ce (outgoing): " + metrics.getEfferentCoupling(SHOP_DOMAIN))
        println("I: " + metrics.getInstability(SHOP_DOMAIN))
        println("A: " + metrics.getAbstractness(SHOP_DOMAIN))
        println("D: " + metrics.getNormalizedDistanceFromMainSequence(SHOP_DOMAIN))
        println()

        println("---------PERSISTENCE---------")
        println("Ca (incoming): " + metrics.getAfferentCoupling(SHOP_PERSISTANCE))
        println("Ce (outgoing): " + metrics.getEfferentCoupling(SHOP_PERSISTANCE))
        println("I: " + metrics.getInstability(SHOP_PERSISTANCE))
        println("A: " + metrics.getAbstractness(SHOP_PERSISTANCE))
        println("D: " + metrics.getNormalizedDistanceFromMainSequence(SHOP_PERSISTANCE))
        println()

        println("---------USECASE---------")
        println("Ca (incoming): " + metrics.getAfferentCoupling(SHOP_USECASE))
        println("Ce (outgoing): " + metrics.getEfferentCoupling(SHOP_USECASE))
        println("I: " + metrics.getInstability(SHOP_USECASE))
        println("A: " + metrics.getAbstractness(SHOP_USECASE))
        println("D: " + metrics.getNormalizedDistanceFromMainSequence(SHOP_USECASE))
        println()

        println("---------WEB---------")
        println("Ca (incoming): " + metrics.getAfferentCoupling(SHOP_WEB))
        println("Ce (outgoing): " + metrics.getEfferentCoupling(SHOP_WEB))
        println("I: " + metrics.getInstability(SHOP_WEB))
        println("A: " + metrics.getAbstractness(SHOP_WEB))
        println("D: " + metrics.getNormalizedDistanceFromMainSequence(SHOP_WEB))
        println()
    }

    @ArchTest
    fun `overall uncle bob metrics`(importedClasses: JavaClasses) {
        val packages: Set<JavaPackage> = importedClasses.getPackage("com.stringconcat.ddd").getSubpackages()
        val components: MetricsComponents<JavaClass> = MetricsComponents.fromPackages(packages)

        val metrics = ArchitectureMetrics.componentDependencyMetrics(components)

        println("---------OVERALL---------")
        println("Ca (incoming): " + metrics.getAfferentCoupling(SHOP_OVERALL))
        println("Ce (outgoing): " + metrics.getEfferentCoupling(SHOP_OVERALL))
        println("I: " + metrics.getInstability(SHOP_OVERALL))
        println("A: " + metrics.getAbstractness(SHOP_OVERALL))
        println("D: " + metrics.getNormalizedDistanceFromMainSequence(SHOP_OVERALL))
        println()
    }
}