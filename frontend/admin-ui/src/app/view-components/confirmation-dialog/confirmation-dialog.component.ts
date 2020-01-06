import { Component, Inject, OnInit } from '@angular/core'
import { MAT_DIALOG_DATA } from '@angular/material/dialog'

@Component({
  selector: 'cag-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent implements OnInit {
  title: string = 'Bekräfta'
  message: string = 'Är du säker?'

  constructor(
    @Inject(MAT_DIALOG_DATA) {title = 'Bekräfta', message = 'Är du säker?'}: { title: string, message: string }
  ) {
    this.title = title
    this.message = message
  }

  ngOnInit() {
  }

}
