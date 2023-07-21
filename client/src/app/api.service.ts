import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { firstValueFrom, Observable } from "rxjs";
import { FormField } from "./model";

const URL = 'http://localhost:8080/api/entry'

@Injectable()
export class ApiService {
    http = inject(HttpClient)

    // public login(username:string, password:string){
    //     const headers = new HttpHeaders({
    //         Authorization: 'Basic' + btoa(username + ":" + password)
    //     })
    //     return this.http.get("http://localhost:8080/", {headers})
    // }

    // Send email from google to backend for validation and storage
    postEmailToBackend(email: string): Observable<string> {
        const payload = { email: email }
        return this.http.post<string>(URL, payload)
    }

    // Set data for send for file uploading
    async postFormToBackend(formField: FormField): Promise<any> {
        console.info(formField.file.name)
        const formData = new FormData()
        formData.set('file', formField.file)
        formData.set('date', formField.date.toLocaleString())
        formData.set('description', formField.description)
        formData.set('city', formField.city)
        formData.set('destination', formField.destination)
        const headers = new HttpHeaders().set('Content-Type', 'multipart/form-data')
        return firstValueFrom(this.http.post<any>(URL + '/post', formData))
            .then(p => {
                console.log('Form sent sucessfully, file ID: ', p.id)
            }
            ).catch(
                err => console.error(err)
            )
    }

}