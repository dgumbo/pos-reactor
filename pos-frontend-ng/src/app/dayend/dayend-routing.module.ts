import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DayendHomeComponent } from './components/dayend-home/dayend-home.component';
import { DayendFormComponent } from './components/dayend-form/dayend-form.component';
import { DayendListComponent } from './components/dayend-list/dayend-list.component';

const routes: Routes = [
  { path: '', component: DayendHomeComponent },
  { path: 'dayend-list', component: DayendListComponent },
  { path: 'dayend-form', component: DayendFormComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DayendRoutingModule { }
