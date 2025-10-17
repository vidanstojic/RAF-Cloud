data class MachineDTO(
    val id: Long?,
    val name: String,
    val type: String,
    val description: String,
    val createdBy: Long,
    val state: String,
    val active: Boolean,
    val uniqueId: String

)
