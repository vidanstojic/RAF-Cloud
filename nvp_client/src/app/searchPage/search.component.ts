import { Component, OnInit, OnDestroy } from '@angular/core';
import { MachineService, MachineDTO } from '../services/machine.service';
import { Router } from '@angular/router'; 
import { WebSocketService } from '../services/websocket.service';
import { Subscription } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-search-machines',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit, OnDestroy {
  private currentUserId = 0;
  machines: MachineDTO[] = [];
  qName = '';
  qType = 'all';
  qState = 'all';
  private wsSubscription!: Subscription;

  constructor(
    private machineService: MachineService,  
    private router: Router,
    private webSocketService: WebSocketService,
    public  auth: AuthService 
  ) {}

  ngOnInit(): void {
    const user = JSON.parse(localStorage.getItem('loggedUser') || '{}');
    this.currentUserId = user?.id || -1;
    this.loadMachines();
    this.subscribeToMachineStatus();
  }

  ngOnDestroy(): void {
    if (this.wsSubscription) {
      this.wsSubscription.unsubscribe();
    }
  }

  private loadMachines(): void {
    console.log("Loading machines...");
  if (this.auth.hasPermission('ADMIN')) {
    this.machineService.getAllMachines().subscribe({
      next: data => {
        console.log('>>> Angular primio:', data);
        this.machines = data;
        this.machines.forEach(m => {
          console.log(`Processing machine: ${m.name} with state: ${m.state}`);
          m.powerState = (m.state === 'ON' || m.state === 'RUNNING') ? 'on' : 'off';
          m.starting = m.state === 'OCCUPIED' && m.powerState === 'off';
          m.shuttingDown = m.state === 'OCCUPIED' && m.powerState === 'on';
          m.restarting = false;
        });
      },
      error: err => console.error(err)
    });
  } else {
    const userId = this.auth.getUserIdFromToken();
    this.machineService.getMachinesByUser(userId).subscribe({
      next: data => {
        console.log('>>> Angular primio:', data);
        this.machines = data;
        this.machines.forEach(m => {
          console.log(`Processing machine: ${m.name} with state: ${m.state}`);
          m.powerState = (m.state === 'ON' || m.state === 'RUNNING') ? 'on' : 'off';
          m.starting = m.state === 'OCCUPIED' && m.powerState === 'off';
          m.shuttingDown = m.state === 'OCCUPIED' && m.powerState === 'on';
          m.restarting = false;
        });
      },
      error: err => console.error(err)
    });
  }
}
  search(): void {
    this.machineService.searchUserMachines(
      this.currentUserId,
      this.qName || undefined,
      this.qType === 'all' ? undefined : this.qType,
      this.qState === 'all' ? undefined : this.qState
    ).subscribe(res => this.machines = res);
  }

  reset(): void {
    this.qName = '';
    this.qType = 'all';
    this.qState = 'all';
  }
  turnOn(m: MachineDTO) {
    console.log("Attempting to turn on " + m.name);
    if (this.canTurnOn(m)) {
      const command = {
        type: 'START',
        machineId: m.id!
      };
      this.webSocketService.sendMessage(command);
      m.starting = true; 
      console.log(`${m.name} is starting (WS command sent)...`);
    } else {
      console.log("Can not be turned on " + m.name);
    }
  }


  turnOff(m: MachineDTO) {
    if (this.canTurnOff(m)) {
      const command = {
        type: 'STOP',
        machineId: m.id!
      };
      this.webSocketService.sendMessage(command);
      m.shuttingDown = true;
      console.log(`${m.name} is shutting down (WS command sent)...`);
    }
  }

  restart(m: MachineDTO) {
    if (this.canRestart(m)) {
      const command = {
        type: 'RESTART',
        machineId: m.id!
      };
      this.webSocketService.sendMessage(command);
      m.restarting = true;
      console.log(`${m.name} is restarting (WS command sent)...`);
    }
  }
  
  destroy(m: MachineDTO) {
    if (this.canDestroy(m)) {
    this.machineService.delete(m.id!).subscribe({
      next: () => {
        console.log(`${m.name} successfully deleted on backend.`);
        m.active = false;
        m.powerState = 'off';
      },
      error: err => {
        console.error("Delete failed:", err);
        alert("Brisanje mašine nije uspelo.");
      }
    });
  }
  }

  canTurnOn(m: MachineDTO): boolean {
    return m.active === true && m.powerState === 'off' && !m.starting && !m.restarting && !m.shuttingDown;
  }

  canTurnOff(m: MachineDTO): boolean {
    return m.active === true && m.powerState === 'on' && !m.starting && !m.restarting && !m.shuttingDown;
  }


  canRestart(m: MachineDTO): boolean {
    return m.active === true && m.powerState === 'on' && !m.starting && !m.restarting && !m.shuttingDown;
  }


  canDestroy(m: MachineDTO): boolean {
  return (m.active === true && m.powerState === 'off' && !m.starting && !m.restarting && !m.shuttingDown
  );
}

isDisabled(m: MachineDTO): boolean {
  return m.active === false;
}



  goToSchedule(m: MachineDTO): void {
    this.router.navigate(['/schedule', m.id]);
  }
  private subscribeToMachineStatus(): void {
  this.wsSubscription = this.webSocketService.messages.subscribe(status => { 
    const machineToUpdate = this.machines.find(m => m.id === status.machineId);

    if (machineToUpdate) {
      
      switch (status.status) {
        case '200':
        case 'STARTING':
        case 'SCHEDULED_START':
          machineToUpdate.state = 'STARTING';
          machineToUpdate.starting = true;
          machineToUpdate.powerState = 'on';
          break;
        case 'RUNNING':
        case 'ON':
          machineToUpdate.state = 'RUNNING';
          machineToUpdate.restarting = false;
          machineToUpdate.starting = false;
          machineToUpdate.powerState = 'on';
          break;

        case '201':
        case 'SHUTTING_DOWN':
        case 'SCHEDULED_STOP':
          machineToUpdate.state = 'SHUTTING_DOWN';
          machineToUpdate.shuttingDown = true;
          machineToUpdate.powerState = 'on';
          break;
          
        case 'OFF':
        case 'STOPPED':
          machineToUpdate.state = 'OFF';
          machineToUpdate.shuttingDown = false;
          machineToUpdate.powerState = 'off'; //
          break;

        case '202':
        case 'RESTARTING':
        case 'SCHEDULED_RESTART':
          machineToUpdate.state = 'RESTARTING';
          machineToUpdate.restarting = true;
          machineToUpdate.powerState = 'off';
          break;
          
        case '404':
          console.error(`Mašina ID ${status.machineId} nije pronađena.`);
          alert(`Operacija '${status.machineId.operation}' nije uspešno izvrsena za ${status.machineId.name}.`);
          break;
          
        default:
          console.log(`Nepoznati status: ${status.status}`);
          break;
      }
      
      console.log(`Status update for ${machineToUpdate.name}: ${status.status}`);
    }
  });
}
}