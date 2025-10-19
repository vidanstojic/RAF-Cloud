import java.time.LocalDateTime

data class MachineDTO(
    val id: Long? = null,
    val name: String,
    val type: String,
    val description: String? = null,
    val createdBy: Long? = null,
    val state: String? = null,
    val active: Boolean? = null,
    val uniqueId: String? = null,
    val createdAt: LocalDateTime? = null
)
