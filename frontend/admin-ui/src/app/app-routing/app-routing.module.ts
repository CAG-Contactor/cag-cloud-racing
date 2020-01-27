import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { ManageUsersComponent } from '../view-components/manage-users/manage-users.component'
import { ManageRaceComponent } from '../view-components/manage-race/manage-race.component'
import { AuthGuard } from './auth-guard.service'
import { LoginComponent } from '../view-components/login/login.component'

const routes: Routes = [
  { path: 'users', canActivate: [AuthGuard], component: ManageUsersComponent },
  { path: 'race', canActivate: [AuthGuard], component: ManageRaceComponent },
  { path: 'login', component: LoginComponent },
  { path: '',
    redirectTo: '/race',
    pathMatch: 'full'
  },
];


@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
