import {Component, OnInit, OnDestroy} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Quote, QuotesService} from './quotes.service';

@Component({
  selector: 'app-quotes',
  templateUrl: 'quotes.component.html',
})
export class QuotesComponent implements OnInit, OnDestroy {

  displayedColumns = ['ticker', 'price', 'instant'];

  quotes$: Observable<Array<Quote>>;

  constructor(private quotesService: QuotesService) {
  }

  ngOnInit() {
    this.quotes$ = this.quotesService.quotes();
  }

  ngOnDestroy() {
    // this.quotes$.unsubscribe();
  }

}
