import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
/* ---------- page components ---------- */
import { LoginComponent } from './loginPage/login.component';
import { HomeComponent } from './homePage/home.component';
import { ManagementComponent } from './managementPage/management.component';
import { AddUserComponent } from './addUserPage/addUser.component';
import { ErrorComponent } from './errorPage/error.component';
import { CreateMachineComponent } from './creatingMachinePage/machine.component';
import { SearchComponent } from './searchPage/search.component'
import { EditUserComponent } from './editUserPage/edit-user/edit-user.component';
import { HeaderComponent } from './components/header/header.component';
import { ScheduleComponent } from './schedulerPage/schedule.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NoAccessComponent } from './no-access/no-access.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    SearchComponent,
    ManagementComponent,
    AddUserComponent,
    CreateMachineComponent,
    ErrorComponent,
    EditUserComponent,
    HeaderComponent,
    ScheduleComponent,
    NoAccessComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,          // <- provides HttpClient
    FormsModule,               // <- provides ngModel
    ReactiveFormsModule,       // <- provides formGroup
    AppRoutingModule   ,        // <- provides router-outlet (already exports RouterModule)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
