package net.wangyl.base.util

import java.lang.reflect.*
import java.util.*

fun typeToString(type: Type): String {
    return if (type is Class<*>) type.name else type.toString()
}

fun checkNotPrimitive(type: Type?) {
    require(!(type is Class<*> && type.isPrimitive))
}

/** Returns true if `a` and `b` are equal.  */
fun typeEquals(a: Type?, b: Type?): Boolean {
    return if (a === b) {
        true // Also handles (a == null && b == null).
    } else if (a is Class<*>) {
        a == b // Class already specifies equals().
    } else if (a is ParameterizedType) {
        if (b !is ParameterizedType) return false
        val pa = a
        val pb = b
        val ownerA: Any? = pa.ownerType
        val ownerB: Any = pb.ownerType
        ((ownerA === ownerB || ownerA != null && ownerA == ownerB)
                && pa.rawType == pb.rawType && Arrays.equals(
            pa.actualTypeArguments,
            pb.actualTypeArguments
        ))
    } else if (a is GenericArrayType) {
        if (b !is GenericArrayType) return false
        typeEquals(a.genericComponentType, b.genericComponentType)
    } else if (a is WildcardType) {
        if (b !is WildcardType) return false
        val wa = a
        val wb = b
        (Arrays.equals(wa.upperBounds, wb.upperBounds)
                && Arrays.equals(wa.lowerBounds, wb.lowerBounds))
    } else if (a is TypeVariable<*>) {
        if (b !is TypeVariable<*>) return false
        val va = a
        val vb = b
        (va.genericDeclaration === vb.genericDeclaration
                && va.name == vb.name)
    } else {
        false // This isn't a type we support!
    }
}

fun isSameWrapClass(a: Type?, b: Type?):Boolean {
    if (a == null || b == null) return false
    val a1 = if (a is ParameterizedType) a.rawType else a
    val b1 = if (b is ParameterizedType) b.rawType else b
    return a1 == b1
}