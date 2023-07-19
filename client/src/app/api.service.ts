import { Inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";

const URL='http://localhost:8080/api/entry'

@Injectable()
export class ApiService{

   

    http = Inject(HttpClient)
    
    // public login(username:string, password:string){
    //     const headers = new HttpHeaders({
    //         Authorization: 'Basic' + btoa(username + ":" + password)
    //     })
    //     return this.http.get("http://localhost:8080/", {headers})
    // }
    
    // Send email from google to backend for validation and storage
    public postEmailToBackend(email: string):Observable<any>{
        const params = new HttpParams()
                    .set("email", email)
        return this.http.post(URL, { params })
        
    }

}