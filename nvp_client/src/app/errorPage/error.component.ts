import { Component, OnInit } from '@angular/core';
import { ErrorLogService, ErrorLogDTO } from '../services/error-log.service';

@Component({
  selector: 'app-error-log',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  errors: ErrorLogDTO[] = [];

  constructor(private service: ErrorLogService) {}

  ngOnInit(): void {
    this.loadLogs();
  }

  private loadLogs(): void {
    this.service.getAll().subscribe(list => this.errors = list);
  }
}