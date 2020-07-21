import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CustomerListComponent } from './pages/customer-list/customer-list.component';

const routes: Routes = [
  { path: 'list', component: CustomerListComponent },
  //{ path: 'customer/:id', component: CustomerDetailComponent },
  { path: '',   redirectTo: '/list', pathMatch: 'full' }, // redirect to `first-component`
  { path: '**', component: CustomerListComponent },  // Wildcard route for a 404 page
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
