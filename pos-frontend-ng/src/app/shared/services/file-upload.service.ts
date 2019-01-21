import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'environments/environment';

@Injectable()
export class FileUploadService {
    hostUrl: string = environment.apiUrl;
    uploadUrl: string = this.hostUrl + '/storage/upload';

    constructor(private httpClient: HttpClient) { }

    uploadFileToStorage(file: File): Observable<any> {

        const formdata: FormData = new FormData();
        formdata.append('file', file);

        return this.httpClient.post(this.uploadUrl, formdata, {
            reportProgress: true,
            responseType: 'text'
        });
    }

    pushFileToStorage(file: File): Observable<HttpEvent<{}>> {
        const formdata: FormData = new FormData();

        formdata.append('file', file);

        const req = new HttpRequest('POST', this.uploadUrl, formdata, {
            reportProgress: true,
            responseType: 'text'
        });

        return this.httpClient.request(req);
    }

    getFiles(): Observable<any> {
        return this.httpClient.get(this.hostUrl + '/storage/getallfiles')
    }

    downloadImage(filename: string): File {
        const downFileUrl: string = this.hostUrl + '/storage/download/' + filename;
        console.log('\n\ndownFileUrl :' + downFileUrl);

        return new File([this.dataURItoBlob(downFileUrl)], filename);
    }

    private dataURItoBlob(dataURI: string): Blob {
        const binary = atob(dataURI.split(',')[1]);
        const array = [];
        for (let i = 0; i < binary.length; i++) {
            array.push(binary.charCodeAt(i));
        }
        return new Blob([new Uint8Array(array)], { type: 'image/jpeg' });
    }

    public getFilesList(filelistUrl: string) {
        return this.httpClient.get(filelistUrl);
    }
}