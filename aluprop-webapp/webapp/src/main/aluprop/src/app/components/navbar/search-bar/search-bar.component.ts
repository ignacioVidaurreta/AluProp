import { Component, OnInit, ViewChild, ElementRef, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import {Subject} from "rxjs";
import { filter } from 'rxjs/operators';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnInit {

  showFilters = false;
  filterForm: FormGroup;
  resetFilters: Subject<void> = new Subject<void>();

  @ViewChild('searchInput') searchInput: ElementRef;


  @HostListener('document:click', ['$event'])
  onDocumentClick(event) {
    if (this.showFilters === true && event.path.filter(
        (elem) => elem.id === 'toggle-filters' ||
                            elem.id === 'filters' ||
                            elem.classList?.contains('filter-select-option')||
                            elem.classList?.contains('cdk-overlay-backdrop')||
                            elem.id === 'search-button').length === 0){
      this.showFilters = false;
    }
  }

  constructor(private router: Router) { }

  ngOnInit(): void {
    // setInterval(()=>console.log(this.showFilters), 10);
  }

  toggleFilters(){
    this.showFilters = !this.showFilters;
  }

  search(){
    if (!this.filterForm && !this.searchInput.nativeElement.value)
      return;
    const searchFilters: any = {};
    if (this.filterForm){
      if (!this.filterForm.valid)
        return;
      Object.assign(searchFilters, this.deleteEmptyElements(this.filterForm.value));
    }
    if (this.searchInput.nativeElement.value)
      searchFilters.description = this.searchInput.nativeElement.value;
    this.router.navigate(['/'], { queryParams: searchFilters});
    this.searchInput.nativeElement.value = "";
    this.searchInput.nativeElement.blur();
    this.resetFilters.next();
    this.showFilters = false;
  }

  setFilterForm(filterForm: FormGroup){
    this.filterForm = filterForm; //this.searchFilters, this.deleteEmptyElements(filters));
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
