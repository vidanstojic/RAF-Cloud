import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

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

  constructor(private http: HttpClient) {}

  create(dto: MachineDTO): Observable<MachineDTO> {
  return this.http.post<MachineDTO>(this.api, dto, { withCredentials: true });
}
getMachinesByUser(userId: number): Observable<MachineDTO[]> {
  return this.http.get<MachineDTO[]>(`${this.api}/user/${userId}`);
}
  search(name?: string, type?: string, state?: string): Observable<MachineDTO[]> {
    let params = new HttpParams();
    if (name)   params = params.set('name', name);
    if (type)   params = params.set('type', type);
    if (state)  params = params.set('state', state);
    return this.http.get<MachineDTO[]>(this.api, { params, withCredentials: true }); 
  }
  searchUserMachines(
  userId: number,
  name?: string,
  type?: string,
  state?: string
): Observable<MachineDTO[]> {
  let params = new HttpParams();
  if (name)  params = params.set('name', name);
  if (type)  params = params.set('type', type);
  if (state) params = params.set('state', state);
  return this.http.get<MachineDTO[]>(`${this.api}/user/${userId}/search`, { params });
}

  start(id: number): Observable<void> {
    return this.http.put<void>(`${this.api}/${id}/start`, {}, { withCredentials: true });
  }
  stop(id: number): Observable<void> {
    return this.http.put<void>(`${this.api}/${id}/stop`, {}, { withCredentials: true });
  }
  restart(id: number): Observable<void> {
    return this.http.put<void>(`${this.api}/${id}/restart`, {}, { withCredentials: true });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`, { withCredentials: true });
  }

  scheduleOperation(id: number, operation: string, scheduledTime: string): Observable<void> {
      const body = {
        operation,
        scheduledTime,
      };
      console.log("Scheduling operation with body:", body);
      return this.http.post<void>(`${this.api}/${id}/schedule`, body);
    }

    getMachineById(id: number): Observable<MachineDTO> {
    return this.http.get<MachineDTO>(`${this.api}/${id}`);
  }

}