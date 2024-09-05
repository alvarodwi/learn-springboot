package me.varoa.logs

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

// Return logger for Java class, if companion object fix the name
fun <T : Any> logger(forClass: Class<T>): Logger = LoggerFactory.getLogger(unwrapCompanionClass(forClass).name)

// unwrap companion class to enclosing class given a Java Class
fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> =
    ofClass.enclosingClass?.takeIf {
        ofClass.enclosingClass.kotlin.companionObject
            ?.java == ofClass
    } ?: ofClass

// unwrap companion class to enclosing class given a Kotlin Class
fun <T : Any> unwrapCompanionClass(ofClass: KClass<T>): KClass<*> = unwrapCompanionClass(ofClass.java).kotlin

// Return logger for Kotlin class
fun <T : Any> logger(forClass: KClass<T>): Logger = logger(forClass.java)

// return logger from extended class (or the enclosing class)
fun <T : Any> T.logger(): Logger = logger(this.javaClass)

// return a lazy logger property delegate for enclosing class
fun <R : Any> R.lazyLogger(): Lazy<Logger> = lazy { logger(this.javaClass) }

// return a logger property delegate for enclosing class
fun <R : Any> R.injectLogger(): Lazy<Logger> = lazyOf(logger(this.javaClass))

// marker interface and related extension (remove extension for Any.logger() in favour of this)
interface Loggable

fun Loggable.logger(): Logger = logger(this.javaClass)

// abstract base class to provide logging, intended for companion objects more than classes but works for either
abstract class WithLogging : Loggable {
    val log = logger()
}
