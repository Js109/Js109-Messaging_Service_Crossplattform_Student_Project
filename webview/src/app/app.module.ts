import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule} from '@angular/common/http';

import { AppComponent } from './app.component';
import {FormsModule} from '@angular/forms';
import {MessageComponent} from './message/message.component';
import { AppRoutingModule } from './app-routing.module';
import { PropertyComponent } from './property/property.component';
import { TopicComponent } from './topic/topic.component';
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [
    AppComponent,
    MessageComponent,
    PropertyComponent,
    TopicComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
