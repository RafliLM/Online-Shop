import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Order } from '../order';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { Customer } from '../../customer/customer';
import { CustomerComponent } from '../../customer/customer.component';
import { catchError, map, Observable, throwError } from 'rxjs';
import { Item } from '../../item/item';
import { ItemComponent } from '../../item/item.component';

@Component({
  selector: 'app-order-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css'
})
export class ModalComponent {
  @Output() uploaded = new EventEmitter<string>();
  @Input() order: Order = {
    orderId: null,
    orderCode: '',
    orderDate: null,
    totalPrice: 0,
    quantity: 0,
    customer: null,
    item: null,
  };
  @Input() mode: string = "view";
  url : string = "http://localhost:8080/orders";
  showModal: boolean = false;
  customers: Observable<Customer[]> | undefined;
  items: Observable<Item[]> | undefined;
  constructor(private httpClient : HttpClient, private toastr : ToastrService, private cd: ChangeDetectorRef, private customerComponent: CustomerComponent, private itemComponent : ItemComponent) {}
  openModal() {
    this.customers = this.httpClient.get<Customer[]>(this.customerComponent.url)
    .pipe(
      map(result => {
        result = result.filter((customer) => {
          return customer.isActive
        })
        return result
      }),
      catchError(error => {
        this.toastr.error(error.message, "Error Occured")
        return throwError(() => new Error(error.message))
      })
    )
    this.items = this.httpClient.get<Item[]>(this.itemComponent.url)
    .pipe(
      map(result => {
        result = result.filter((item) => {
          return item.isAvailable
        })
        return result
      }),
      catchError(error => {
        this.toastr.error(error.message, "Error Occured")
        return throwError(() => new Error(error.message))
      })
    )
    this.cd.detectChanges()
    this.showModal = true
  }
  closeModal() {
    this.order = {
      orderId: null,
      orderCode: '',
      orderDate: null,
      totalPrice: 0,
      quantity: 0,
      customer: null,
      item: null,
    }
    this.showModal = false
  }

  submitForm () {
    console.log(this.order)
    if (this.mode == 'add'){
      const body = {
        customerId: this.order.customer?.customerId,
        itemId: this.order.item?.itemId,
        quantity: this.order.quantity
      }
      this.httpClient.post(this.url, body, {
        responseType: 'text'
      })
      .subscribe({
        next: (message: string) => {
          this.toastr.success(message, "Add Order Success")
          this.uploaded.emit()
          this.closeModal()
        },
        error: (error) => {
          if(error.error)
            this.toastr.error(error.error)
          else
            this.toastr.error(error.message)
          this.closeModal()
        }
      })
    }
    if (this.mode == 'edit'){
      const body = {
        itemId: this.order.item?.itemId,
        quantity: this.order.quantity
      }
      this.httpClient.put(`${this.url}/${this.order.orderId}`, body, {
        responseType: 'text'
      })
      .subscribe({
        next: (message: string) => {
          this.toastr.success(message, "Edit Order Success")
          this.uploaded.emit()
          this.closeModal()
        },
        error: (error) => {
          if(error.error)
            this.toastr.error(error.error)
          else
            this.toastr.error(error.message)
          this.closeModal()
        }
      })
    }
  }
}