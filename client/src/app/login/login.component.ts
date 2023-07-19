import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../api.service';
import { GoogleApiService, UserInfo } from '../google-api.service';
// import { GoogleApiService } from '../google-api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  userInfo?: UserInfo

  router=inject(Router)

  svc=inject(ApiService)
  
  constructor(readonly googleLogin: GoogleApiService){
    googleLogin.userProfileSubject.subscribe((info) =>{
      this.userInfo = info
    })
    console.info(">>> userinfo: " + this.userInfo?.info)
  }

  ngOnInit(): void {
  }


  login(){
    // let resp = this.svc.login(this.username, this.password)
    // // resp.subscribe(data => {
    // //   console.log(data)
    // // })
  }





}
