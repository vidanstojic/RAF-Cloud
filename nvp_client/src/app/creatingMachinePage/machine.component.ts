import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

interface MachineType {
  value: string;
  label: string;
}

@Component({
  selector: 'app-create-machine',
  templateUrl: './machine.component.html',
  styleUrls: ['./machine.component.css']
})
export class CreateMachineComponent {
  machineForm: FormGroup;
  machineTypes: MachineType[] = [
    { value: 'LINUX', label: 'Linux' },
    { value: 'WINDOWS', label: 'Windows' },
    { value: 'MAC', label: 'MacOS' }
  ];

  constructor(private fb: FormBuilder) {
    this.machineForm = this.fb.group({
      name: ['', Validators.required],
      type: ['', Validators.required],
      description: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.machineForm.invalid) {
      alert('Попуни сва поља!');
      return;
    }

    const newMachine = {
      name: this.machineForm.value.name,
      type: this.machineForm.value.type,
      description: this.machineForm.value.description,
      state: 'UGASENA',
      active: true
    };

  
    console.log('✅ Napravljena mašina:', newMachine);

    alert(`Машина "${newMachine.name}" је успешно направљена!`);

    
    this.machineForm.reset();
  }
}
