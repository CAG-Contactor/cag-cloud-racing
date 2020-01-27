import { Component, OnInit } from '@angular/core'
import { BackendService } from '../../common-services/backend.service'
import { User } from '../../domain/user.model'
import { catchError, mergeMap, tap } from 'rxjs/operators'
import { of as observableOf } from 'rxjs'
import { MatDialog } from '@angular/material/dialog'
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component'

@Component({
  selector: 'cag-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.scss']
})
export class ManageUsersComponent implements OnInit {
  users: User[]
  loading = true

  constructor(
    private readonly backend: BackendService,
    public readonly dialog: MatDialog
  ) {
  }

  ngOnInit() {
    this.getUsers()
      .subscribe(users => this.users = users)

  }

  iconFor(user: User) {
    switch (user.type) {
      case 'ADMIN':
        return 'supervisor_account'
      default:
        return 'person'
    }
  }

  removeUser(user: User) {
    this.dialog
      .open(ConfirmationDialogComponent, {
        data: {
          title: 'Radera användare',
          message: `Är du säker på att vill radera användaren ${user.name}?`
        }
      })
      .afterClosed()
      .pipe(
        mergeMap(doRemove => {
          return doRemove ? this.backend.deleteUser(user.name) : observableOf(undefined)
        }),
      )
      .subscribe(() => this.getUsers())
  }

  private getUsers() {
    this.loading = true
    return this.backend.fetchRegisteredUsers().pipe(
      tap(() => this.loading = false),
      catchError((e) => {
        this.loading = false;
        throw e;
      })
    )
  }
}
