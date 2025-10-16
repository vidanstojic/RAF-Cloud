import java.time.LocalDateTime

data class ErrorLogDTO(
    val id: Long?,
    val machineId: Long,
    val operation: String,
    val message: String,
    val date: LocalDateTime
)
