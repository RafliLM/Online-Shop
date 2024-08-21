import { CommonModule, NgFor } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, Injectable, OnInit, ViewChild } from '@angular/core';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { error } from 'console';
import { ModalComponent } from './modal/modal.component';
import { Customer } from './customer';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [CommonModule, ModalComponent, FormsModule],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css'
})

@Injectable({
  providedIn: 'root'
})
export class CustomerComponent implements OnInit {
  @ViewChild(ModalComponent) modal: ModalComponent | undefined;
  customers : Observable<{
    customerId: number;
    customerName: string;
    customerAddress: string;
    customerCode: string;
    customerPhone: string;
    pic: string;
    isActive: boolean;
    lastOrderDate: string;
  }[]> | undefined;
  url : string = "http://localhost:8080/customers";
  pageNumber: number = 0;
  pageSize: number = 10;
  nameFilter: string = "";
  totalPages: number = 0;
  totalCustomer: number = 0;
  constructor(private httpClient : HttpClient, private toastr : ToastrService, private cd: ChangeDetectorRef) {}
  ngOnInit() {
    this.getCustomer()
  }

  getCustomer(){
    this.customers = this.httpClient.get(this.url, {
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
    this.getCustomer()
  }

  deleteCustomer(customerId : number){
    this.httpClient.delete(`${this.url}/${customerId}`, {
      responseType: 'text'
    })
    .subscribe({
      next: (message: String) => {
        this.getCustomer()
        this.toastr.success(message as string, "Delete Customer Success")
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
        // this.modal.customer = data
        this.modal.customer = new Customer(data.customerId, data.customerName, data.customerCode, data.customerPhone, data.customerAddress, data.pic, data.isActive, data.lastOrderDate)
        this.modal.imageFile = await this.getFileFromUrl(data.pic, new URL(data.pic).pathname.split('/').at(-1) as string)
      }
        
      if(this.modal.customer)
        this.modal.openModal()
    }
  }

  async getFileFromUrl(url: string, name: string, defaultType = 'image/jpeg'){
    const response = await fetch(url);
    const data = await response.blob();
    return new File([data], name, {
      type: data.type || defaultType,
    });
  }
}
