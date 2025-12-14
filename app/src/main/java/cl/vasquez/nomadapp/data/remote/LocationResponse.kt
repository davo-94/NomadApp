package cl.vasquez.nomadapp.data.remote

data class LocationResponse(
    val city: String?,
    val region: String?,
    val country_name: String?,
    val latitude: Double?,
    val longitude: Double?
)
