import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface User {
  firstName: string;
  lastName: string;
  email: string;
  permissions: string[];
}

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.css']
})
export class ManagementComponent implements OnInit {

  users: User[] = [
    { firstName: 'Marko', lastName: 'Marković', email: 'marko@example.com', permissions: ['Admin', 'Edit'] },
    { firstName: 'Jelena', lastName: 'Jovanović', email: 'jelena@example.com', permissions: ['View'] },
    { firstName: 'Petar', lastName: 'Petrović', email: 'petar@example.com', permissions: ['Edit', 'View'] },
    { firstName: 'Ana', lastName: 'Anić', email: 'ana@example.com', permissions: ['Admin'] }
  ];

  constructor(private router: Router) { }

  ngOnInit(): void { }

  goToAddUser() {
    this.router.navigate(['/add-user']);
  }
}
