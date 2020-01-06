import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ViewComponentsModule } from './view-components/view-components.module'
import { AppRoutingModule } from './app-routing/app-routing.module'
import { CommonServicesModule } from './common-services/common-services.module'

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ViewComponentsModule,
    AppRoutingModule,
    CommonServicesModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
