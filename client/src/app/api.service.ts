import { Inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable()
export class ApiService{

    http = Inject(HttpClient)
    
    public login(username:string, password:string){
        const headers = new HttpHeaders({
            Authorization: 'Basic' + btoa(username + ":" + password)
        })
        return this.http.get("http://localhost:8080/", {headers})
    }

}