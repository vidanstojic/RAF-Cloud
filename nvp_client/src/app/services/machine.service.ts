import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

type PowerState = 'on' | 'off';

export interface MachineDTO {
  id?: number;
  name: string;
  type: string;
  description: string;
  createdBy?: number;
  state?: string;
  active?: boolean;
  powerState: PowerState;
  starting?: boolean;
  shuttingDown?: boolean;
  restarting?: boolean;
}

@Injectable({ providedIn: 'root' })
export class MachineService {
  private readonly api = '/api/machines';

  constructor(private auth: AuthService) {}

  create(dto: MachineDTO): Observable<MachineDTO> {
    return this.auth.post<MachineDTO>(this.api, dto);
  }

  getMachinesByUser(userId: number): Observable<MachineDTO[]> {
    return this.auth.get<MachineDTO[]>(`${this.api}/user/${userId}`);
  }

  search(name?: string, type?: string, state?: string): Observable<MachineDTO[]> {
    let params = new HttpParams();
    if (name)   params = params.set('name', name);
    if (type)   params = params.set('type', type);
    if (state)  params = params.set('state', state);
    return this.auth.get<MachineDTO[]>(this.api, params);
  }

  searchUserMachines(userId: number, name?: string, type?: string, state?: string): Observable<MachineDTO[]> {
    let params = new HttpParams();
    if (name)  params = params.set('name', name);
    if (type)  params = params.set('type', type);
    if (state) params = params.set('state', state);
    return this.auth.get<MachineDTO[]>(`${this.api}/user/${userId}/search`, params);
  }

  start(id: number): Observable<void> {
    return this.auth.put<void>(`${this.api}/${id}/start`, {});
  }

  stop(id: number): Observable<void> {
    return this.auth.put<void>(`${this.api}/${id}/stop`, {});
  }

  restart(id: number): Observable<void> {
    return this.auth.put<void>(`${this.api}/${id}/restart`, {});
  }

  delete(id: number): Observable<void> {
    return this.auth.delete<void>(`${this.api}/${id}`);
  }

  scheduleOperation(id: number, operation: string, scheduledTime: string): Observable<void> {
    const body = { operation, scheduledTime };
    return this.auth.post<void>(`${this.api}/${id}/schedule`, body);
  }

  getMachineById(id: number): Observable<MachineDTO> {
    return this.auth.get<MachineDTO>(`${this.api}/${id}`);
  }
}