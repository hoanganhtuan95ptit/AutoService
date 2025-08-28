package com.hoanganhtuan95ptit.autobind.utils.exts


internal fun String.createClass() = runCatching {

    Class.forName(this)
}.getOrElse {

    null
}

internal fun <T> String.createObject(clazz: Class<T>) = runCatching {

    val instance = Class.forName(this).getDeclaredConstructor().newInstance()
    if (clazz.isInstance(instance)) clazz.cast(instance) else null
}.getOrElse {

    null
}