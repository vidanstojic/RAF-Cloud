import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MachineService, MachineDTO } from '../services/machine.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-machine',
  templateUrl: './machine.component.html',
  styleUrls: ['./machine.component.css']
})
export class CreateMachineComponent {
  machineForm: FormGroup;
  machineTypes = [
    { value: 'LINUX', label: 'Linux' },
    { value: 'WINDOWS', label: 'Windows' },
    { value: 'MAC', label: 'MacOS' }
  ];

  constructor(private fb: FormBuilder,
              private machineService: MachineService,
              private router: Router) {
    this.machineForm = this.fb.group({
      name: ['', Validators.required],
      type: ['', Validators.required],
      description: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.machineForm.invalid) return;

    const user = JSON.parse(localStorage.getItem('loggedUser') || '{}');
    const dto: MachineDTO = {
      id: localStorage.getItem('loggedUser') ? undefined : -1,
      name: this.machineForm.value.name,
      type: this.machineForm.value.type,
      description: this.machineForm.value.description,
      createdBy: user.id          // ← NEW
    };

    this.machineService.create(dto).subscribe({
      next: () => {
        alert('Machine created');
        this.router.navigate(['/search-machines']);
      },
      error: err => alert(err.message)
    });
  }
}