import { AfterViewInit, Component, OnInit, inject } from '@angular/core';
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
export class LoginComponent implements AfterViewInit {

  email!: string

  router = inject(Router)

  svc = inject(ApiService)

  constructor(readonly googleLogin: GoogleApiService) {
    console.info(">>> email: " + (googleLogin.email))
    console.info(">>> idToken: " + (googleLogin.idToken))
  }

  ngAfterViewInit(): void {
    this.email = this.googleLogin.email
    if (this.email) {
      this.svc.postEmailToBackend(this.email).subscribe({
        next: n => {
          console.log('Response from server... ', n)
          console.info("posted>>> " + this.email)
        },
        error: err => { console.log('Error!!!... ', err) }
      })
    }
  }

  login() {
    // let resp = this.svc.login(this.username, this.password)
    // // resp.subscribe(data => {
    // //   console.log(data)
    // // })
  }

}
