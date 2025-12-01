import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = '/api/users';
  constructor(public http: HttpClient) {}

  login(email: string, pass: string): Observable<{ token: string; permissions: string[] }> {
    return this.http.post<{ token: string; permissions: string[] }>(
      `${this.api}/loginuser`, { email, password: pass }
    ).pipe(tap(res => {
      localStorage.setItem('token', res.token);
      localStorage.setItem('permissions', JSON.stringify(res.permissions));
    }));
  }

  getPermissions(): string[] {
    try { return JSON.parse(localStorage.getItem('permissions') || '[]'); } catch { return []; }
  }
  hasPermission(perm: string): boolean { return this.getPermissions().includes(perm); }
  logout(): void { localStorage.clear(); }

  /* ==========  JEDNA get metoda – pokriva sve slučajeve  ========== */
  get<T>(url: string, params?: HttpParams): Observable<T> {
    return this.http.get<T>(url, {
      headers: this.authHeaders(),
      params: params || undefined
    });
  }

  post<T>(url: string, body: any): Observable<T> {
    return this.http.post<T>(url, body, { headers: this.authHeaders() });
  }
  put<T>(url: string, body: any): Observable<T> {
    return this.http.put<T>(url, body, { headers: this.authHeaders() });
  }
  delete<T>(url: string): Observable<void> {
    return this.http.delete<void>(url, { headers: this.authHeaders() });
  }

  /* privatni helper */
  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Authorization', token ? `Bearer ${token}` : '');
  }
}