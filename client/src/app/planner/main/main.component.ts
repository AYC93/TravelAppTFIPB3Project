import { Component, OnInit, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { ApiService } from 'src/app/api.service';
import { GoogleApiService } from 'src/app/google-api.service';
import { LocalStorageService } from 'src/app/localstorage.service';
import { CombinedModel } from 'src/app/models/model';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {


  email!: string
  emailId!: number
  combinedModel$!: Promise<CombinedModel[]>

  googleLogin = inject(GoogleApiService)
  apiSvc = inject(ApiService)
  local = inject(LocalStorageService)

  ngOnInit(): void {
    // post email to backend to get email id etc
    this.postEmailToGetId()
    this.populateView();
    // dashboardViewInit

  }

  async postEmailToGetId() {
    this.email = this.googleLogin.email
    this.emailId = this.local.emailIdLocalPull()
    if (this.email) {
      this.apiSvc.postEmailToBackend(this.email).subscribe({
        next: n => {
          console.log('Response from server... ', n)
          // to convert back to int before sending to server later
          localStorage.setItem("emailIdString", n.emailId.toString())
          console.info("posted>>> " + this.email)
        },
        error: err => { console.log('Error!!!... ', err) }
      })
    }
  }

  async populateView() {
    try {
      this.combinedModel$ = this.apiSvc.getDataFromServer(this.emailId)
      this.combinedModel$.then(resp =>
        console.log("Server sent data to view: " + resp))
    } catch (err) {
      console.error('Error from server', err)
    }
  }

  //TODO to display all the things input from form
}
