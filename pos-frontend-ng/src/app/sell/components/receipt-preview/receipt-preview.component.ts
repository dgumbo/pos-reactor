import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { PDFSource, PDFDocumentProxy } from 'pdfjs-dist';
import { SellPrintService } from 'app/sell/services';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ResponseContentType } from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-receipt-preview',
  templateUrl: './receipt-preview.component.html',
  styleUrls: ['./receipt-preview.component.scss']
})
export class ReceiptPreviewComponent implements OnInit, AfterViewInit {

  @ViewChild('ifrm') ifrm: ElementRef;

  pdfUrl = 'http://localhost:8080/api/pdf-receipt/view/pddoc.pdf';
  pdfSrc: string | PDFSource | ArrayBuffer;

  error: any;
  Url: SafeUrl;
  backgroundImagePicFile: any;
  backgroundImagePicDisplay: any;

  constructor(private sellPrintService: SellPrintService,
    private http: HttpClient,
    private matSnackBar: MatSnackBar) {
    this.pdfSrc = 'http://localhost:8080/api/pdf-receipt/views/pddoc.pdf';
  }

  ngOnInit() {
    console.log('ngOnInit');
    // const rc = 'http://localhost:8080/api/pdf-receipt/view';
    // this.Url = this.sanitizer.bypassSecurityTrustResourceUrl(rc);
  }

  ngAfterViewInit(): void {
    // this.sellPrintService.onDataReady();
    // this.onFileSelected();
    // this.loadPdf();
    console.log('ngAfterViewInit');
    console.log(this.ifrm);

    this.getPDF().subscribe((response) => {

      const file = new Blob([response], { type: 'application/pdf' });
      const fileURL = URL.createObjectURL(file);
      window.open(fileURL);
    });
  }

  getPDF() {
    const url = this.pdfUrl;

    const httpOptions = {
      headers: new HttpHeaders(
        {
          'responseType': 'blob'
          // both combination
          // 'responseType'  : 'arraybuffer'
        }
      )
    };

    return this.http.get<any>(url, httpOptions);

  }

  createPDF() {
    console.log('Sending GET on ' + this.pdfSrc);

    const pdfUrl: string = this.pdfSrc.toString();

    this.http.get(pdfUrl).subscribe(
      (data) => {
        console.log('Data');
        this.handleResponse(data);
      });
  }

  handleResponse(data: any) {
    console.log('[Receipt service] GET PDF byte array ');
    console.log(JSON.stringify(data));

    const file = new Blob([data._body], { type: 'application/pdf' });
    const fileURL = URL.createObjectURL(file);
    window.open(fileURL);
  }

  onIframeLoaded($event) {
    console.log('onIframeLoaded');

    // this.sellPrintService.onDataReady();
  }

  onIframeEnded($event) {
    console.log('onIframeLoaded');
  }

  load() {
    console.log('onIframeLoaded');

    const pdfFrame = window.frames['pdf'];

    pdfFrame.focus();
    pdfFrame.print();
    // this.sellPrintService.onDataReady();
  }

  onFileSelected() {
    const $pdf: any = 'http://localhost:8080/api/pdf-receipt/view/pddoc.pdf';

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
    xhr.open('GET', 'http://localhost:8080/api/pdf-receipt/view/pddoc.pdf', true);
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
