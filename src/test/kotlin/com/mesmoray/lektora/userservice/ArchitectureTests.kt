package com.mesmoray.lektora.userservice

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import java.io.PrintStream

class ArchitectureTests {

    private val basePackage = "com.mesmoray.lektora.userservice"
    private val importedClasses: JavaClasses = ClassFileImporter().importPackages(basePackage)

    @Test
    fun `controllers should be in the controller package`() {
        val rule = classes()
            .that().areAnnotatedWith(RestController::class.java)
            .should().resideInAPackage("$basePackage.adapter.web.input..")

        rule.check(importedClasses)
    }

    @Test
    fun `services should be in the service package`() {
        val rule = classes()
            .that().areAnnotatedWith(Service::class.java)
            .should().resideInAPackage("..service..")

        rule.check(importedClasses)
    }

    @Test
    fun `repositories should be in the repository package`() {
        val rule = classes()
            .that().areInterfaces()
            .and().areAssignableTo(MongoRepository::class.java)
            .should().resideInAPackage("..repository..")

        rule.check(importedClasses)
    }

    @Test
    fun `controllers should not access repositories`() {
        val rule = noClasses()
            .that().resideInAPackage("..adapter.web.input..")
            .should().accessClassesThat().resideInAPackage("..repository..")

        rule.check(importedClasses)
    }

    @Test
    @Disabled
    fun `classes in the model package should not access classes outside of it`() {
        val rule = noClasses()
            .that().resideInAPackage("..model..")
            .should().accessClassesThat().resideOutsideOfPackages(
                "java..", // Standard Java packages
                "javax..", // Standard Java extensions
                "kotlin..", // Standard Kotlin packages
                "org.springframework..",
                "$basePackage.model.." // Your application's model package
            )

        rule.check(importedClasses)
    }

    @Test
    fun `repositories should only be accessed by services`() {
        val rule = classes()
            .that().resideInAPackage("..repository..")
            .should().onlyBeAccessed().byAnyPackage("..service..", "..repository..")

        rule.check(importedClasses)
    }

    @Test
    fun `services should only be accessed by controllers`() {
        val rule = classes()
            .that().resideInAPackage("..service..")
            .should().onlyBeAccessed().byAnyPackage("..web.input..", "..service..")
            .orShould().onlyBeAccessed().byAnyPackage("..service..")

        rule.check(importedClasses)
    }

    @Test
    fun `no cycles between packages`() {
        // no cycles between packages
        val rule = slices()
            .matching("$basePackage.(*)..")
            .should().beFreeOfCycles()
        rule.check(importedClasses)
    }

    @Test
    fun `standard logging approach`() {
        val rule = noClasses()
            .should().callMethod("java.lang.System", "out.println")
            .orShould().callMethod("java.lang.System", "out.print")
            .orShould().callMethod(PrintStream::class.java, "println", Any::class.java)
            .orShould().callMethod(PrintStream::class.java, "print", Any::class.java)
            .because("we should use a logging framework instead of System.out")

        rule.check(importedClasses)
    }

    // Enable if you have custom exceptions
    fun `custom exceptions extend a base exception`() {
        val rule = classes()
            .that().haveSimpleNameEndingWith("Exception")
            .and().doNotHaveSimpleName("BaseException")
            .should().beAssignableTo(Exception::class.java)
        rule.check(importedClasses)
    }

    @Test
    fun `DTOs and model objects should not have logic`() {
        val rule = classes()
            .that().resideInAPackage("..dto..")
            .or().resideInAPackage("$basePackage.model..")
            .should(haveOnlyStandardMethodsInDTOs())
            .because("DTOs should only be used to transfer data")

        rule.check(importedClasses)
    }

    @Test
    fun `classes annotated as Service, Component, or Controller should not have parameterized constructors`() {
        val rule = classes()
            .that().areAnnotatedWith(Service::class.java)
            .or().areAnnotatedWith(Component::class.java)
            .or().areAnnotatedWith(Controller::class.java)
            .should(haveOnlyDefaultConstructor())
            .because("we want to disallow autowiring in constructors for Service, Component, or Controller")

        rule.check(importedClasses)
    }

    private fun haveOnlyDefaultConstructor(): ArchCondition<JavaClass> {
        return object : ArchCondition<JavaClass>("have only default constructor") {
            override fun check(item: JavaClass, events: ConditionEvents) {
                val constructors = item.constructors
                if (constructors.any { it.parameterTypes.isNotEmpty() }) {
                    val message = "Class ${item.name} has a parameterized constructor"
                    events.add(SimpleConditionEvent.violated(item, message))
                }
            }
        }
    }

    @Test
    @Disabled
    fun `adapters are not accessed directly by any class`() {
        val rule = noClasses()
            .that().resideOutsideOfPackage("..adapter..") // Exclude the adapter package itself
            .should().accessClassesThat().resideInAPackage("..adapter..")
            .because("the adapter package should not be accessed directly by any class outside of it")

        rule.check(importedClasses)
    }

    @Test
    fun `controllers are not accessed directly by any class`() {
        val rule = noClasses()
            .that().resideOutsideOfPackage("..controller..") // Exclude the adapter package itself
            .should().accessClassesThat().resideInAPackage("..controller..")
            .because("Controllers should not be accessed directly by any class")

        rule.check(importedClasses)
    }

    @Test
    fun `autowired fields should be private`() {
        val rule = ArchRuleDefinition.fields()
            .that().areAnnotatedWith(Autowired::class.java)
            .should().bePrivate()
            .because("we want to enforce encapsulation of dependencies")

        rule.check(importedClasses)
    }

    /**
     * Auxiliary methods
     */

    private fun haveOnlyStandardMethodsInDTOs(): ArchCondition<JavaClass> {
        return object : ArchCondition<JavaClass>("have only standard methods") {
            override fun check(item: JavaClass, events: ConditionEvents) {
                val allowedMethodNames = setOf(
                    "equals",
                    "hashCode",
                    "toString",
                    "component",
                    "copy",
                    "getDescription",
                    "get",
                    "values",
                    "valueOf",
                    "set",
                    "to",
                    "\$values",
                    "set"
                )

                item.methods.forEach { method ->
                    if (allowedMethodNames.none { method.name.startsWith(it) }) {
                        val message = "Class ${item.name} contains a non-standard method: ${method.name}"
                        events.add(SimpleConditionEvent.violated(method, message))
                    }
                }
            }
        }
    }
}
