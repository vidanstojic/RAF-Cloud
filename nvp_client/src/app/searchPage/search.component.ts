import { Component, OnInit } from '@angular/core';
import { MachineService, MachineDTO } from '../services/machine.service';

@Component({
  selector: 'app-search-machines',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})export class SearchComponent implements OnInit {
  machines: MachineDTO[] = [];
  results: MachineDTO[] = [];

  qName = '';
  qType: 'all' | 'LINUX' | 'WINDOWS' | 'MAC' = 'all';
  qState: 'all' | 'RUNNING' | 'STOPPED' | 'RESTARTING' = 'all';

  constructor(private machineService: MachineService) {}

  ngOnInit(): void {
    this.loadMyMachines();          
  }

  private loadMyMachines(): void {
    this.machineService.getMachinesByUser(localStorage.getItem('loggedUser') ? JSON.parse(localStorage.getItem('loggedUser')!).id : -1).subscribe(list => {
      this.machines = list;
      this.results = list;
    });
  }

  search(): void {
    /* optional: still call generic search and filter client-side */
    this.machineService.search(
      this.qName || undefined,
      this.qType === 'all' ? undefined : this.qType,
      this.qState === 'all' ? undefined : this.qState
    ).subscribe(res => {
      this.results = res;   // already filtered by back-end
    });
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

  restart(m: MachineDTO): void {
    this.machineService.restart(m.id!).subscribe(() => this.loadMyMachines());
  }

  delete(m: MachineDTO): void {
    this.machineService.delete(m.id!).subscribe(() => this.loadMyMachines());
  }
}