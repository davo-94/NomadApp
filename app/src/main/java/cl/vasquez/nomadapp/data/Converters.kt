package cl.vasquez.nomadapp.data

import androidx.room.TypeConverter

/**
 * TypeConverter en Room
 * Room solo entiende tipos básicos en las columnas SQLite (String, Int, Boolean, etc).
 * Room no sabe guardar listas ni objetos completos.
 * TypeConverter le indica a Room cómo transformar un tipo complejo en uno básico y viceversa.
 * Para el uso de carruseles y galerías de imágenes, estamos utilizando listas de URIs.
 * Los TypeConverter permiten el almacenamiento de esas URIs en cadenas de strings para Room
 * y el uso de ellas en Compose con la reconversión en listas.
 */
class Converters {
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String?): List<String>? {
        return data?.split(",")?.map { it.trim() }
    }
}
