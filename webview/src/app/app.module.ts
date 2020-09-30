import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule} from '@angular/common/http';

import { AppComponent } from './app.component';
import {FormsModule} from '@angular/forms';
import {MessageComponent} from './message/message.component';
import { AppRoutingModule } from './app-routing.module';
import { PropertyComponent } from './property/property.component';
import { TopicComponent } from './topic/topic.component';
import {RouterModule} from '@angular/router';
import { TemplateLoadComponent } from './message/template-load/template-load.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatListModule} from '@angular/material/list';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatDialogModule} from '@angular/material/dialog';
import { SaveTemplateDialogComponent } from './message/template-load/save-template-dialog/save-template-dialog.component';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import { MessageHistoryComponent } from './message-history/message-history.component';
import { ColorPickerModule} from 'ngx-color-picker';
import {MatTabsModule} from '@angular/material/tabs';
import { MessageFormComponent } from './message/message-form/message-form.component';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import { TopicDescriptionDialogComponent } from './topic/topic-description-dialog/topic-description-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    MessageComponent,
    PropertyComponent,
    TopicComponent,
    TemplateLoadComponent,
    SaveTemplateDialogComponent,
    MessageHistoryComponent,
    SaveTemplateDialogComponent,
    MessageFormComponent,
    TopicDescriptionDialogComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    RouterModule,
    BrowserAnimationsModule,
    MatListModule,
    MatSidenavModule,
    MatDialogModule,
    MatButtonModule,
    MatToolbarModule,
    ColorPickerModule,
    MatTabsModule,
    MatSlideToggleModule
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    RouterModule,
    BrowserAnimationsModule,
    MatListModule,
    MatSidenavModule,
    MatDialogModule,
    MatButtonModule,
    MatToolbarModule,
    ColorPickerModule,
    MatTabsModule,
    MatSlideToggleModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
