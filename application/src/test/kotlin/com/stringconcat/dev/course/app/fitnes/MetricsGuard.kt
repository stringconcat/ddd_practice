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

    @ArchTest
    fun `uncle bob metrics for shop`(importedClasses: JavaClasses) {
        val packages: Set<JavaPackage> = importedClasses.getPackage("com.stringconcat.ddd.shop").getSubpackages()
        val components: MetricsComponents<JavaClass> = MetricsComponents.fromPackages(packages)

        val metrics = ArchitectureMetrics.componentDependencyMetrics(components)

        println("Ce: " + metrics.getEfferentCoupling("com.stringconcat.ddd.shop.domain"))
        println("Ca: " + metrics.getAfferentCoupling("com.stringconcat.ddd.shop.domain"))
        println("I: " + metrics.getInstability("com.stringconcat.ddd.shop.domain"))
        println("A: " + metrics.getAbstractness("com.stringconcat.ddd.shop.domain"))
        println("D: " + metrics.getNormalizedDistanceFromMainSequence("com.stringconcat.ddd.shop.domain"))
    }
}