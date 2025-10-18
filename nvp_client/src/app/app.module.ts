import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './loginPage/login.component';
import { HomeComponent } from './homePage/home.component';
import { ManagementComponent } from './managementPage/management.component';
import { AddUserComponent } from './addUserPage/addUser.component';
//import { SearchComponent } from './searchPage/search.component';
//import { ManagementComponent } from './management/management.component';
//import { ErrorComponent } from './errorPage/error.component';
import { CreateMachineComponent } from './creatingMachinePage/machine.component';
import { ReactiveFormsModule } from '@angular/forms'; 

import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
 //   SearchComponent,
    ManagementComponent,
    AddUserComponent,
    CreateMachineComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
