import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common'
import { MaterialModule } from '../material.module'
import { MainNavComponent } from './main-nav/main-nav.component'
import { LayoutModule } from '@angular/cdk/layout'
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatButtonModule } from '@angular/material/button'
import { MatSidenavModule } from '@angular/material/sidenav'
import { MatIconModule } from '@angular/material/icon'
import { MatListModule } from '@angular/material/list'
import { RouterModule } from '@angular/router'
import { ManageUsersComponent } from './manage-users/manage-users.component'
import { ManageRaceComponent } from './manage-race/manage-race.component'
import { CommonServicesModule } from '../common-services/common-services.module'
import { AppRoutingModule } from '../app-routing/app-routing.module'
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component'


@NgModule({
  declarations: [
    MainNavComponent,
    ManageUsersComponent,
    ManageRaceComponent,
    ConfirmationDialogComponent
  ],
  exports: [
    MainNavComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    RouterModule,
    CommonServicesModule,
    AppRoutingModule
  ],
  entryComponents: [
    ConfirmationDialogComponent
  ]
})
export class ViewComponentsModule {
}
