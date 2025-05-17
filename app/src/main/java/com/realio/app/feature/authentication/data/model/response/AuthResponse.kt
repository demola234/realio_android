import com.google.gson.annotations.SerializedName
import com.realio.app.feature.authentication.domain.entity.User

data class AuthApiResponse(
    @SerializedName("user") val user: UserDto?,
    @SerializedName("session") val session: SessionDto?
)

data class UserDto(
    @SerializedName("user_id") val userId: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("isVerified") val isVerified: Boolean?,
    @SerializedName("role") val role: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("createdAt") val createdAt: String?
)

data class SessionDto(
    @SerializedName("token") val token: String?,
    @SerializedName("expiresAt") val expiresAt: String?
)

data class AuthResponse(
    val userId: String,
    val token: String,
    val refreshToken: String,
    val name: String,
    val email: String,
    val phone: String?,
    val isVerified: Boolean
)

fun AuthApiResponse.toAuthResponse(refreshToken: String = "") : AuthResponse {
    return AuthResponse(
        userId = user?.userId ?: "",
        token = session?.token ?: "",
        refreshToken = refreshToken,
        name = user?.fullName ?: "",
        email = user?.email ?: "",
        phone = user?.phone ?: "",
        isVerified = user?.isVerified == true
    )
}

fun AuthResponse.toUser(): User {
    return User(
        id = userId,
        name = name,
        email = email,
        phone = phone,
        isVerified = isVerified
    )
}