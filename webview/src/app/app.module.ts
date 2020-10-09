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
import {EditMessageDialogComponent} from './message-history/edit-message-dialog/edit-message-dialog.component';
import {MatIconModule} from '@angular/material/icon';
import { ViewMessageDialogComponent } from './message-history/view-message-dialog/view-message-dialog.component';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {NgxMatDatetimePickerModule} from '@angular-material-components/datetime-picker';
import {NgxMatMomentModule} from '@angular-material-components/moment-adapter';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { ElektrobitDatePickerComponent } from './elektrobit-date-picker/elektrobit-date-picker.component';

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
    TopicDescriptionDialogComponent,
    EditMessageDialogComponent,
    ViewMessageDialogComponent,
    ElektrobitDatePickerComponent
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
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSlideToggleModule,
    MatDatepickerModule,
    NgxMatDatetimePickerModule,
    NgxMatMomentModule,
    MatIconModule,
    MatButtonToggleModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
