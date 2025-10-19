import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  permissions: string[];
  password?: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly api = '/api/users';   

  constructor(private http: HttpClient) {}

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.api);
  }

  create(user: User): Observable<User> {
    return this.http.post<User>(this.api, user);
  }
  update(id: number, user: User): Observable<User> {
    return this.http.put<User>(`${this.api}/${id}`, user);
  }
}