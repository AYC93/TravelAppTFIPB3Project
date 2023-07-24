import { Component, OnChanges, OnInit, inject } from '@angular/core';
import { ApiService } from 'src/app/api.service';
import { GoogleApiService } from 'src/app/google-api.service';
import { LocalStorageService } from 'src/app/localstorage.service';
import { CombinedModel } from 'src/app/models/model';
import { Store } from '@ngrx/store';
import { emailSelector, emailIdSelector, dashboardSelector } from 'src/app/redux/selector';
import { ReduxAppState } from 'src/app/redux/state.model';
import { updateEntriesAction } from 'src/app/redux/dashboard/dashboard.actions';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  // Google maps
  center!:google.maps.LatLngLiteral
  lat!:number
  lon!:number

  pid!: number
  email!: string
  emailId!: number
  combinedModel$!: Promise<CombinedModel[]>
  fetchedCombinedModel!: CombinedModel[]

  googleLogin = inject(GoogleApiService)
  apiSvc = inject(ApiService)
  local = inject(LocalStorageService)

  constructor(private store: Store<ReduxAppState>) {
    this.store.select(emailSelector).subscribe((email: string) => this.email = email);
    this.store.select(emailIdSelector).subscribe((emailId: number) => this.emailId = emailId);
  }

  ngOnInit(): void {
    // dashboardViewInit
    this.populateView();
  }

  // postEmailToGetId() {
  //   this.email = this.googleLogin.email
  //   if (this.email) {
  //     this.apiSvc.postEmailToBackend(this.email).subscribe({
  //       next: n => {
  //         console.log('Response from server... ', n)
  //         // to convert back to int before sending to server later
  //         localStorage.setItem("emailIdString", n.emailId.toString())
  //         this.emailId = this.local.emailIdLocalPull()
  //         console.info("posted>>> " + this.email)
  //         console.info("EmailId = " + this.emailId)
  //         try{
  //         this.combinedModel$ = this.apiSvc.getDataFromServer(this.emailId)
  //         this.combinedModel$.then(resp =>{
  //           console.log("Server sent data to view: ")
  //           resp.forEach((data, i)=> {
  //             console.log(`Row ${i}: `, data)
  //           })})
  //       } catch (err) {
  //         console.error('Error from server', err)
  //       }
  //       },
  //       error: err => { console.log('Error!!!... ', err) }
  //     })
  //   }
  // }

  populateView() {
    try {
      this.apiSvc.getDataFromServer(this.emailId).then((resp) => {
        this.fetchedCombinedModel = resp;
        this.store.dispatch(updateEntriesAction({payload: this.fetchedCombinedModel}))
        this.store.select(dashboardSelector)
          .subscribe((entries: CombinedModel[]) => this.combinedModel$ = new Promise(res => res(entries))); //refactor
      })
    } catch (err) {
      console.error('Error from server', err)
    }
  }

  // populateView() {
  //   try {
  //     this.combinedModel$ = this.apiSvc.getDataFromServer(this.emailId)
  //     this.combinedModel$.then(resp =>
  //       console.log("Server sent data to view: " + resp.forEach))
  //   } catch (err) {
  //     console.error('Error from server', err)
  //   }
  // }

  // to add in delete for s3 as well
  deleteRow(pid: number){
    this.apiSvc.deleteDataFromServer(pid).then(
      resp=> {
      this.populateView()
      console.log(resp)
    }).catch(err=>{
      console.error('Error with deleting...', err)
    })
  }

  //TODO to display all the things input from form
}
