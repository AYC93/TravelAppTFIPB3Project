import { Component, OnChanges, OnInit, inject } from '@angular/core';
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

  center!:google.maps.LatLngLiteral
  lat!:number
  lon!:number

  pid!: number
  email!: string
  emailId!: number
  combinedModel$!: Promise<CombinedModel[]>

  googleLogin = inject(GoogleApiService)
  apiSvc = inject(ApiService)
  local = inject(LocalStorageService)

  ngOnInit(): void {
    // post email to backend to get email id etc
    this.postEmailToGetId()
    // dashboardViewInit
  }

  postEmailToGetId() {
    this.email = this.googleLogin.email
    if (this.email) {
      this.apiSvc.postEmailToBackend(this.email).subscribe({
        next: n => {
          console.log('Response from server... ', n)
          // to convert back to int before sending to server later
          localStorage.setItem("emailIdString", n.emailId.toString())
          this.emailId = this.local.emailIdLocalPull()
          console.info("posted>>> " + this.email)
          console.info("EmailId = " + this.emailId)
          try{
          this.combinedModel$ = this.apiSvc.getDataFromServer(this.emailId)
          this.combinedModel$.then(resp =>{
            console.log("Server sent data to view: ")
            resp.forEach((data, i)=> {
              console.log(`Row ${i}: `, data)
            })})
        } catch (err) {
          console.error('Error from server', err)
        }
        },
        error: err => { console.log('Error!!!... ', err) }
      })
    }
  }

  populateView() {
    try {
      this.combinedModel$ = this.apiSvc.getDataFromServer(this.emailId)
      this.combinedModel$.then(resp =>
        console.log("Server sent data to view: " + resp.forEach))
    } catch (err) {
      console.error('Error from server', err)
    }
  }

  // to add in delete for s3 as well
  deleteRow(pid: number){
    this.apiSvc.deleteDataFromServer(pid).then(
      resp=> {
      // not refreshing, to see again
      this.populateView()
      console.log(resp)
    }).catch(err=>{
      console.error('Error with deleting...', err)
    }
    )
  }

  //TODO to display all the things input from form
}
