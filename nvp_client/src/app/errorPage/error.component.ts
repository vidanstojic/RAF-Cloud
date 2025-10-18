import { Component, OnInit } from '@angular/core';

interface ErrorMessage {
  id: number;
  date: string;
  machineId: string;
  operation: string;
  message: string;
  createdBy: string;
}

@Component({
  selector: 'app-error-log',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {

  // Za sada mock podaci
  errors: ErrorMessage[] = [
    {
      id: 1,
      date: '2024-11-01',
      machineId: 'VM-001',
      operation: 'start',
      message: 'Mašina nije ugašena, ne može da se upali.',
      createdBy: 'marko'
    },
    {
      id: 2,
      date: '2024-11-02',
      machineId: 'VM-002',
      operation: 'stop',
      message: 'Mašina nije upaljena, ne može da se ugasi.',
      createdBy: 'jelena'
    },
    {
      id: 3,
      date: '2024-11-03',
      machineId: 'VM-003',
      operation: 'restart',
      message: 'Mašina nije upaljena, ne može da se restartuje.',
      createdBy: 'marko'
    }
  ];

  constructor() { }

  ngOnInit(): void { }

}
