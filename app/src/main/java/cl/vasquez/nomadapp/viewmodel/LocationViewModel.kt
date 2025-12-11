package cl.vasquez.nomadapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.vasquez.nomadapp.data.remote.LocationResponse
import cl.vasquez.nomadapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class LocationViewModel : ViewModel() {

    private val _location = MutableStateFlow<LocationResponse?>(null)  // <- Expone un StateFlow
    val location: StateFlow<LocationResponse?> = _location.asStateFlow() // que se puede observar
                                                                        // desde cualquier pantalla.

    init{
        fetchLocation()     // <- Llama a la API apenas se crea
    }


    fun fetchLocation() {
        viewModelScope.launch {

            try {
                val response = RetrofitClient.locationApi.getLocation()
                println("DEBUG_API_RESPONSE: $response")
                _location.value = response
            } catch (e: Exception) {
                println("DEBUG_API_ERROR: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}