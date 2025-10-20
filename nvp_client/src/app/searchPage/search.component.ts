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

  restart(m: MachineDTO): void {
    this.machineService.restart(m.id!).subscribe(() => this.loadMyMachines());
  }

  delete(m: MachineDTO): void {
    this.machineService.delete(m.id!).subscribe(() => this.loadMyMachines());
  }
}