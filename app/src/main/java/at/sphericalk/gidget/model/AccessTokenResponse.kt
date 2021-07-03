package at.sphericalk.gidget.model

import androidx.annotation.Keep

@Keep
data class AccessTokenResponse(
    val access_token: String? = null,
    val token_type: String? = null,
    val error: String? = null,
    val error_description: String? = null,
    val error_uri: String? = null,
)