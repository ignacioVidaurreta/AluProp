import { Component, OnInit, ViewChild, ElementRef, HostListener } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnInit {

  showFilters = false;
  searchFilters: any;

  @ViewChild('searchInput') searchInput: ElementRef;


  @HostListener('document:click', ['$event']) 
  onDocumentClick(event) {
    if (this.showFilters === true && event.path.filter(
        (elem)=> elem.id === 'toggle-filters' || 
                            elem.id === 'filters' || 
                            elem.classList?.contains('filter-select-option')).length !== 1){
      this.showFilters = false;
      console.log('dsfdsf');
    }
  }

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.searchFilters = {};
    // setInterval(()=>console.log(this.showFilters), 10);
  }

  toggleFilters(){
    this.showFilters = !this.showFilters;
  }

  search(){
    this.showFilters = false;
    this.searchFilters.description = this.searchInput.nativeElement.value;
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
