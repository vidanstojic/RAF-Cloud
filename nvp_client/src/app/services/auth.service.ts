import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = '/api/users';

  constructor(private http: HttpClient) {}

  login(email: string, pass: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(
      `${this.api}/loginuser`,
      { email, password: pass }
    );
  }
}