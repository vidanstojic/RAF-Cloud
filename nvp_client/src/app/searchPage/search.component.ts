import { Component, OnInit } from '@angular/core';
import { MachineService, MachineDTO } from '../services/machine.service';

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

  constructor(private machineService: MachineService) {}

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
    /* ----  CALL  /user/{id}/search  WITH PARAMS  ---- */
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

  /* action buttons – unchanged */
    start(m: MachineDTO): void {
    this.machineService.start(m.id!).subscribe(() => this.loadMyMachines());
  }

  stop(m: MachineDTO): void {
    this.machineService.stop(m.id!).subscribe(() => this.loadMyMachines());
  }

  // restart(m: MachineDTO): void {
  //   this.machineService.restart(m.id!).subscribe(() => this.loadMyMachines());
  // }

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
      console.log(`${m.name} se pokreće...`);

      const delay = 10000;

      setTimeout(() => {
        m.powerState = 'on';
        m.starting = false;
        console.log(`${m.name} je sada upaljena.`);
      }, delay);
    }else{
      console.log("Ne može da se upali mašina " + m.name);
    }
  }


  turnOff(m: MachineDTO) {
    if (this.canTurnOff(m)) {
      m.shuttingDown = true;
      console.log(`${m.name} se gasi...`);

      const delay = 10000;

      setTimeout(() => {
        m.powerState = 'off';
        m.shuttingDown = false;
        console.log(`${m.name} je sada ugašena.`);
      }, delay);
    }
  }

  restart(m: MachineDTO) {
    if (this.canRestart(m)) {
      m.restarting = true;
      console.log(`${m.name} se restartuje...`);

      const totalDelay = 10000;
      const halfDelay = Math.floor(totalDelay / 2);

     
      setTimeout(() => {
        m.powerState = 'off';
        console.log(`${m.name} se trenutno gasi...`);
      }, halfDelay);

      
      setTimeout(() => {
        m.powerState = 'on';
        m.restarting = false;
        console.log(`${m.name} je restartovana i upaljena.`);
      }, totalDelay);
    }
  }


  destroy(m: MachineDTO) {
    if (this.canDestroy(m)) {
      m.active = false;
      m.powerState = 'off';
      console.log(`${m.name} je uništena.`);
    }
  }
}
