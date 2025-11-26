// app.component.ts

import { Component } from '@angular/core';
import { WebSocketService } from './services/websocket.service'; // Uverite se da je putanja ispravna!

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'nvp_client';

  // Injektovanjem servisa u konstruktor, Angular ga automatski inicijalizuje.
  // Inicijalizacija pokreće konstruktor unutar WebSocketService-a, 
  // koji zatim uspostavlja konekciju.
  constructor(private webSocketService: WebSocketService) {
    console.log('AppComponent initialized. WebSocket service is now running.');

    // Opcionalno, možete ovde da se pretplatite da biste videli da li prima poruke.
    this.webSocketService.messages.subscribe(message => {
        console.log('🚀 WebSocket message received:', message);
    });
  }
}