import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PlannerService } from 'src/app/planner.service';
import { CityWeather } from 'src/app/weather.models';

@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.css']
})
export class EntryComponent implements OnInit{

  fb= inject(FormBuilder)
  router = inject(Router)

  svc=inject(PlannerService)

  form!:FormGroup


  ngOnInit(): void {
      this.form = this.createForm()
  }

  process(){
    // weather info process
    const weatherQuery = this.form.value as CityWeather
    weatherQuery.city = this.form.get('city')?.value as string
    // weather info process
  }

  private createForm(){
    return this.fb.group({
      city: this.fb.control<string>('',[Validators.required])
    })
  }

}
