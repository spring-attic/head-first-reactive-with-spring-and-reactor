import {Injectable, NgZone} from '@angular/core';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class QuotesService {

  quotesCache: Array<Quote> = [];

  eventSource: any = window['EventSource'];

  quotesObservable: Observable<Array<Quote>>;

  constructor(private ngZone: NgZone) {
    this.quotesObservable = new Observable<Array<Quote>>(obs => {
      const eventSource = new this.eventSource('/quotes/feed');
      eventSource.onmessage = event => {
        if (this.quotesCache.length > 50) {
          this.quotesCache = this.quotesCache.slice(0, 50);
        }
        this.quotesCache = [JSON.parse(event.data)].concat(this.quotesCache);
        this.ngZone.run(() => obs.next(this.quotesCache));
      };
      return () => eventSource.close();
    });
  }

  quotes(): Observable<Array<Quote>> {
    return this.quotesObservable;
  }

}

export interface Ticker {
  key: string;
  current: Quote;
  quotes: Array<Quote>;
}

export interface Quote {
  ticker: string;
  price: number;
  instant: number;
}
