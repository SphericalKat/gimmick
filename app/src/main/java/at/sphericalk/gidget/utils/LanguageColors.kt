package at.sphericalk.gidget.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

class LanguageColors @Inject constructor(moshi: Moshi) {
    private val mapType: ParameterizedType = Types.newParameterizedType(
        MutableMap::class.java,
        String::class.java,
        String::class.java
    )
    private val mapAdapter = moshi.adapter<Map<String, String>>(mapType)
    var languages = mapAdapter.fromJson(Constants.LANGUAGES)!!

    operator fun get(lang: String) = languages[lang]
}