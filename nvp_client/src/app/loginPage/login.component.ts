import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService, User } from '../services/user.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email: string = '';
  password: string = '';
  users: User[] = [];
  errorMessage: string = '';

  constructor(private authService: AuthService, private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    //this.loadUsers();
  }

  loadUsers() {
    this.userService.getAll().subscribe({
      next: (data) => this.users = data,
      error: (err) => console.error(err)
    });
  }

 login(): void {
  console.log('🔍 Login URL:', `${this.authService.api}/loginuser`);
  this.authService.login(this.email, this.password).subscribe({
    next: res => {
      localStorage.setItem('token', res.token);
      this.router.navigate(['/home']);
    },
    error: err => {
      console.error('❌ Login error:', err);
      this.errorMessage = 'Invalid credentials';
    }
  });
}
}
