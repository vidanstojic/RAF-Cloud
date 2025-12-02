import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service'; 

export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  permissions: string[];
  password?: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private api = '/api/users';

  constructor(private auth: AuthService) {}

  getAll(): Observable<User[]> {
    return this.auth.get<User[]>(this.api);
  }

  create(user: User): Observable<User> {
    return this.auth.post<User>(this.api, user);
  }

  update(id: number, user: User): Observable<User> {
    return this.auth.put<User>(`${this.api}/${id}`, user);
  }

  delete(id: number): Observable<void> {
    return this.auth.delete<void>(`${this.api}/${id}`);
  }
  findUserByEmail(email: string): Observable<User> {
  return this.auth.get<User>(`${this.api}/email/${email}`);
}
}
