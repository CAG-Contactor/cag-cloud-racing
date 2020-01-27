import { Component, OnInit } from '@angular/core'
import { BackendService } from '../../common-services/backend.service'
import { mergeMap, tap } from 'rxjs/operators'
import { Race } from '../../domain/race.model'

@Component({
  selector: 'cag-manage-race',
  templateUrl: './manage-race.component.html',
  styleUrls: ['./manage-race.component.scss']
})
export class ManageRaceComponent implements OnInit {
  raceStatus: string
  loading = true
  queue: Race[]
  wsMessages$ = this.backendService.wsMessages().pipe(
    tap(() => {
        this.backendService.getQueue()
          .subscribe(q => this.queue = q)
        this.backendService.fetchRaceState()
          .subscribe(status => {
              this.raceStatus = status
              this.loading = false
            },
            (e) => {
              this.loading = false
            })
      }
    )
  )

  constructor(private readonly backendService: BackendService) {

  }

  ngOnInit() {
    this.backendService.getQueue()
      .subscribe(q => this.queue = q)
    this.backendService.fetchRaceState()
      .subscribe(status => {
          this.raceStatus = status
          this.loading = false
        },
        (e) => {
          this.loading = false
        })
  }

  get raceIsActive() {
    return this.raceStatus === 'ARMED'
      || this.raceStatus === 'STARTED'
  }

  armRace() {
    this.loading = true
    this.backendService.armRace()
      .pipe(
        mergeMap(() => this.backendService.fetchRaceState())
      )
      .subscribe(status => {
        this.raceStatus = status
        this.loading = false
      })
  }
}
