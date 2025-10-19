import { Component, OnInit } from '@angular/core';
import { MachineService, MachineDTO } from '../services/machine.service';

@Component({
  selector: 'app-search-machines',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  machines: MachineDTO[] = [];
  results: MachineDTO[] = [];

  qName = '';
  qType: 'all' | 'LINUX' | 'WINDOWS' | 'MAC' = 'all';
  qState: 'all' | 'RUNNING' | 'STOPPED' | 'RESTARTING' = 'all';

  constructor(private machineService: MachineService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  private loadAll(): void {
    this.machineService.search().subscribe(list => {
      this.machines = list;
      this.results = list;
    });
  }

  search(): void {
    this.machineService.search(
      this.qName || undefined,
      this.qType === 'all' ? undefined : this.qType,
      this.qState === 'all' ? undefined : this.qState
    ).subscribe(res => this.results = res);
  }

  reset(): void {
    this.qName = '';
    this.qType = 'all';
    this.qState = 'all';
    this.loadAll();
  }
}