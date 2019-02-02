import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { PDFSource, PDFDocumentProxy } from 'pdfjs-dist';
import { SellPrintService } from 'app/sell/services';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ResponseContentType } from '@angular/http';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-receipt-preview',
  templateUrl: './receipt-preview.component.html',
  styleUrls: ['./receipt-preview.component.scss']
})
export class ReceiptPreviewComponent implements OnInit, AfterViewInit {

  @ViewChild('ifrm') ifrm: ElementRef;

  pdfSrc: string | PDFSource | ArrayBuffer;

  error: any;
  Url: SafeUrl;

  constructor(private sellPrintService: SellPrintService,
    private http: HttpClient, ) {
    this.pdfSrc = 'http://localhost:8080/api/pdf-receipt/view';
  }

  ngOnInit() {
    // const rc = 'http://localhost:8080/api/pdf-receipt/view';
    // this.Url = this.sanitizer.bypassSecurityTrustResourceUrl(rc);
  }

  ngAfterViewInit(): void {
    // this.onFileSelected();
    // this.loadPdf();
    console.log('ifrm');
    console.log(this.ifrm);

    // const rc = 'http://localhost:8080/api/pdf-receipt/view';
    // this.http.get(rc , {
    //   responseType: ResponseContentType.Blob
    // } ).subscribe(
    //   (response: any) => { // download file
    //     const blob = new Blob([response.blob()], { type: 'application/pdf' });
    //     const blobUrl = URL.createObjectURL(blob);
    //     // const iframe = document.createElement('iframe');
    //     const iframe = this.ifrm.nativeElement;
    //     iframe.style.display = 'none';
    //     iframe.src = blobUrl;
    //     document.body.appendChild(iframe);
    //     iframe.contentWindow.print();
    //   });
    // this.sellPrintService.onDataReady();
  }

  onLoaded() {
    console.log('tesrffffffmffmm');

    const pdfFrame = window.frames['pdf'];

    pdfFrame.focus();
    pdfFrame.print();
    // this.sellPrintService.onDataReady();
  }

  onFileSelected() {
    const $pdf: any = 'http://localhost:8080/api/pdf-receipt/view';

    if (typeof FileReader !== 'undefined') {
      const reader = new FileReader();

      reader.onload = (e: any) => {
        this.pdfSrc = e.target.result;
      };

      reader.readAsArrayBuffer($pdf);
    }
  }

  loadPdf() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/api/pdf-receipt/view', true);
    xhr.responseType = 'blob';

    xhr.onload = (e: any) => {
      console.log(xhr);
      if (xhr.status === 200) {
        const blob = new Blob([xhr.response], { type: 'application/pdf' });
        this.pdfSrc = URL.createObjectURL(blob);
      }
    };

    xhr.send();
  }

  onError(error: any) {
    this.error = error; // set error

    if (error.name === 'PasswordException') {
      const password = prompt(
        'This document is password protected. Enter the password:'
      );

      if (password) {
        this.error = null;
        this.setPassword(password);
      }
    }
  }

  setPassword(password: string) {
    let newSrc;

    if (this.pdfSrc instanceof ArrayBuffer) {
      newSrc = { data: this.pdfSrc };
    } else if (typeof this.pdfSrc === 'string') {
      newSrc = { url: this.pdfSrc };
    } else {
      newSrc = { ...this.pdfSrc };
    }

    newSrc.password = password;

    this.pdfSrc = newSrc;
  }

  afterLoadComplete(pdf: PDFDocumentProxy) {
    console.log('load-complete');
  }

  pageRendered(e: CustomEvent) {
    console.log('page-rendered', e);
    this.sellPrintService.onDataReady();
  }

}
