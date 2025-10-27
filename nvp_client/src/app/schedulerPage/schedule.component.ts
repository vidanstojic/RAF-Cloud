import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MachineService, MachineDTO } from '../services/machine.service';

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {
  machineId!: number;
  machine?: MachineDTO;
  operation = 'start';
  scheduledTime: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private machineService: MachineService
  ) {}

  ngOnInit(): void {
    this.machineId = Number(this.route.snapshot.paramMap.get('id'));
    console.log("Scheduling for machine ID:", this.machineId);
    this.machineService.getMachineById(this.machineId).subscribe({
      next: (m) => (this.machine = m),
      error: () => console.error('Greška prilikom učitavanja mašine')
    });
  }

  scheduleOperation(): void {
  if (!this.scheduledTime) {
    alert('Molimo izaberite datum i vreme.');
    return;
  }


  this.machineService.scheduleOperation(this.machineId, this.operation, this.scheduledTime)
    .subscribe({
      next: () => {
        alert(`Operacija '${this.operation}' uspešno zakazana za ${this.scheduledTime}.`);
        this.router.navigate(['/search-machines']);
      },
      error: (err) => {
        console.error('Greška pri zakazivanju operacije:', err);
        alert('Došlo je do greške pri zakazivanju.');
      }
    });
}


  cancel(): void {
    this.router.navigate(['/search']);
  }
}

