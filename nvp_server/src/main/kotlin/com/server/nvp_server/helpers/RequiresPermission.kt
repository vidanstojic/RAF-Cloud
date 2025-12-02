package com.server.nvp_server.helpers

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresAnyPermission(vararg val value: String)