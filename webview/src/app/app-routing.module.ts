import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MessageComponent} from './message/message.component';
import {PropertyComponent} from './property/property.component';
import {TopicComponent} from './topic/topic.component';
import {MessageHistoryComponent} from './message-history/message-history.component';
import {StatisticsComponent} from './statistics/statistics.component';

const routes: Routes = [
  { path: '', redirectTo: '/message', pathMatch: 'full'},
  { path: 'message', component: MessageComponent },
  { path: 'message_history', component: MessageHistoryComponent},
  { path: 'property', component: PropertyComponent },
  { path: 'topic', component: TopicComponent},
  { path: 'statistics', component: StatisticsComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ]
})
export class AppRoutingModule { }
