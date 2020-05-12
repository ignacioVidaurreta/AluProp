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
    interests: new MatTableDataSource<Properties>(ELEMENT_DATA),
    properties: new MatTableDataSource<Properties>(ELEMENT_DATA),
    proposals: new MatTableDataSource<Properties>(ELEMENT_DATA),
  };

  constructor() { }

  ngOnInit(): void {
  }

}

export interface Properties {
  name: string;
}

const ELEMENT_DATA: Properties[] = [
  {name: 'Bonita casa en Punta Chica'},
  {name: 'Loft con vista al Rio'},
  {name: 'Departamento moderno'},
  {name: 'Bonita casa en Punta Chica'},
  {name: 'Loft con vista al Rio'},
  {name: 'Departamento moderno'},
  {name: 'Bonita casa en Punta Chica'},
  {name: 'Loft con vista al Rio'},
  {name: 'Departamento moderno'},
  {name: 'Bonita casa en Punta Chica'},
  {name: 'Loft con vista al Rio'},
  {name: 'Departamento moderno'},
  {name: 'Bonita casa en Punta Chica'},
  {name: 'Loft con vista al Rio'},
  {name: 'Departamento moderno'},
  {name: 'Bonita casa en Punta Chica'},
  {name: 'Loft con vista al Rio'},
  {name: 'Departamento moderno'},
  {name: 'Bonita casa en Punta Chica'},
  {name: 'Loft con vista al Rio'},
  {name: 'Departamento moderno'},
  {name: 'Bonita casa en Punta Chica'},
  {name: 'Loft con vista al Rio'},
  {name: 'Departamento moderno'},
  {name: 'Bonita casa en Punta Chica'},
  {name: 'Loft con vista al Rio'},
  {name: 'Departamento moderno'},
];
