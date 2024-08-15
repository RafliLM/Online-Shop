import { NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [NgIf, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  showMobileButton: boolean = false;
  loadingButton: boolean = false;
  url : string = "http://localhost:8080/orders/generate-report"
  constructor(private httpClient: HttpClient, private toastr: ToastrService) {}
  downloadOrderReport() {
    this.loadingButton = true;
    this.httpClient.get(this.url, {
      responseType: 'blob' as 'json'
    })
    .subscribe((data) => {
      let blob = new Blob([data as any], {type: 'application/pdf'})
      let downloadUrl = URL.createObjectURL(blob)
      let link = document.createElement('a')
      link.href = downloadUrl
      link.download = `order-report-${new Date().getTime()}.pdf`
      link.click()
      this.toastr.success("Generate Report Success")
      this.loadingButton = false;
    }, 
    catchError(error => {
      this.toastr.error(error.message)
      return throwError(error)
    }))
  }
}
