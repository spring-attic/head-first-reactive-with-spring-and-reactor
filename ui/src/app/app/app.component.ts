import {Component, DoCheck} from '@angular/core';
import {OnInit} from '@angular/core';
import {QuotesService} from '../quotes/quotes.service';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
})
export class AppComponent implements DoCheck, OnInit {

  constructor(private quotesService: QuotesService) {

  }

  ngOnInit() {
  }

  ngDoCheck() {
  }

}
