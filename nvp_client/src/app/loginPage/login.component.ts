import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService, User } from '../services/user.service';

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

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAll().subscribe({
      next: (data) => this.users = data,
      error: (err) => console.error(err)
    });
  }

  login() {
    this.errorMessage = '';

    const userByEmail = this.users.find(u => u.email === this.email);

    if (!userByEmail) {
      this.errorMessage = 'Email does not exist!';
      return;
    }

    if (userByEmail.password !== this.password) {
      this.errorMessage = 'Incorrect password!';
      return;
    }

    localStorage.setItem('loggedUser', JSON.stringify(userByEmail));

    console.log('Login successful', userByEmail);
    this.router.navigate(['/home']);
  }
}
