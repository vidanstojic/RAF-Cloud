import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router, public auth: AuthService) { }

  ngOnInit(): void {
  const perms = this.auth.getPermissions();
    if (perms.length === 0 || this.auth.hasAnyReadingPermission() === false) {
      this.router.navigate(['/no-access']);
    }
  } 

  goToSearchMachines() {
    this.router.navigate(['/search-machines']);
  }
  
}
