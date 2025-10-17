import com.server.nvp_server.dto.UserDTO
import com.server.nvp_server.model.Permission
import com.server.nvp_server.model.User

object UserMapper {
    fun toDTO(user: User) = UserDTO(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        password = user.password,
        permissions = user.permissions.map { it.name }
    )

    fun toEntity(dto: UserDTO) = User(
        id = dto.id,
        firstName = dto.firstName,
        lastName = dto.lastName,
        email = dto.email,
        password = dto.password,
        permissions = dto.permissions.map { Permission(name = it) }.toMutableSet()
    )
}
