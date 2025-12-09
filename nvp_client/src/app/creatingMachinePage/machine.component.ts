import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MachineService, MachineDTO } from '../services/machine.service';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';

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
              private router: Router, private auth: AuthService, private UserService: UserService) {
    this.machineForm = this.fb.group({
      name: ['', Validators.required],
      type: ['', Validators.required],
      description: ['', Validators.required],
    });
  }

  onSubmit(): void {
  if (this.machineForm.invalid) return;

  const dto: MachineDTO = {
    id: undefined,
    powerState: 'off',
    name: this.machineForm.value.name,
    type: this.machineForm.value.type,
    description: this.machineForm.value.description,
    createdBy: this.auth.getUserIdFromToken()  // ➜ koristi ID iz tokena
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