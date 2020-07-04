import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {

  errorCode: number;

  constructor( private route: ActivatedRoute ) { }

  ngOnInit(): void {
    this.errorCode = +this.route.snapshot.paramMap.get("code");
  }

}
