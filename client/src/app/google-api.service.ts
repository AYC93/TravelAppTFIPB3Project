import { Injectable, inject } from "@angular/core";
import { Router } from "@angular/router";
import { AuthConfig, OAuthService } from "angular-oauth2-oidc";
import { Subject, filter } from "rxjs";

const oAuthConfig: AuthConfig = {
    issuer: 'https://accounts.google.com',
    strictDiscoveryDocumentValidation: false,
    redirectUri: window.location.origin + '/login',
    clientId: '795945122365-f88an7rsf6qb9q4dcplmdi4qvkp6aph5.apps.googleusercontent.com',
    scope: 'openid profile email',
    showDebugInformation: true
}


@Injectable()
export class GoogleApiService {

    router=inject(Router)

    constructor(private readonly oAuthSvc: OAuthService) {
        oAuthSvc.configure(oAuthConfig)
        
        this.oAuthSvc.loadDiscoveryDocument();
        this.oAuthSvc.setupAutomaticSilentRefresh();
    
        // Automatically load user profile
        this.oAuthSvc.events
          .pipe(filter( (e) => e.type === 'token_received'))
          .subscribe( (_) => this.oAuthSvc.loadUserProfile());
      }
    
      get email(): string {
        const claims = this.oAuthSvc.getIdentityClaims();
        if (!claims) return '';
        return claims['email'];
      }

      get idToken(): string {
        return this.oAuthSvc.getIdToken();
      }

      // ensure user is logged in
      isLoggedIn():boolean{
        return this.oAuthSvc.hasValidAccessToken()
      }

      public login(targetUrl?: string) {
        this.oAuthSvc.initLoginFlow(targetUrl || this.router.url);
      }

    }
