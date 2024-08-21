import { CommonModule, NgFor } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, Injectable, OnInit, ViewChild } from '@angular/core';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { error } from 'console';
import { ModalComponent } from './modal/modal.component';
import { Order } from './order';
import { Customer } from '../customer/customer';
import { Item } from '../item/item';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule, ModalComponent, FormsModule],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})

@Injectable({
  providedIn: 'root'
})
export class OrderComponent implements OnInit {
  @ViewChild(ModalComponent) modal: ModalComponent | undefined;
  orders : Observable<{
    orderId: number;
    orderCode: string;
    quantity: number;
    totalPrice: number;
    customer: Customer;
    item: Item;
    orderDate: string;
  }[]> | undefined;
  url : string = "http://localhost:8080/orders";
  pageNumber: number = 0;
  pageSize: number = 10;
  nameFilter: string = "";
  totalPages: number = 0;
  totalCustomer: number = 0;
  constructor(private httpClient : HttpClient, private toastr : ToastrService, private cd: ChangeDetectorRef) {}
  ngOnInit() {
    this.getOrder()
  }

  getOrder(){
    this.orders = this.httpClient.get(this.url, {
      params: {
        pageNumber: this.pageNumber,
        pageSize: this.pageSize,
        name: this.nameFilter
      }
    })
    .pipe(
      map(result => {
        this.totalPages = (result as any).page.totalPages
        this.pageSize = (result as any).page.size
        this.pageNumber = (result as any).page.number
        this.totalCustomer = (result as any).page.totalElements
        return (result as any).content
      }),
      catchError(error => {
        this.toastr.error(error.message, "Error Occured")
        return throwError(() => new Error(error.message))
      })
    )
    this.cd.detectChanges()
  }

  changePage(page: number){
    this.pageNumber = page
    this.getOrder()
  }

  deleteOrder(orderId : number){
    this.httpClient.delete(`${this.url}/${orderId}`, {
      responseType: 'text'
    })
    .subscribe({
      next: (message: String) => {
        this.getOrder()
        this.toastr.success(message as string, "Delete Order Success")
      },
      error: (error) => {
        this.toastr.error(error.message, "Error Occured")
      }
    })
  }

  async openModal(data : any, mode: string) {
    if(this.modal){
      this.modal.mode = mode
      if(data) {
        // this.modal.order = data
        this.modal.order = new Order(data.orderId, data.orderCode, data.orderDate, data.totalPrice, data.quantity, data.customer, data.item)
      }
      if(this.modal.order)
        this.modal.openModal()
    }
  }
}
