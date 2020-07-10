import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

  queryParamsSub: Subscription;
  queryParams: any;

  constructor(private route: ActivatedRoute) {
    this.queryParamsSub = route.queryParams.subscribe((params) => this.queryParams = params);
  }

  ngOnInit(): void {}

  ngOnDestroy(): void {
    if (this.queryParamsSub){ this.queryParamsSub.unsubscribe() };
  }

}
