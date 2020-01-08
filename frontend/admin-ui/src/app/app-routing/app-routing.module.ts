import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { ManageUsersComponent } from '../view-components/manage-users/manage-users.component'
import { ManageRaceComponent } from '../view-components/manage-race/manage-race.component'

const routes: Routes = [
  { path: 'users', component: ManageUsersComponent },
  { path: 'race', component: ManageRaceComponent },
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
