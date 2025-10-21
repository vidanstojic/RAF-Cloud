import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './addUser.component.html',
  styleUrls: ['./addUser.component.css']
})
export class AddUserComponent {
  firstName = '';
  lastName = '';
  email = '';
  permissions = '';
  errorMessage = '';
  password = ''

  constructor(private userService: UserService,
              private router: Router) {
  }

  addUser(): void {
    if (!this.firstName || !this.lastName || !this.email || !this.permissions) {
      this.errorMessage = 'All fields are required!';
      if (!this.firstName || !this.lastName || !this.email || !this.permissions || !this.password) {
        this.errorMessage = 'Sva polja su obavezna!';
        return;
      }

      const dto = {
        firstName: this.firstName.trim(),
        lastName: this.lastName.trim(),
        email: this.email.trim(),

        permissions: this.permissions
          .split(',')
          .map(p => p.trim())
          .filter(p => p.length > 0),

        password: this.password
      };

      this.userService.create(dto).subscribe({
        next: () => this.router.navigate(['/management']),
        error: err => {
          console.error(err);
          this.errorMessage = 'Error saving user.';
        }
      });
    }
  }
}
