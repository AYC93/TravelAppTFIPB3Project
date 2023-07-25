import { Component, OnInit, inject } from '@angular/core';
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

  populateView() {
    try {
      this.apiSvc.getDataFromServer(this.emailId).then((resp) => {
        this.fetchedCombinedModel = resp;
        this.store.dispatch(updateEntriesAction({payload: this.fetchedCombinedModel}))
        this.store.select(dashboardSelector)
          .subscribe((entries: CombinedModel[]) => this.combinedModel$ = new Promise(res => res(entries)));
      })
    } catch (err) {
      console.error('Error from server', err)
    }
  }

  deleteRow(pid: number){
    this.apiSvc.deleteDataFromServer(pid).then(
      resp=> {
      this.populateView()
      console.log(resp)
    }).catch(err=>{
      console.error('Error with deleting...', err)
    })
  }
}
