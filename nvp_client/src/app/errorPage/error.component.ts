import { Component, OnInit } from '@angular/core';
import { ErrorLogService, ErrorLogDTO } from '../services/error-log.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-error-log',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  errors: ErrorLogDTO[] = [];

  constructor(private service: ErrorLogService, private auth: AuthService) {}

  ngOnInit(): void {
    this.loadLogs();
  }

  private loadLogs(): void {
  if (this.auth.hasPermission('ADMIN')) {
    this.service.getAll().subscribe(list => this.errors = list);
  } else {
    const userId = this.auth.getUserIdFromToken();
    this.service.getLogsByUser(userId).subscribe(list => this.errors = list);
  }
}
}