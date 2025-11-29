import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService,
              private router: Router) {}

  login(): void {
    this.authService.login(this.email, this.password).subscribe({
      next: res => {
        this.storeTokenWithExpiry(res.token, 120);      // 120 min
        this.router.navigate(['/home']);
      },
      error: err => {
        console.error(err);
        this.errorMessage = 'Invalid credentials';
        localStorage.clear();
      }
    });
  }

  /* pomoćna – čuva i token i lokalni exp */
  private storeTokenWithExpiry(jwt: string, minutes: number): void {
    const payload = JSON.parse(atob(jwt.split('.')[1]));
    const jwtExp  = payload.exp * 1000;                 // JWT vlastito exp
    const ourExp  = Date.now() + minutes * 60_000;      // naš „lokalni“ exp
    const finalExp = Math.min(jwtExp, ourExp);          // ne duže od JWT-a

    localStorage.setItem('token', jwt);
    localStorage.setItem('token_exp', finalExp.toString());
  }
}