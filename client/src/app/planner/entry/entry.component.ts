import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/api.service';
import { GoogleApiService } from 'src/app/google-api.service';
import { PlannerService } from 'src/app/planner.service';
import { CityWeather } from 'src/app/model';
import { formatDate } from '@angular/common';


@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.css']
})
export class EntryComponent implements OnInit{

  email!:string

  fb= inject(FormBuilder)
  router = inject(Router)

  googleSvc=inject(GoogleApiService)
  apiSvc=inject(ApiService)

  // includes Japanese cities and destination types
  svc=inject(PlannerService)

  form!:FormGroup


  ngOnInit(): void {
    this.email = this.googleSvc.email
    this.apiSvc.postEmailToBackend(this.email)
    this.form = this.createForm()
  }

  process(){
    // weather info process
    const weatherQuery = this.form.value as CityWeather
    weatherQuery.city = this.form.get('city')?.value as string
    // weather info process
  }

  private createForm(){
    formatDate(new Date(), 'dd/MM/yyyy hh:mm a', 'en')
    return this.fb.group({
      date: this.fb.control<Date>(new Date(), [Validators.required]),
      description: this.fb.control<string>('', [Validators.required]),
      city: this.fb.control<string>('', [Validators.required]),
      destination: this.fb.control<string>('', [Validators.required])
    })
  }

}
