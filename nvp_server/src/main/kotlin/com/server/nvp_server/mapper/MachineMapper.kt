import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.MachineState
import com.server.nvp_server.model.User

object MachineMapper {
    fun toDTO(machine: Machine) = MachineDTO(
        id = machine.id,
        name = machine.name,
        type = machine.type,
        description = machine.description!!,
        createdBy = machine.createdBy.id,
        state = machine.state.name,
        active = machine.active,
        uniqueId = machine.uniqueId
    )

    fun toEntity(dto: MachineDTO, createdBy: User) = Machine(
        id = dto.id!!,
        name = dto.name,
        type = dto.type,
        description = dto.description,
        createdBy = createdBy,
        state = MachineState.valueOf(dto.state),
        active = dto.active,
        uniqueId = dto.uniqueId
    )
}
