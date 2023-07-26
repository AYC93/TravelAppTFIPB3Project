import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/api.service';
import { GoogleApiService } from 'src/app/google-api.service';
import { PlannerService } from 'src/app/planner.service';
import { FormField } from 'src/app/models/model';
import { formatDate } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Store } from '@ngrx/store';
import { emailSelector } from 'src/app/redux/selector';
import { ReduxAppState } from 'src/app/redux/state.model';


@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.css']
})
export class EntryComponent implements OnInit {
  googleSvc = inject(GoogleApiService)

  @ViewChild('uploadDoc')
  fileRef!: ElementRef
  email!: string
  fileSizeExceeded = false

  form!: FormGroup
  fb = inject(FormBuilder)
  router = inject(Router)
  snackBar = inject(MatSnackBar)

  googleLogin = inject(GoogleApiService)
  apiSvc = inject(ApiService)
  // includes Japanese cities and destination types
  svc = inject(PlannerService)

  constructor(private store: Store<ReduxAppState>) {
    this.store.select(emailSelector).subscribe((email: string) => this.email = email);
  }

  ngOnInit(): void {
    // initialise form
    this.form = this.createForm()
    this.svc.japCities.sort()
  }

  process() {
    const f: File = this.fileRef.nativeElement.files[0]
    const formField: FormField = {
      ... this.form.value,
      file: f
    }

    this.apiSvc.postFormToBackend(formField, this.email).then((resp) => {
      console.info("posted>>> City: " + formField.city + " Description: " + formField.description)
      console.log('Response from server... PID: ', resp.pid)
      // prompt to show successful post
      this.snackBar.open('Entry sucessful!', 'Close', {
        duration: 2500,
        panelClass: 'success-snackbar'
      })
      this.form.reset()
    }).catch(
      err => {
        console.log('Error... ', err)
      }
    )
  }

  private createForm(): FormGroup {

    formatDate(new Date(), 'dd/MM/yyyy hh:mm a', 'en')
    return this.fb.group({
      date: this.fb.control<Date>(new Date(), [Validators.required]),
      description: this.fb.control<string>('', [Validators.required,
      Validators.minLength(15), Validators.maxLength(1000)]),
      city: this.fb.control<string>('', [Validators.required]),
      destination: this.fb.control<string>('', [Validators.required]),
      file: this.fb.control<File | null>(null, [this.fileSizeValidators])
    })
  }

  private fileSizeValidators(control: AbstractControl): ValidationErrors | null{
    const file = control.value as File
    const maxSize = 20 * 1024 * 1024

    if (file && file.size > maxSize){
      return { fileSizeExceeded: true }
    }
    return null
  }

  fileSizeHTMLValidation(event: Event): void {
    const fileUpload = event.target as HTMLInputElement
    const file = fileUpload.files?.[0]

    this.fileSizeExceeded = file ? file.size > 20 * 1024 * 1024 : false

    const fileControl = this.form.get('file')
    if (fileControl) {
      if (this.fileSizeExceeded) {
        fileControl.setErrors({ fileSizeExceeded: true })
      } else {
        fileControl.setErrors(null)
      }
      fileControl.updateValueAndValidity()
    }
  }

  // authenticate login
  isLoggedIn(): boolean {
    return this.googleSvc.isLoggedIn()
  }

  //logout function
  logout() {
    this.googleSvc.logout()
  }

}
