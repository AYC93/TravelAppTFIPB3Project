import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../api.service';
import { GoogleApiService } from '../google-api.service';
// import { GoogleApiService } from '../google-api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  email!:string

  router=inject(Router)

  svc=inject(ApiService)
  
  constructor(readonly googleLogin: GoogleApiService){
    console.info(">>> email: " + (googleLogin.email) )
    console.info(">>> idToken: " + (googleLogin.idToken) )
  }

  ngOnInit(): void {
    this.email = this.googleLogin.email
    if(this.email)
      this.svc.postEmailToBackend(this.email)
  }

  login(){
    // let resp = this.svc.login(this.username, this.password)
    // // resp.subscribe(data => {
    // //   console.log(data)
    // // })
  }

}
