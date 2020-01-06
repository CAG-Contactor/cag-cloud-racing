import { Component, OnInit } from '@angular/core'
import { BackendService } from '../../common-services/backend.service'
import { User } from '../../domain/user.model'
import { mergeMap } from 'rxjs/operators'
import { Observable, of as observableOf } from 'rxjs'
import { MatDialog } from '@angular/material/dialog'
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component'

@Component({
  selector: 'cag-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.scss']
})
export class ManageUsersComponent implements OnInit {
  users$: Observable<User[]>

  constructor(
    private readonly backend: BackendService,
    public readonly dialog: MatDialog
  ) {
  }

  ngOnInit() {
    this.users$ = this.backend.fetchRegisteredUsers()
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
      .subscribe(users => this.users$ = this.backend.fetchRegisteredUsers())
  }
}
