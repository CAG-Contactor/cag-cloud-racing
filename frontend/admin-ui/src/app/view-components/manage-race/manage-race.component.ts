import { Component, OnInit } from '@angular/core'
import { BackendService } from '../../common-services/backend.service'
import { mergeMap } from 'rxjs/operators'

@Component({
  selector: 'cag-manage-race',
  templateUrl: './manage-race.component.html',
  styleUrls: ['./manage-race.component.scss']
})
export class ManageRaceComponent implements OnInit {
  private raceStatus: string

  constructor(private readonly backendService: BackendService) {
  }

  ngOnInit() {
    this.backendService.fetchRaceState()
      .subscribe(status => this.raceStatus = status)
  }

  get raceIsIdle() {
    return this.raceStatus === 'IDLE'
  }

  armRace() {
    this.backendService.armRace()
      .pipe(
        mergeMap(() => this.backendService.fetchRaceState())
      )
      .subscribe(status => this.raceStatus = status)
  }
}
