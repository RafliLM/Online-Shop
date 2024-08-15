import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Customer } from '../customer';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-customer-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css'
})
export class ModalComponent {
  @ViewChild('pic') pic: ElementRef | undefined;
  @Output() uploaded = new EventEmitter<string>();
  @Input() customer: Customer = {
    customerId: null,
    customerCode: '',
    customerName: '',
    customerAddress: '',
    customerPhone: '',
    pic: '',
    isActive: false,
    lastOrderDate: null
  };
  @Input() mode: string = "view";
  url : string = "http://localhost:8080/customers";
  showModal: boolean = false;
  imageFile: File | null = null;
  constructor(private httpClient : HttpClient, private toastr : ToastrService) {}
  openModal() {
    this.showModal = true
  }
  
  closeModal() {
    this.customer = {
      customerId: null,
      customerCode: '',
      customerName: '',
      customerAddress: '',
      customerPhone: '',
      pic: '',
      isActive: false,
      lastOrderDate: null
    }
    this.imageFile = null
    this.showModal = false
  }

  submitForm () {
    if (this.mode == 'add'){
      let body = new FormData()
      body.append('customerName', this.customer.customerName)
      body.append('customerAddress', this.customer.customerAddress)
      body.append('customerPhone', this.customer.customerPhone)
      body.append('pic', this.imageFile!)
      this.httpClient.post(this.url, body, {
        responseType: 'text'
      })
      .subscribe({
        next: (message: string) => {
          this.toastr.success(message, "Add Customer Success")
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
      let body = new FormData()
      body.append('customerName', this.customer.customerName)
      body.append('customerAddress', this.customer.customerAddress)
      body.append('customerPhone', this.customer.customerPhone)
      body.append('isActive', String(this.customer.isActive))
      body.append('pic', this.imageFile!)
      this.httpClient.put(`${this.url}/${this.customer.customerId}`, body, {
        responseType: 'text'
      })
      .subscribe({
        next: (message: string) => {
          this.toastr.success(message, "Edit Customer Success")
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

  resetFile(){
    this.pic!.nativeElement.value = null
  }

  changeImage(event: Event) {
    let files = (event.target as HTMLInputElement).files;
    if(files != null){
      let file = files.item(0)
      if(file != null){
        const reader = new FileReader();
        // this.customer.pic = file
        // reader.onload = e => console.log(reader.result)
        reader.onload = e => {
          this.customer.pic = reader.result
          this.imageFile = file
        }
        reader.readAsDataURL(file)
      }
    }
  }
}