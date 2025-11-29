import { Component } from '@angular/core';
import { WebSocketService } from './services/websocket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'nvp_client';
  constructor(private webSocketService: WebSocketService) {
    console.log('AppComponent initialized. WebSocket service is now running.');

    this.webSocketService.messages.subscribe(message => {
        console.log('WebSocket message received:', message);
    });
  }
}