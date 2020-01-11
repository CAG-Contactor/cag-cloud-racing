import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";
import {User} from "./domain/user.model";


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private SERVER_URL = "https://dqgo8v372l.execute-api.eu-central-1.amazonaws.com/cchehu/registered-users";

  constructor(private httpClient: HttpClient) { }

  public getUsers() {
    return this.httpClient.get(this.SERVER_URL);
  }

}
