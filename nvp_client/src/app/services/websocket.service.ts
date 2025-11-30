import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket!: WebSocket;
  private messageSubject = new Subject<any>();

  constructor() {
    this.connect('ws://localhost:8080/ws/machines');
  }

  public connect(url: string): void {
    this.socket = new WebSocket(url);

    this.socket.onopen = () => {
      console.log('WebSocket connected');
    };

    this.socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      this.messageSubject.next(data);
    };

    this.socket.onclose = () => {
      console.log('WebSocket disconnected');
    };
  }

  public sendMessage(msg: any): void {
    if (this.socket.readyState === WebSocket.OPEN) {
        console.log('Sending WebSocket message:', msg);
      this.socket.send(JSON.stringify(msg));
    }
  }

  public get messages(): Observable<any> {
    return this.messageSubject.asObservable();
  }
}
