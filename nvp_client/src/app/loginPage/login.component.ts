import { Component, OnInit } from '@angular/core';
import {timeout} from "rxjs/operators";
import {Router} from "@angular/router";
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  
  email: string = '';
  password: string = '';

  constructor() { }

  ngOnInit(): void {
  }

  login() {
    console.log('Email:', this.email);
    console.log('Password:', this.password);
  }
}
