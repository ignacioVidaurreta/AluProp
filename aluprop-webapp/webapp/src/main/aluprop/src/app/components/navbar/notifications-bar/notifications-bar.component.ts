import { Component, OnInit, HostListener } from '@angular/core';

@Component({
  selector: 'app-notifications-bar',
  templateUrl: './notifications-bar.component.html',
  styleUrls: ['./notifications-bar.component.scss']
})
export class NotificationsBarComponent implements OnInit {

  showNotifications = false;

  @HostListener('document:click', ['$event']) 
  onDocumentClick(event) {
    if (this.showNotifications === true && event.path.filter(
        (elem)=> elem.id === 'toggle-notifications').length !== 1){
      this.showNotifications = false;
    }
  }

  constructor() { }

  ngOnInit(): void {
  }

  toggleNotifications(){
    this.showNotifications = !this.showNotifications;
  }

}
