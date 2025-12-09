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
  hasPermission(required: string | string[]): boolean {
  const perms = JSON.parse(localStorage.getItem('permissions') || '[]') as string[];

  if (perms.includes('ADMIN')) return true; 

  const requiredArray = Array.isArray(required) ? required : [required];
  return requiredArray.some(r => perms.includes(r));
}
  logout(): void { localStorage.clear(); }

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
  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Authorization', token ? `Bearer ${token}` : '');
  }
  getCurrentUserEmail(): string {
  const token = localStorage.getItem('token');
  if (!token) return '';
  const payload = JSON.parse(atob(token.split('.')[1]));
  return payload.sub || '';
}
getPermissionsFromToken(): string[] {
  const token = localStorage.getItem('token');
  if (!token) return [];

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.permissions || payload.authorities || [];
  } catch {
    return [];
  }
}
getUserIdFromToken(): number {
  const token = localStorage.getItem('token');
  if (!token) return 0;

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.userId || 0;   
  } catch {
    return 0;
  }
}

hasAnyReadingPermission(): boolean {
  const perms = JSON.parse(localStorage.getItem('permissions') || '[]') as string[];
  if (perms.includes('ADMIN')) return true; 
  const readingPerms = ['READING_USER', 'READING_MACHINE', 'READING_ERROR','SEARCHING_MACHINE']; 
  return perms.some(p => readingPerms.includes(p));
}
}