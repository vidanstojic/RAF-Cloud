import { Component, OnInit } from '@angular/core';
import { WebSocketService } from './services/websocket.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'nvp_client';

  constructor(private webSocketService: WebSocketService, public router: Router) {
  }

  ngOnInit(): void {
    console.log('AppComponent initialized.');

    const token = localStorage.getItem('token');
    const exp = localStorage.getItem('token_exp');
    const now = Date.now();

    const isValid = token && exp && Number(exp) > now;

    if (isValid && token) {
      console.log('User already logged in, connecting to WebSocket...');
      this.webSocketService.connect(token);
    } else {
      console.log('No valid session found, waiting for login to connect WebSocket.');
    }

    this.webSocketService.messages.subscribe(message => {
        console.log('WebSocket message received:', message);
    });
  }
}