import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-user',
  templateUrl: './addUser.component.html',
  styleUrls: ['./addUser.component.css']
})
export class AddUserComponent {

  firstName: string = '';
  lastName: string = '';
  email: string = '';
  permissions: string = '';

  errorMessage: string = '';

  constructor(private router: Router) { }

  addUser() {
    if (!this.firstName || !this.lastName || !this.email || !this.permissions) {
      this.errorMessage = 'Sva polja su obavezna!';
      return;
    }
    this.router.navigate(['/management']);
  }
}
