import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User, UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  user!: User;
  permissions = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.userService.getAll().subscribe(list => {
      this.user = list.find(u => u.id === id)!;
      this.permissions = this.user.permissions.join(', ');
    });
  }

  save(): void {
  this.user.permissions = this.permissions
                               .split(',')
                               .map(p => p.trim())
                               .filter(p => p);

this.userService.update(this.user.id!, this.user).subscribe({
  next: (res: any) => {
    const loggedId = this.authService.getUserIdFromToken();
    const updatedId = this.user.id;

    if (loggedId === updatedId && res.token) {
      // Menjao sam sebe → osveži token i localStorage
      localStorage.setItem('token', res.token);
      const newPerms = this.authService.getPermissionsFromToken();
      localStorage.setItem('permissions', JSON.stringify(newPerms));
    } else {
      // Menjao sam drugog → samo obavesti
      console.log('Korisniku su ažurirane dozvole.');
    }

    this.router.navigate(['/home']);
  },
  error: err => console.error(err)
});
}}
