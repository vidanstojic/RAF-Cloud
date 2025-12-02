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
    if (res.token) {
      localStorage.setItem('token', res.token); // ✅ osveži token
    }

    // Osveži permisije iz novog tokena
    const newPerms = this.authService.getPermissionsFromToken();
    localStorage.setItem('permissions', JSON.stringify(newPerms));

    this.router.navigate(['/home']);
  },
  error: err => console.error(err)
});
}
}
