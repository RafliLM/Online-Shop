import { Routes } from '@angular/router';
import { CustomerComponent } from './customer/customer.component';
import { ItemComponent } from './item/item.component';
import { OrderComponent } from './order/order.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '/customer',
        pathMatch: 'full'
    },
    {
        path: 'customer', 
        component: CustomerComponent,
        pathMatch: 'full'
    },
    {
        path: 'item',
        component: ItemComponent,
        pathMatch: 'full'
    },
    {
        path: 'order',
        component: OrderComponent,
        pathMatch: 'full'
    }
];
