import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";

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

    // to troubleshoot!!!
    // Send email from google to backend for validation and storage
    postEmailToBackend(email: string): Observable<string> {
        const payload = {email : email}
        return this.http.post<string>(URL, payload)
    }

}