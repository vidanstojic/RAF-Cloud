import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService, User } from '../services/user.service';   
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.css']
})
export class ManagementComponent implements OnInit {
  users: User[] = [];          

   constructor(
    private userService: UserService,
    public  auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUsers();          
  }
  deleteUser(id: number): void {
  if (!confirm('Delete this user?')) return;

  this.userService.delete(id).subscribe({
    next: () => {
      this.loadUsers();
    },
    error: err => console.error(err)
  });
}

  private loadUsers(): void {
    this.userService.getAll().subscribe({
      next: data => this.users = data,
      error: err => console.error('Cannot load users', err)
    });
  }

  goToAddUser(): void {
    this.router.navigate(['/add-user']);
  }
}