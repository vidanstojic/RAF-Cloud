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

/* guard-i */
import { AuthGuard } from './services/auth-guard.service';
import { PermissionGuard } from './services/permission-guard.service';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },

  /* 1. HOME – samo ulogovani */
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },

  /* 2. MANAGEMENT – čitanje user-a */
  {
    path: 'management',
    component: ManagementComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'READING_USER' }
  },

  /* 3. ADD-USER – kreiranje user-a */
  {
    path: 'add-user',
    component: AddUserComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'CREATING_USER' }
  },

  /* 4. CREATE-MACHINE – kreiranje mašine */
  {
    path: 'create-machine',
    component: CreateMachineComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'CREATING_MACHINE' }
  },

  /* 5. SEARCH-MACHINES – čitanje mašina */
  {
    path: 'search-machines',
    component: SearchComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'READING_MACHINE' }
  },

  /* 6. EDIT-USER – izmena user-a */
  {
    path: 'edit-user/:id',
    component: EditUserComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'UPDATE_USER' }
  },

  /* 7. ERROR-LOG – čitanje logova */
  {
    path: 'error-log',
    component: ErrorComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'READING_ERROR' }
  },

  /* 8. SCHEDULE – pokretanje/zakazivanje mašine */
  {
    path: 'schedule/:id',
    component: ScheduleComponent,
    canActivate: [AuthGuard, PermissionGuard],
    data: { permission: 'STARTING_MACHINE' }
  },

  /* 9. 404 – nazad na login */
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}