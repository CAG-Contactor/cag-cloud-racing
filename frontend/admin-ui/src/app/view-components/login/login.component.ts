import { Component, OnInit } from '@angular/core'
import { AuthService } from '../../app-routing/auth.service'
import { Router } from '@angular/router'
import { HttpErrorResponse } from '@angular/common/http'

@Component({
  selector: 'cag-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  name: string
  password: string
  errorMessage: string
  waiting: boolean

  constructor(public readonly authService: AuthService, private readonly router: Router) {
  }

  ngOnInit() {
  }

  login() {
    this.errorMessage = undefined
    this.waiting = true
    this.authService.login(this.name, this.password).pipe(
    ).subscribe(() => {
        this.waiting = false
        if (this.authService.isLoggedIn) {
          // Get the redirect URL from our auth service
          // If no redirect has been set, use the default
          let redirect = this.authService.redirectUrl ? this.router.parseUrl(this.authService.redirectUrl) : '/race'

          // Redirect the user
          this.router.navigateByUrl(redirect)
        }
      },
      (error: HttpErrorResponse) => {
        this.waiting = false
        if (error.status === 401) {
          this.errorMessage = 'Ogiltiga inloggningsuppgifter'
        } else {
          this.errorMessage = error.message
        }

      })
  }
}
