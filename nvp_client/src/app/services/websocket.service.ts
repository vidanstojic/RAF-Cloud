import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { AuthService } from  '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket: WebSocket | null = null;
  private messageSubject = new Subject<any>();
  
  private readonly BASE_URL = 'ws://localhost:8080/ws/machines';

  constructor( private auth: AuthService) {
  }

  public connect(token: string): void {
    if (this.socket && (this.socket.readyState === WebSocket.OPEN || this.socket.readyState === WebSocket.CONNECTING)) {
      console.log('WebSocket is already connected.');
      return;
    }

    console.log('Attempting to connect to WebSocket with token...');

    const url = `${this.BASE_URL}?token=${token}`;

    this.socket = new WebSocket(url);

    this.socket.onopen = () => {
      console.log('WebSocket connected');
    };

    this.socket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        this.messageSubject.next(data);
      } catch (e) {
        console.error('Error parsing message:', e);
      }
    };

    this.socket.onclose = (event) => {
      this.auth.logout();   
      console.log('WebSocket disconnected', event);
      this.socket = null;
    };

    this.socket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };
  }

  public sendMessage(msg: any): void {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      console.log('Sending WebSocket message:', msg);
      this.socket.send(JSON.stringify(msg));
    } else {
      console.warn('Cannot send message: WebSocket is not connected.');
    }
  }

  public disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }

  public get messages(): Observable<any> {
    return this.messageSubject.asObservable();
  }
}