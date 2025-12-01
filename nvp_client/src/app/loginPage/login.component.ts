import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { WebSocketService } from '../services/websocket.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private webSocketService: WebSocketService
  ) {}

  login(): void {
    console.log('Attempting login for', this.email);
    this.authService.login(this.email, this.password).subscribe({
      next: res => {
        this.storeTokenWithExpiry(res.token, 120);
        console.log('Login successful, token stored.');
        this.webSocketService.connect(res.token);

        this.router.navigate(['/home']);
      },
      error: err => {
        console.log('Login failed:', err);
        console.error(err);
        this.errorMessage = 'Invalid credentials';
        localStorage.clear();
      }
    });
  }

  private storeTokenWithExpiry(jwt: string, minutes: number): void {
    const payload = JSON.parse(atob(jwt.split('.')[1]));
    const jwtExp  = payload.exp * 1000;
    const ourExp  = Date.now() + minutes * 60_000;
    const finalExp = Math.min(jwtExp, ourExp);

    localStorage.setItem('token', jwt);
    localStorage.setItem('token_exp', finalExp.toString());
  }
}