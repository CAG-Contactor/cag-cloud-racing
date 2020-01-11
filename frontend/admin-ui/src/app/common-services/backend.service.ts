import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { Observable, of as observableOf } from 'rxjs'
import { User } from '../domain/user.model'
import { filter, map, range } from 'lodash'
import * as faker from 'faker'
import {ApiService} from "../api.service";


@Injectable({
  providedIn: 'root'
})
export class BackendService {
  //private users = map(range(10), n => ({name: faker.name.firstName(), password:  'xxxx', type: faker.random.number(10) % 0 === 0 ? 'ADMIN' : 'CONTESTANT'}))
  private users = []

  private raceState: string = 'IDLE'

  constructor(private apiService: ApiService) { }

 // constructor(private readonly httpClient: HttpClient) {}

  fetchRegisteredUsers(): Observable<User[]> {
    console.log('fetch users:', this.users)
    this.apiService.getUsers().subscribe((data: User[])=>{
      console.log(data);
      this.users = data;
    })

    console.log('fetched users:', this.users)

    return observableOf(this.users)
  }

  deleteUser(name: string): Observable<void> {
    console.log('remove user:', name)
    this.users = filter(this.users, u => u.name !== name)
    return observableOf(undefined)
  }

  fetchRaceState(): Observable<string> {
    console.log('fetch race state:', this.raceState)
    return observableOf(this.raceState)
  }

  armRace(): Observable<void> {
    console.log('arm race')
    this.raceState = 'ARMED'
    return observableOf(undefined)
  }

}
