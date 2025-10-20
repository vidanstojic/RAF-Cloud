import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.MachineState
import com.server.nvp_server.model.User
import java.util.UUID

object MachineMapper {
    fun toDTO(machine: Machine) = MachineDTO(
        id = machine.id,
        name = machine.name,
        type = machine.type,
        description = machine.description!!,
        createdBy = machine.createdBy.id,
        state = machine.state.name,
        active = machine.active,
        uniqueId = machine.uniqueId,
        createdAt = machine.createdAt,
    )

    fun toEntity(dto: MachineDTO, createdBy: User) = Machine(
        id = dto.id ?: 0,
        name = dto.name,
        type = dto.type,
        description = dto.description ?: "",
        createdBy = createdBy,
        state = dto.state?.let { MachineState.valueOf(it) } ?: MachineState.OFF,
        active = dto.active ?: true,
        uniqueId = dto.uniqueId ?: UUID.randomUUID().toString()

    )
}
