import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms'
import { Routes, RouterModule } from '@angular/router';
// import { AuthModule } from '@auth0/auth0-angular';

import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';


import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './login/home.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ApiService } from './api.service';
import { MainComponent } from './planner/main/main.component';
import { DocumentComponent } from './planner/document/document.component';
import { WeatherComponent } from './planner/weather/weather.component';
import { PlannerService } from './planner.service';
import { EntryComponent } from './planner/entry/entry.component';
import { OAuthModule } from 'angular-oauth2-oidc';
import { GoogleApiService } from './google-api.service';
import { AuthGuard } from './auth-guard.service';

const appRoutes: Routes = [
  { path: '', component: HomeComponent, title: 'Home'},
  { path: 'login', component: LoginComponent, title: 'Login Page'},
  { path: 'entry', component: EntryComponent, canActivate:[AuthGuard], title: 'Plan Entry'},
  { path: '**', redirectTo: '/', pathMatch: 'full' }
  
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    MainComponent,
    DocumentComponent,
    WeatherComponent,
    EntryComponent,
  ],
  imports: [
    BrowserModule, HttpClientModule, ReactiveFormsModule, RouterModule.forRoot(appRoutes, { scrollPositionRestoration: "enabled" }), BrowserAnimationsModule, MatButtonModule, MatSelectModule,
    OAuthModule.forRoot()
  ],
  providers: [ApiService, PlannerService, GoogleApiService, AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
