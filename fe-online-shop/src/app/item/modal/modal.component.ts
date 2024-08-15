import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Item } from '../item';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-item-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css'
})
export class ModalComponent {
  @Output() uploaded = new EventEmitter<string>();
  @Input() item: Item = {
    itemId: null,
    itemCode: '',
    itemName: '',
    price: 0,
    stock: 0,
    isAvailable: false,
    lastReStock: null
  };
  @Input() mode: string = "view";
  url : string = "http://localhost:8080/items";
  showModal: boolean = false;
  constructor(private httpClient : HttpClient, private toastr : ToastrService) {}
  openModal() {
    this.showModal = true
  }
  closeModal() {
    this.item = {
      itemId: null,
      itemCode: '',
      itemName: '',
      price: 0,
      stock: 0,
      isAvailable: false,
      lastReStock: null
    }
    this.showModal = false
  }

  submitForm () {
    if (this.mode == 'add'){
      const body = {
        itemName: this.item.itemName,
        stock: this.item.stock,
        price: this.item.price,
        isAvailable: this.item.isAvailable ? this.item.isAvailable : false
      }
      this.httpClient.post(this.url, body, {
        responseType: 'text'
      })
      .subscribe({
        next: (message: string) => {
          this.toastr.success(message, "Add Item Success")
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
        itemName: this.item.itemName,
        stock: this.item.stock,
        price: this.item.price,
        isAvailable: this.item.isAvailable ? this.item.isAvailable : false
      }
      this.httpClient.put(`${this.url}/${this.item.itemId}`, body, {
        responseType: 'text'
      })
      .subscribe({
        next: (message: string) => {
          this.toastr.success(message, "Edit Item Success")
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