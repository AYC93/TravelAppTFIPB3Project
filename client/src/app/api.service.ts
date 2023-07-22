import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { firstValueFrom, Observable } from "rxjs";
import { CombinedModel, FormField, ServerEmail } from "./models/model";
import { LocalStorageService } from "./localstorage.service";

const URL_ENTRY = 'http://localhost:8080/api/entry'
const URL_MAIN = 'http://localhost:8080/api/main'

@Injectable()
export class ApiService {
    http = inject(HttpClient)
    local=inject(LocalStorageService)

    // public login(username:string, password:string){
    //     const headers = new HttpHeaders({
    //         Authorization: 'Basic' + btoa(username + ":" + password)
    //     })
    //     return this.http.get("http://localhost:8080/", {headers})
    // }

    // Send email from google to backend for validation and storage
    postEmailToBackend(email: string): Observable<ServerEmail> {
        const payload = { email: email }
        return this.http.post<ServerEmail>(URL_ENTRY, payload)
    }

    // Set data for send for file uploading
    async postFormToBackend(formField: FormField, email: string): Promise<any> {
        console.info(formField.file.name)
        const formData = new FormData()
        formData.set('file', formField.file)
        formData.set('date', formField.date.toLocaleString())
        formData.set('description', formField.description)
        formData.set('city', formField.city)
        formData.set('destination', formField.destination)
        formData.set('email', email)
        const headers = new HttpHeaders().set('Content-Type', 'multipart/form-data')
        return firstValueFrom(this.http.post<any>(URL_ENTRY + '/post', formData))
            .then(p => {
                console.log('Form sent sucessfully, file URL: ', p.url)
            }
            ).catch(
                err => console.error(err)
            )
    }

    getDataFromServer(emailId: number):Promise<CombinedModel[]>{
        emailId = this.local.emailIdLocalPull()
        const params = new HttpParams()
                        .set('emailId', emailId)
        return firstValueFrom(this.http.get<CombinedModel[]>(URL_MAIN, {params}))
    }

}