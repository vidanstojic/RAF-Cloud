import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ErrorLogDTO {
  id?: number;
  machineId: number;
  operation: string;
  message: string;
  date: string;                
}

@Injectable({ providedIn: 'root' })
export class ErrorLogService {
  private readonly api = '/api/error-logs';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ErrorLogDTO[]> {
    const token = localStorage.getItem('token');
    return this.http.get<ErrorLogDTO[]>(this.api, {
      headers: new HttpHeaders().set('Authorization', `Bearer ${token}`)
    });
  }
}