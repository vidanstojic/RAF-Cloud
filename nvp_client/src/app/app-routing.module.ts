import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

/* komponente */
import { LoginComponent } from './loginPage/login.component';
import { HomeComponent } from './homePage/home.component';
import { ManagementComponent } from './managementPage/management.component';
import { AddUserComponent } from './addUserPage/addUser.component';
import { CreateMachineComponent } from './creatingMachinePage/machine.component';
import { SearchComponent } from './searchPage/search.component';
import { EditUserComponent } from './editUserPage/edit-user/edit-user.component';
import { ErrorComponent } from './errorPage/error.component';
import { ScheduleComponent } from './schedulerPage/schedule.component';

/* guard */
import { AuthGuard } from './services/auth-guard.service'

const routes: Routes = [
  /* podrazumevana ruta – login stranica */
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  /* javno dostupno */
  { path: 'login', component: LoginComponent },

  /* zaštićene stranice (samo ulogovani) */
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'management',
    component: ManagementComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'add-user',
    component: AddUserComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'create-machine',
    component: CreateMachineComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'search-machines',
    component: SearchComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'edit-user/:id',
    component: EditUserComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'error-log',
    component: ErrorComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'schedule/:id',
    component: ScheduleComponent,
    canActivate: [AuthGuard]
  },

  /* 404 – vrati na login */
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}