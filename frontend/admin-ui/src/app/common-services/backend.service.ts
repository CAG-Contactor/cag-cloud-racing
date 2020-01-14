import { Injectable } from '@angular/core'
import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { environment } from '../../environments/environment'
import { Observable, of as observableOf, ReplaySubject, Subject } from 'rxjs'
import { catchError, filter, map, tap } from 'rxjs/operators'
import { User } from '../domain/user.model'
import { Race } from '../domain/race.model'

export interface WsEvent {
  type: 'open' | 'close' | 'error' | 'message',
  messageType?: string,
  payload?: any
}

@Injectable({
  providedIn: 'root'
})
export class BackendService {
  private webSocket: WebSocket
  private wsSubject: Subject<WsEvent> = new ReplaySubject(1)

  constructor(private readonly httpClient: HttpClient) {
    console.log('Environment:', environment)
    this.webSocket = new WebSocket(environment.WSEndpoint)
    this.webSocket.onopen = (e: Event) => {
      console.log('Connected to', environment.WSEndpoint, 'event', e)
      this.wsSubject.next({type: 'open'})
    }
    this.webSocket.onclose = (e: CloseEvent) => {
      console.log('Closed', environment.WSEndpoint, 'event', e)
      this.wsSubject.next({type: 'close'})
    }
    this.webSocket.onerror = (e: Event) => {
      console.log('Error to', environment.WSEndpoint, 'event', e)
      this.wsSubject.next({type: 'error', payload: e})
    }
    this.webSocket.onmessage = (e: MessageEvent) => {
      console.log('Error to', environment.WSEndpoint, 'event', e)
      this.wsSubject.next({
        type: 'message',
        messageType: e.type,
        payload: e.data
      })
    }
  }

  fetchRegisteredUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(`${environment.APIEndpoint}/registered-users`)
  }

  deleteUser(name: string): Observable<void> {
    return this.httpClient
      .delete(`${environment.APIEndpoint}/registered-user/${name}`,
        {responseType: 'text'}
      ).pipe(
        map(() => undefined)
      )
  }

  fetchRaceState(): Observable<string> {
    return this.httpClient.get<Race>(`${environment.APIEndpoint}/current-race`)
      .pipe(
        map(r => r.raceStatus),
        catchError((e: HttpErrorResponse) => {
          if (e.status === 404) {
            return observableOf('NO_ACTIVE_RACE')
          } else {
            throw e
          }
        }),
        tap(x => console.log('race status', x))
      )
  }

  armRace(): Observable<void> {
    return this.httpClient.post<void>(`${environment.APIEndpoint}/current-race`, undefined)
  }

  wsMessages(): Observable<{ messageType: string, payload: any }> {
    return this.wsSubject.asObservable().pipe(
      filter(wsMessage => wsMessage.type === 'message'),
      map(({messageType, payload}) => ({messageType, payload}))
    )
  }
}
