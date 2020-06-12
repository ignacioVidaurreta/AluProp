import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnInit {

  showFilters = false;
  searchFilters = {};

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  toggleFilters(){
    this.showFilters = !this.showFilters;
  }

  search(){
    this.showFilters = false;
    this.router.navigate(['/'], { queryParams: this.searchFilters});
  }

  setSearchFilters(filters: any){
    this.searchFilters = this.deleteEmptyElements(filters);
  }

  deleteEmptyElements(params: any = {}) {
    Object.keys(params).forEach(
        (key) => {
          if (params[key] == null || params[key] === '') {
            delete params[key];
            return;
          }
        }
      );
      return params;
}

}
