data class MachineDTO(
    val id: Long?,
    val name: String,
    val type: String,
    val description: String,
    val ownerId: Long,
    val state: String,
    val active: Boolean
)
