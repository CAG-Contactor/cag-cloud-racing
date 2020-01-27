import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs'
import { Router } from '@angular/router'
import { delay, map, tap } from 'rxjs/operators'
import { BackendService } from '../common-services/backend.service'

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  redirectUrl: string

  constructor(private readonly router: Router, private readonly backendService: BackendService) { }

  get isLoggedIn(): boolean {
    return Boolean(localStorage.getItem('cag-tk'));
  }

  initLogin(redirectUrl: string) {
    this.redirectUrl = redirectUrl;
    this.router.navigate(['/login']);
  }

  login(userName: string, password: string): Observable<boolean> {
    return this.backendService.userLogin(userName, password)
      .pipe(
        tap(token =>
          localStorage.setItem('cag-tk', token)
        ),
        map(token => Boolean(token))
      )
    // return of(true).pipe(
    //   delay(1000),
    //   tap(val => {
    //     localStorage.setItem('cag-tk', '12345')
    //   })
    // );
  }

  logout() {
    localStorage.removeItem('cag-tk')
    this.router.navigate(['/login']);
  }
}
