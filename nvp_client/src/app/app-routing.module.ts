import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./loginPage/login.component";
import {HomeComponent} from "./homePage/home.component";
import {ManagementComponent} from "./managementPage/management.component";
import {AddUserComponent} from "./addUserPage/addUser.component";
import { CreateMachineComponent } from './creatingMachinePage/machine.component';
import { SearchComponent } from './searchPage/search.component';
import { EditUserComponent } from './editUserPage/edit-user/edit-user.component';
import { ErrorComponent } from './errorPage/error.component';
const routes: Routes = [
  {
    path: "",
    component: LoginComponent
  },
  {
    path: "home",
    component: HomeComponent
  },
  { path: 'management',
    component: ManagementComponent },
  { path: 'add-user',
    component: AddUserComponent
  },
  {
    path: 'create-machine',
    component: CreateMachineComponent
  },
  {
    path: 'search-machines',
    component: SearchComponent
  },
  {
     path: 'edit-user/:id', component: EditUserComponent
  },
  { path: 'error', component: ErrorComponent }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
