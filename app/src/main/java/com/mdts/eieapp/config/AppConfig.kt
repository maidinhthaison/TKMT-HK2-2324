package com.mdts.eieapp.config

object AppConfig {
    /**
     * Get environment from current build type
     */
    var backendEnvironment: BackendEnvironment = BackendEnvironment.Dev
    private fun getFromBuildType(flavor: String): BackendEnvironment {
        return when (flavor) {
            "dev" -> BackendEnvironment.Dev
            "stag" -> BackendEnvironment.Staging
            else -> BackendEnvironment.Prod
        }
    }

    fun setBackendEnvironment(flavor: String) {
        backendEnvironment = getFromBuildType(flavor)
    }
}