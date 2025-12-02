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
import { NoAccessComponent } from './no-access/no-access.component';

/* guard-i */
import { AuthGuard } from './services/auth-guard.service';
import { PermissionGuard } from './services/permission-guard.service';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },

  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },

  { path: 'no-access', component: NoAccessComponent },

  /* 2. MANAGEMENT – čitanje user-a */
  {
    path: 'management',
    component: ManagementComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'READING_USER' }
  },

  {
    path: 'add-user',
    component: AddUserComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'CREATING_USER' }
  },

  {
    path: 'create-machine',
    component: CreateMachineComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'CREATING_MACHINE' }
  },

  {
    path: 'search-machines',
    component: SearchComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'READING_MACHINE' }
  },

  {
    path: 'edit-user/:id',
    component: EditUserComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'UPDATE_USER' }
  },

  {
    path: 'error-log',
    component: ErrorComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'READING_ERROR' }
  },

  {
    path: 'schedule/:id',
    component: ScheduleComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'STARTING_MACHINE' }
  },

  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}