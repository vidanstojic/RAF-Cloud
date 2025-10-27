import { Component, OnInit } from '@angular/core';
import { MachineService, MachineDTO } from '../services/machine.service';
import { Router } from '@angular/router'; 

@Component({
  selector: 'app-search-machines',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})export class SearchComponent implements OnInit {
  private currentUserId = 0;
  machines: MachineDTO[] = [];
  results: MachineDTO[] = [];
  qName = '';
  qType = 'all';
  qState = 'all';

  constructor(private machineService: MachineService,  private router: Router) {}

  ngOnInit(): void {
    const user = JSON.parse(localStorage.getItem('loggedUser') || '{}');
    this.currentUserId = user?.id || -1;
    this.loadMyMachines();
  }

  private loadMyMachines(): void {
    this.machineService.getMachinesByUser(this.currentUserId)
      .subscribe(list => {
        this.machines = list;
        this.results = list;
      });
  }

  search(): void {
    this.machineService.searchUserMachines(
      this.currentUserId,
      this.qName || undefined,
      this.qType === 'all' ? undefined : this.qType,
      this.qState === 'all' ? undefined : this.qState
    ).subscribe(res => this.results = res);
  }

  reset(): void {
    this.qName = '';
    this.qType = 'all';
    this.qState = 'all';
    this.loadMyMachines();
  }

    start(m: MachineDTO): void {
    this.machineService.start(m.id!).subscribe(() => this.loadMyMachines());
  }

  stop(m: MachineDTO): void {
    this.machineService.stop(m.id!).subscribe(() => this.loadMyMachines());
  }

  delete(m: MachineDTO): void {
    this.machineService.delete(m.id!).subscribe(() => this.loadMyMachines());
  }

  canTurnOn(m: MachineDTO): boolean {

    console.log("nestoooooo " + m.starting + " " + m.active +  " " + m.powerState);
    return m.active === true && (m.powerState === 'off' || m.powerState === undefined) && (!m.starting || m.starting === undefined);
  }

  canTurnOff(m: MachineDTO): boolean {
    return m.active === true && m.powerState === 'on' && m.starting === false;
  }


  canRestart(m: MachineDTO): boolean {
    return m.active === true && m.powerState === 'on' && m.starting === false;
  }


  canDestroy(m: MachineDTO): boolean {
    return m.active === true && m.powerState === 'off';
  }

  turnOn(m: MachineDTO) {
    if (this.canTurnOn(m)) {
      m.starting = true;
      console.log(`${m.name} is starting...`);

      const delay = 10000;

      setTimeout(() => {
        m.powerState = 'on';
        m.starting = false;
        console.log(`${m.name} is turned on.`);
      }, delay);
    }else{
      console.log("Can not be turned on " + m.name);
    }
  }


  turnOff(m: MachineDTO) {
    if (this.canTurnOff(m)) {
      m.shuttingDown = true;
      console.log(`${m.name} is shouting down...`);

      const delay = 10000;

      setTimeout(() => {
        m.powerState = 'off';
        m.shuttingDown = false;
        console.log(`${m.name} is shotted down.`);
      }, delay);
    }
  }

  restart(m: MachineDTO) {
    if (this.canRestart(m)) {
      m.restarting = true;
      console.log(`${m.name} is restarting...`);

      const totalDelay = 10000;
      const halfDelay = Math.floor(totalDelay / 2);

     
      setTimeout(() => {
        m.powerState = 'off';
        console.log(`${m.name} is shutting down...`);
      }, halfDelay);

      
      setTimeout(() => {
        m.powerState = 'on';
        m.restarting = false;
        console.log(`${m.name} is restarted.`);
      }, totalDelay);
    }
  }


  destroy(m: MachineDTO) {
    if (this.canDestroy(m)) {
      m.active = false;
      m.powerState = 'off';
      console.log(`${m.name} is deleted.`);
    }
  }

  goToSchedule(m: MachineDTO): void {
    this.router.navigate(['/schedule', m.id]);
  }
}
