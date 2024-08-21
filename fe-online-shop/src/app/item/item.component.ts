import { CommonModule, NgFor } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, Injectable, OnInit, ViewChild } from '@angular/core';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { error } from 'console';
import { ModalComponent } from './modal/modal.component';
import { Item } from './item';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-item',
  standalone: true,
  imports: [CommonModule, ModalComponent, FormsModule],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css'
})

@Injectable({
  providedIn: 'root'
})
export class ItemComponent implements OnInit {
  @ViewChild(ModalComponent) modal: ModalComponent | undefined;
  items : Observable<{
    itemId: number;
    itemName: string;
    itemCode: string;
    stock: number;
    price: number;
    isAvailable: boolean;
    lastReStock: string;
  }[]> | undefined;
  url : string = "http://localhost:8080/items";
  pageNumber: number = 0;
  pageSize: number = 10;
  nameFilter: string = "";
  totalPages: number = 0;
  totalCustomer: number = 0;
  constructor(private httpClient : HttpClient, private toastr : ToastrService, private cd: ChangeDetectorRef) {}
  ngOnInit() {
    this.getItem()
  }

  getItem(){
    this.items = this.httpClient.get(this.url, {
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
    this.getItem()
  }

  deleteItem(itemId : number){
    this.httpClient.delete(`${this.url}/${itemId}`, {
      responseType: 'text'
    })
    .subscribe({
      next: (message: String) => {
        this.getItem()
        this.toastr.success(message as string, "Delete Item Success")
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
        // this.modal.item = data
        this.modal.item = new Item(data.itemId, data.itemName, data.itemCode, data.stock, data.price, data.isAvailable, data.lastReStock)
      }
      if(this.modal.item)
        this.modal.openModal()
    }
  }
}
