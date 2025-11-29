import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    const token = localStorage.getItem('token');
    const exp   = localStorage.getItem('token_exp');
    const now   = Date.now();

    if (!token || !exp || Number(exp) < now) {
      localStorage.clear();
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}