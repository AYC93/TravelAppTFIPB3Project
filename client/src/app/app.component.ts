import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MetaReducer, Store } from '@ngrx/store';
import { hydrationMetaReducer } from './redux/hydration.reducer';
import { ReduxAppState } from './redux/state.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  router = inject(Router)
  store = inject(Store<ReduxAppState>)

  }

export const metaReducers: MetaReducer[] = [hydrationMetaReducer];