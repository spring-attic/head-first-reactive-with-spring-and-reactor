import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HeaderComponent} from './core/layout/header/header.component';
import {FooterComponent} from './core/layout/footer/footer.component';
import {MatButtonModule, MatCheckboxModule, MatTableModule} from '@angular/material';
import {QuotesComponent} from './quotes/quotes.component';
import {QuotesService} from './quotes/quotes.service';
import {HttpClientModule} from '@angular/common/http';
import {AppComponent} from './app/app.component';

@NgModule({
  declarations: [
    AppComponent,
    QuotesComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCheckboxModule,
    MatTableModule,
    HttpClientModule
  ],
  providers: [
    QuotesService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
