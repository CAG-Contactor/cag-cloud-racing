import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";
import {User} from "./domain/user.model";


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private SERVER_URL = "https://dqgo8v372l.execute-api.eu-central-1.amazonaws.com/cchehu/";
  private REG_USERS = "registered-users"
  private RACE_QUEUE = "race-queue"

  constructor(private httpClient: HttpClient) { }

  public getUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.SERVER_URL + this.REG_USERS);
  }

  public getRaces(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.SERVER_URL + this.REG_USERS);
  }

}
