import { Component, OnInit, ViewChild } from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user = {
    firstName: 'Clara',
    lastName: 'Guzzetti',
    university: 'ITBA',
    birthDate: '07/07/1993',
    bio: 'Me gustan los drones',
    role: 'host',
    id: 1,
    interests: new MatTableDataSource<Properties>(ELEMENT_DATA_PROPERTIES),
    properties: new MatTableDataSource<Properties>(ELEMENT_DATA_PROPERTIES),
    proposals: new MatTableDataSource<Proposals>(ELEMENT_DATA_PROPOSALS),
  };

  constructor() { }

  ngOnInit(): void {
  }

}

export interface Properties {
  name: string;
  availability: string;
}

const ELEMENT_DATA_PROPERTIES: Properties[] = [
  {name: 'Bonita casa en Punta Chica', availability: 'available'},
  {name: 'Loft con vista al Rio', availability: 'available'},
  {name: 'Departamento moderno', availability: 'unavailable'},
  {name: 'Bonita casa en Punta Chica', availability: 'available'},
  {name: 'Loft con vista al Rio', availability: 'available'},
  {name: 'Bonita casa en Punta Chica', availability: 'unavailable'},
  {name: 'Loft con vista al Rio', availability: 'unavailable'},
  {name: 'Departamento moderno', availability: 'available'},
  {name: 'Bonita casa en Punta Chica', availability: 'available'},
  {name: 'Loft con vista al Rio', availability: 'available'},
];


export interface Proposals {
  name: string;
  creatorId: number;
  state: string;
}

const ELEMENT_DATA_PROPOSALS: Proposals[] = [
  {name: 'Bonita casa en Punta Chica', creatorId: 1, state: 'sent'},
  {name: 'Loft con vista al Rio', creatorId: 2, state: 'accepted'},
  {name: 'Departamento moderno',  creatorId: 1, state: 'declined'},
  {name: 'Bonita casa en Punta Chica', creatorId: 1, state: 'dropped'},
  {name: 'Loft con vista al Rio', creatorId: 2, state: 'sent'},
  {name: 'Departamento moderno',  creatorId: 1, state: 'sent'},
];
